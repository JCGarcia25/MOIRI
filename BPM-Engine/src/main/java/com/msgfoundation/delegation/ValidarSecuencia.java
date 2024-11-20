package com.msgfoundation.delegation;

import com.msgfoundation.annotations.BPMNGetterVariables;
import com.msgfoundation.annotations.BPMNSetterVariables;
import com.msgfoundation.annotations.BPMNTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
@BPMNTask(type = "serviceTask", name = "Validar la secuencia del informe a solicitar")
public class ValidarSecuencia implements JavaDelegate {

    private final JdbcTemplate jdbcTemplate;

    public ValidarSecuencia(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
 
    public Integer getterVariables(Long estudianteId) {
        System.out.println("buscando");
        String query = "SELECT mes_actual FROM estudiante WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{estudianteId}, Integer.class);
    }

    @BPMNSetterVariables( variables = { "informe"})
    public void setterVariables(DelegateExecution execution, Integer mesActual) {
        if (mesActual != null) {
            boolean informe = mesActual != 1; // true si mes_actual != 1, false en caso contrario
            execution.setVariable("informe", informe);
        } else {
            System.out.println("No se encontró mes_actual para el estudiante.");
            execution.setVariable("informe", false); // Valor predeterminado si no se encuentra el estudiante
        }
    }
 
    @Override
    @BPMNGetterVariables( variables = { "estudianteId" })
    public void execute(DelegateExecution execution) throws Exception {
        // Recupera el ID del estudiante del contexto del proceso
        Long estudianteId = (Long) execution.getVariable("estudianteId");
        System.out.println("estudianteId: " + estudianteId);
        if (estudianteId != null) {
            // Llama al método para obtener el mes actual
            Integer mesActual = getterVariables(estudianteId);

            // Establece las variables de salida en el proceso BPMN
            setterVariables(execution, mesActual);
        } else {
            System.out.println("La variable estudianteId no está definida o es nula.");
            execution.setVariable("informe", false); // Valor predeterminado si no se proporciona el ID
        }
    }
}
