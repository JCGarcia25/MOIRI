/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MSGFCentralSys.MSGFCentralSys.services;

import com.MSGFCentralSys.MSGFCentralSys.model.Anexo;
import com.MSGFCentralSys.MSGFCentralSys.model.Solicitud;
import com.MSGFCentralSys.MSGFCentralSys.model.Tramite;
import com.MSGFCentralSys.MSGFCentralSys.repository.SolicitudRepository;
import com.MSGFCentralSys.MSGFCentralSys.repository.TramiteRepository;
import com.msgfoundation.annotations.BPMNTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

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
    private TramiteRepository tramiteRepository;
    
    private final RestTemplate restTemplate;

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }
    
    public List<Tramite> listarTramites() {
        return tramiteRepository.findAll();
    }
    
    public void solicitarEquivalencia(Long tramite_id) {
        
        try {
            Tramite tramite = tramiteRepository.getOne(tramite_id);
            
            String task_id = this.obtenerCodigoTaskEnviarInformeFinal(tramite.getEstudiante().getProceso(), "Activity_01ct8n4");
        
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
    
    public List<Anexo> obtenerAnexos(Long solicitud_id)
    {
        Solicitud solicitud = solicitudRepository.getOne(solicitud_id);
        
        return solicitud.getAnexos();
    }
}
