package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.model.Anexo;
import com.MSGFCentralSys.MSGFCentralSys.model.Solicitud;
import com.MSGFCentralSys.MSGFCentralSys.model.Tramite;
import com.MSGFCentralSys.MSGFCentralSys.services.SolicitudService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jacke
 */
@Controller
public class SolicitudController {
    
    @Autowired
    private SolicitudService solicitudService;
    
    private String proceso = null;
    
    @GetMapping("/solicitudes")
    public String listarEstudiantes(Model model) {
        List<Solicitud> estudiantes = solicitudService.listarSolicitudes();
        model.addAttribute("solicitudes", estudiantes);
        model.addAttribute("proceso_id", this.proceso);

        return "views/solicitudes_index";
    }
    
    @GetMapping("/tramites")
    public String listarTramites(Model model) {
        List<Tramite> tramites = solicitudService.listarTramites();
        model.addAttribute("tramites", tramites);

        return "views/tramites";
    }
    
    @PostMapping("/solicitar_equivalencia")
    public String solicitarEquivalencia(
            @RequestParam("tramite_id") Long tramite_id,
            Model model
    ) {
        solicitudService.solicitarEquivalencia(tramite_id);

        return "views/tramites";
    }
    
    @GetMapping("/ver_anexos")
    public String viewTaskValidate(
            @RequestParam(name = "solicitud_id") Long solicitud_id,
            Model model)
    {
        List<Anexo> anexos = solicitudService.obtenerAnexos(solicitud_id);

        model.addAttribute("anexos", anexos);

        return  "views/anexos";
    }
}
