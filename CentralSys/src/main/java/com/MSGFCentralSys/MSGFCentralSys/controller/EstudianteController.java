package com.MSGFCentralSys.MSGFCentralSys.controller;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import com.MSGFCentralSys.MSGFCentralSys.model.Estudiante;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.MSGFCentralSys.MSGFCentralSys.services.EstudianteService;

/**
 *
 * @author jacke
 */
@Controller
public class EstudianteController {
    
    @Autowired
    private EstudianteService estudianteService;
    
    @GetMapping("/estudiantes")
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteService.listarEstudiantes();
        model.addAttribute("estudiantes", estudiantes);

        return "views/estudiantes_index";
    }
    
    @PostMapping("/proceso")
    public  String proceso(
            @RequestParam("estudiante_id") Long estudiante_id,
            Model model)
    {
        String proceso_id = estudianteService.iniciarProceso(estudiante_id);
        estudianteService.actualizarProcesoEstudiante(estudiante_id, proceso_id);
        System.out.println("id del proceso: " + proceso_id);
        return  this.listarEstudiantes(model);
    }
    
    @PostMapping("/reporte_mensual")
    public  String reporteMensual(
            @RequestParam("estudiante_id") Long estudiante_id,
            Model model)
    {
        estudianteService.generarSolicitudMensual(estudiante_id);
        return  this.listarEstudiantes(model);
    }
    
    @PostMapping("/reporte_final")
    public  String reporteFinal(
            @RequestParam("estudiante_id") Long estudiante_id,
            Model model)
    {
        estudianteService.generarSolicitudFinal(estudiante_id);
        return  this.listarEstudiantes(model);
    }
}
