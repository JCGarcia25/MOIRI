/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MSGFoundation.service;

import com.MSGFoundation.model.Anexo;
import com.MSGFoundation.model.Estudiante;
import com.MSGFoundation.model.Solicitud;
import com.MSGFoundation.model.SolicitudExterior;
import com.MSGFoundation.repository.AnexoRepository;
import com.MSGFoundation.repository.EstudianteRepository;
import com.MSGFoundation.repository.SolicitudExteriorRepository;
import com.MSGFoundation.repository.SolicitudRepository;
import com.msgfoundation.annotations.BPMNTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author jacke
 */
@Service
@RequiredArgsConstructor
@BPMNTask(type = "userTask", name = "Solicitar equivalencia de notas")
public class SolicitudService {
    
    @Value("${API_URL}")
    private String camundaUrl;

    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private AnexoRepository anexoRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private SolicitudExteriorRepository solicitudExteriorRepository;
    
    @Autowired
    private MinioService minioService;
    
    private final RestTemplate restTemplate;
    
    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findByCargadaFalse();
    }
    
    public List<SolicitudExterior> listarSolicitudesExteriores() {
        List<SolicitudExterior> solicitudes = solicitudExteriorRepository.findAll();

        // Generar la URL pre-firmada para cada solicitud
        for (SolicitudExterior solicitud : solicitudes) {
            String anexo = solicitud.getAnexo();
            if (anexo != null && !anexo.isEmpty()) {
                String presignedUrl = minioService.generarPresignedUrl("camundanzia", anexo);
                solicitud.setAnexo(presignedUrl); // Ajusta el valor de "anexo" al URL pre-firmado
            }
        }

        return solicitudes;
    }
    
    public void actualizarEstadoCertificado(Long solicitud_id)
    {
        SolicitudExterior solicitud = solicitudExteriorRepository.getOne(solicitud_id);
        solicitud.setCargada(true);
        
        
        this.solicitarCertificadoISE(solicitud.getEstudiante().getProceso());
        
        String proceso_id = this.vincularSolicitudExteriorAProceso();
        solicitud.setProceso(proceso_id);
        solicitudExteriorRepository.save(solicitud);
    }
    
    public String vincularSolicitudExteriorAProceso()
    {
        String camunda_url = camundaUrl + "process-definition/key/SolicitarCertificadoNotasFinales";

        Map<String, Object> processDefinition = restTemplate.getForObject(camunda_url, Map.class);

        // Verificar si se obtuvo una respuesta y devolver el ID del proceso
        if (processDefinition != null && processDefinition.containsKey("id")) {
            return (String) processDefinition.get("id"); // Extraer el ID del proceso
        } else {
            return null; // Si no se encuentra el ID, devolver null
        }
    }
    
    public List<Anexo> obtenerAnexos(Long solicitud_id)
    {
        Solicitud solicitud = solicitudRepository.getOne(solicitud_id);
        
        return solicitud.getAnexos();
    }
    
    public void guardarReporteMensual(Long solicitud_id, String fileName) throws Exception
    {
        Solicitud solicitud = solicitudRepository.getOne(solicitud_id);
        Estudiante estudiante = estudianteRepository.getOne(solicitud.getEstudiante().getId());
        
        Anexo anexo = new Anexo();
        anexo.setNombreArchivo(fileName);
        anexo.setSolicitud(solicitud);
        anexoRepository.save(anexo);
        
        enviarInformeMensual(estudiante.getProceso());
        
        
        estudiante.setMes_actual(2);
        estudiante.setEstado("En Proceso Final");
        estudianteRepository.save(estudiante);
        
        solicitud.setCargada(true);
        solicitudRepository.save(solicitud);
    }
    
    public void guardarReporteFinal(Long solicitud_id, String fileName1, String fileName2) throws Exception
    {
        Solicitud solicitud = solicitudRepository.getOne(solicitud_id);
        Estudiante estudiante = estudianteRepository.getOne(solicitud.getEstudiante().getId());
    
        Anexo anexo = new Anexo();
        anexo.setNombreArchivo(fileName1);
        anexo.setSolicitud(solicitud);
        anexoRepository.save(anexo);
        
        Anexo anexo2 = new Anexo();
        anexo2.setNombreArchivo(fileName2);
        anexo2.setSolicitud(solicitud);
        anexoRepository.save(anexo2);
        
        enviarInformeFinal(estudiante.getProceso());
        
        
        estudiante.setMes_actual(3);
        estudianteRepository.save(estudiante);
        
        solicitud.setCargada(true);
        solicitudRepository.save(solicitud);
    }
    
    public String obtenerCodigoTask(String proceso_id, String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
                .queryParam("executionId", proceso_id)
                .queryParam("taskDefinitionKey", key_task)
                .toUriString();

        // Realizar la solicitud GET
        Map<String, Object>[] tasks = restTemplate.getForObject(url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
    
    public void enviarInformeMensual(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTask(proceso_id, "Activity_1cw7qj5");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public void enviarInformeFinal(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTaskEnviarInformeFinal(proceso_id, "Activity_1m988xv");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public String obtenerCodigoTaskEnviarInformeFinal(String proceso_id, String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
                .queryParam("processInstanceId", proceso_id)
                .queryParam("taskDefinitionKey", key_task)
                .toUriString();

        // Realizar la solicitud GET
        Map<String, Object>[] tasks = restTemplate.getForObject(url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
    
    public void solicitarCertificadoISE(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTaskParaISE(proceso_id, "Activity_15zl1rf");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public String obtenerCodigoTaskParaISE(String proceso_id, String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
                .queryParam("processInstanceId", proceso_id)
                .queryParam("taskDefinitionKey", key_task)
                .toUriString();

        // Realizar la solicitud GET
        Map<String, Object>[] tasks = restTemplate.getForObject(url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
}
