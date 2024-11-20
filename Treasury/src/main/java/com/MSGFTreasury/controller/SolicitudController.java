package com.MSGFTreasury.controller;

import com.MSGFTreasury.dto.Response;
import com.MSGFTreasury.model.Estudiante;
import java.util.List;
import org.springframework.ui.Model;
import com.MSGFTreasury.model.SolicitudExterior;
import com.MSGFTreasury.services.MinioService;
import org.springframework.stereotype.Controller;
import com.MSGFTreasury.services.SolicitudService;
import java.util.HashMap;
import java.util.Map;
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
    
    @GetMapping("/solicitudes_ise")
    public String listarEstudiantesIES(Model model) {
        List<SolicitudExterior> solicitudes = solicitudService.listarSolicitudesExteriores();
        model.addAttribute("solicitudes", solicitudes);

        return "views/solicitudes_ise";
    }
    
    @PostMapping("/consultar_informacion_estudiante")
    public String consultarInformacionEstudiante(
            @RequestParam(name = "solicitud_id") Long solicitud_id,
            Model model)
    {
        solicitudService.consultarInformacionEstudiante(solicitud_id);
        
        Response response = solicitudService.obtenerMaterias(solicitud_id);
        
        Map<String, Integer> materias = response.getValue();
        

        // Calcular el promedio
        double promedio = materias.values().stream()
                                  .mapToInt(Integer::intValue)
                                  .average()
                                  .orElse(0.0);
        
        Estudiante estudiante = solicitudService.obtenerEstudiante(solicitud_id);

        // Pasar datos a la vista
        model.addAttribute("materias", materias);
        model.addAttribute("promedio", promedio);
        model.addAttribute("estudiante", estudiante);

        return "views/notas";
    }
    
    @PostMapping(value = "/subir_certificado", consumes = "multipart/form-data")
    public String subirAnexo(
            @RequestParam("certificado") MultipartFile certificado,
            @RequestParam(name = "solicitud_id") Long solicitud_id,
            Model model) throws Exception
    {
        String fileName = solicitud_id + "_certificado_" + certificado.getOriginalFilename();

        minioService.uploadFile(fileName, certificado.getInputStream(), certificado.getContentType());

        solicitudService.guardarCertificado(solicitud_id, fileName);
        
        return  this.listarEstudiantesIES(model);
    }
}
