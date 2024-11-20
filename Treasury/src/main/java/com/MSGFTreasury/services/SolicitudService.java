/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MSGFTreasury.services;

import com.MSGFTreasury.dto.Response;
import com.MSGFTreasury.model.Estudiante;
import com.MSGFTreasury.model.SolicitudExterior;
import com.MSGFTreasury.repository.SolicitudExteriorRepository;
import com.msgfoundation.annotations.BPMNTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@BPMNTask(type = "userTask", name = "Consultar  información sobre el estudiante")
public class SolicitudService {
    
    @Value("${API_URL}")
    private String camundaUrl;
    
    @Autowired
    private SolicitudExteriorRepository solicitudExteriorRepository;
    
    @Autowired
    private MinioService minioService;
    
    private final RestTemplate restTemplate;
   
    
    public List<SolicitudExterior> listarSolicitudesExteriores() {
        return solicitudExteriorRepository.findAll();
    }
    
    
    public String obtenerCodigoTask(String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
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
    
    public void consultarInformacionEstudiante(Long solicitud_id) {
        
        SolicitudExterior solicitud = solicitudExteriorRepository.getOne(solicitud_id);
        
        try {
            String task_id = this.obtenerCodigoTask("Activity_1mayx5z");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        
            Map<String, Object> variables = new HashMap<>();
            
            variables.put("Nombre", Map.of("value", solicitud.getEstudiante().getNombre(), "type", "String"));
            variables.put("Codigo", Map.of("value", solicitud.getEstudiante().getCodigo(), "type", "String"));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("variables", variables);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(camunda_url, requestEntity, Map.class);
            
        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public Response obtenerMaterias(Long solicitud_id)
    {
        SolicitudExterior solicitud = solicitudExteriorRepository.getOne(solicitud_id);
        try {
            String task_id = this.obtenerInstanciaDelProceso(solicitud.getProceso());
        
            String camunda_url = camundaUrl + "process-instance/" + task_id + "/variables/materias";

            Response response = restTemplate.getForObject(camunda_url, Response.class);
            
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
        return null;
    }
    
    public String obtenerInstanciaDelProceso(String proceso_id)
    {
        String camunda_url = camundaUrl + "process-instance?processDefinitionId=" + proceso_id;

        Map<String, Object>[] tasks = restTemplate.getForObject(camunda_url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
    
    public Estudiante obtenerEstudiante(Long solicitud_id)
    {
        SolicitudExterior solicitud = solicitudExteriorRepository.getOne(solicitud_id);
        
        Estudiante estudiante = solicitud.getEstudiante();
        
        return estudiante;
    }
    
    public void guardarCertificado(Long solicitud_id, String fileName) throws Exception
    {
        SolicitudExterior solicitud = solicitudExteriorRepository.getOne(solicitud_id);
        
        solicitud.setAnexo(fileName);
        solicitudExteriorRepository.save(solicitud);
        
        this.completarNotas();
    }
    
    public void completarNotas() {
        
        try {
            String task_id = this.obtenerCodigoTask("Activity_18b2ihj");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);
            
        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
}
