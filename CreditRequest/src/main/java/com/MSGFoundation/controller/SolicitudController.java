package com.MSGFoundation.controller;

import com.MSGFoundation.model.Estudiante;
import java.util.List;
import org.springframework.ui.Model;
import com.MSGFoundation.model.Solicitud;
import com.MSGFoundation.model.SolicitudExterior;
import com.MSGFoundation.service.MinioService;
import org.springframework.stereotype.Controller;
import com.MSGFoundation.service.SolicitudService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jacke
 */
@Controller
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;
    
    @Autowired
    private MinioService minioService;
    
    @GetMapping("/solicitudes")
    public String listarEstudiantes(Model model) {
        List<Solicitud> estudiantes = solicitudService.listarSolicitudes();
        model.addAttribute("solicitudes", estudiantes);

        return "views/solicitudes_index";
    }
    
    @PostMapping(value = "/subir_anexo", consumes = "multipart/form-data")
    public String subirAnexo(
            @RequestParam("pdf_mensual") MultipartFile pdf_mensual,
            @RequestParam(name = "solicitud_id") Long solicitud_id,
            Model model) throws Exception
    {
        String fileName = solicitud_id + "_" + pdf_mensual.getOriginalFilename();

        minioService.uploadFile(fileName, pdf_mensual.getInputStream(), pdf_mensual.getContentType());
        
        String fileName1Firmada1 = minioService.generarPresignedUrl("camundanzia", fileName);

        solicitudService.guardarReporteMensual(solicitud_id, fileName1Firmada1);
        return  this.listarEstudiantes(model);
    }
    
    @PostMapping(value = "/subir_anexos", consumes = "multipart/form-data")
    public String subirAnexos(
            @RequestParam("pdf_final") MultipartFile pdf_final,
            @RequestParam("pdf_certificado") MultipartFile pdf_certificado,
            @RequestParam(name = "solicitud_id2") String solicitud_id2,
            Model model) throws Exception
    {
        System.out.println("valor solicitud: " + solicitud_id2);
        
        Long solicitud_id = Long.valueOf(solicitud_id2);
        
        String fileName1 = solicitud_id + "_" + pdf_final.getOriginalFilename();

        minioService.uploadFile(fileName1, pdf_final.getInputStream(), pdf_final.getContentType());
        
        String fileName1Firmada = minioService.generarPresignedUrl("camundanzia", fileName1);
        
        String fileName2 = solicitud_id + "_" + pdf_certificado.getOriginalFilename();

        minioService.uploadFile(fileName2, pdf_certificado.getInputStream(), pdf_certificado.getContentType());
        
        String fileName1Firmada2 = minioService.generarPresignedUrl("camundanzia", fileName2);

        solicitudService.guardarReporteFinal(solicitud_id, fileName1Firmada, fileName1Firmada2);
        
        return  this.listarEstudiantes(model);
    }
    
    @GetMapping("/solicitudes_ise")
    public String listarEstudiantesIES(Model model) {
        List<SolicitudExterior> solicitudes = solicitudService.listarSolicitudesExteriores();
        model.addAttribute("solicitudes", solicitudes);

        return "views/solicitudes_ise";
    }
    
    @PostMapping("/solicitar_certificado")
    public String solicitarCertificado(
            @RequestParam(name = "solicitud_id") Long solicitud_id,
            Model model
    ) {
        
        solicitudService.actualizarEstadoCertificado(solicitud_id);

        return this.listarEstudiantesIES(model);
    }
}
