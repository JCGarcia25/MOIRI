package com.MSGFCentralSys.MSGFCentralSys.services;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import com.msgfoundation.annotations.BPMNTask;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.MSGFCentralSys.MSGFCentralSys.model.Solicitud;
import org.springframework.web.util.UriComponentsBuilder;
import com.MSGFCentralSys.MSGFCentralSys.model.Estudiante;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import com.MSGFCentralSys.MSGFCentralSys.repository.SolicitudRepository;
import com.MSGFCentralSys.MSGFCentralSys.repository.EstudianteRepository;

/**
 *
 * @author jacke
 */
@Service
@RequiredArgsConstructor
@BPMNTask(type = "userTask", name = "Validar la secuencia del informe a solicitar")
public class EstudianteService {
    
    @Value("${API_URL}")
    private String camundaUrl;

    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private SolicitudRepository solicitudRepository;
    
    private final RestTemplate restTemplate;
    
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }
    
    public String iniciarProceso(Long estudiante_id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        
            Map<String, Object> variables = new HashMap<>();
            
            variables.put("estudianteId", Map.of("value", estudiante_id, "type", "Long"));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("variables", variables);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                camundaUrl + "process-definition/key/MOIRI/start", 
                requestEntity,  // Si el proceso no requiere un cuerpo de solicitud adicional
                Map.class
            );
            
            // Obtener el ID del proceso desde la respuesta
            String processId = String.valueOf(response.getBody().get("id"));

            return processId; // Devolver el ID del proceso iniciado

        } catch (HttpClientErrorException e) {
            System.out.println(e);
            // En caso de error, devolver el mensaje de error
            return e.getResponseBodyAsString();
        }
    }

    public void actualizarProcesoEstudiante(Long estudiante_id, String proceso_id)
    {
        System.out.println("estudiante id: " + estudiante_id);
        Estudiante estudiante = estudianteRepository.getOne(estudiante_id);

        estudiante.setEstado("En proceso");
        estudiante.setProceso(proceso_id);
        estudianteRepository.save(estudiante);
    }
    
    public void generarSolicitudMensual(Long estudiante_id)
    {
        Estudiante estudiante = estudianteRepository.getOne(estudiante_id);
        System.out.println("cod estudiante: " + estudiante_id);
        Solicitud solicitud = new Solicitud();
        solicitud.setEstudiante(estudiante);
        solicitud.setTipo(1);
        System.out.println("estudiante solicitud: " + solicitud.getEstudiante().getNombre());
        solicitudRepository.save(solicitud);
        
        estudiante.setEstado("En Proceso Mensual");
        estudianteRepository.save(estudiante);

        this.solicitarInformeMensual(estudiante.getProceso());
    }
    
    public void generarSolicitudFinal(Long estudiante_id)
    {
        Estudiante estudiante = estudianteRepository.getOne(estudiante_id);

        Solicitud solicitud = new Solicitud();
        solicitud.setEstudiante(estudiante);
        solicitud.setTipo(2);
        solicitudRepository.save(solicitud);
        
        this.solicitarCertificadoFinal(estudiante.getProceso());
        this.solicitarInformeFinal(estudiante.getProceso());
    }
    
    public void solicitarInformeMensual(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTask(proceso_id, "Activity_0hvm4zr");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public void solicitarCertificadoFinal(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTaskFinal(proceso_id, "Activity_1ls71fd");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }
    
    public void solicitarInformeFinal(String proceso_id) {
        
        try {
            String task_id = this.obtenerCodigoTaskFinal(proceso_id, "Activity_1it53vu");
        
            String camunda_url = camundaUrl + "task/" + task_id + "/complete";

            restTemplate.postForEntity(camunda_url, new HashMap<>(), Map.class);

        } catch (HttpClientErrorException e) {
            System.err.println("Error during task completion: " + e.getMessage());
        }
    }

    public String obtenerCodigoTask(String proceso_id, String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
                .queryParam("executionId", proceso_id)
                .queryParam("taskDefinitionKey", key_task)
                .toUriString();
        System.out.println("url: " + camundaUrl);
        System.out.println("url completa: " + url);
        // Realizar la solicitud GET
        Map<String, Object>[] tasks = restTemplate.getForObject(url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            System.out.println("id tarea: " + tasks[0].get("id"));
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            System.out.println("id tarea nulo");
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
    
    public String obtenerCodigoTaskFinal(String proceso_id, String key_task) {
        // Construir la URL para la API de Camunda con los parámetros adecuados
        String url = UriComponentsBuilder.fromHttpUrl(camundaUrl + "task")
                .queryParam("processInstanceId", proceso_id)
                .queryParam("taskDefinitionKey", key_task)
                .toUriString();
        System.out.println("url: " + camundaUrl);
        System.out.println("url completa: " + url);
        // Realizar la solicitud GET
        Map<String, Object>[] tasks = restTemplate.getForObject(url, Map[].class);

        // Verificar si se encontró alguna tarea y devolver el ID de la primera tarea
        if (tasks != null && tasks.length > 0) {
            System.out.println("id tarea: " + tasks[0].get("id"));
            return (String) tasks[0].get("id"); // Extraer el ID de la tarea
        } else {
            System.out.println("id tarea nulo");
            return null; // Si no se encuentra ninguna tarea, devolver null
        }
    }
}
