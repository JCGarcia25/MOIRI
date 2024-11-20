package com.msgfoundation.delegation;

import com.msgfoundation.annotations.BPMNTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@BPMNTask(type = "serviceTask", name = "Realizar equivalencia de notas")
public class RealizarEquivalencia implements JavaDelegate {

 
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Recupera el ID del estudiante del contexto del proceso
        System.out.println("Realizando equivalencia de notas.....");
        System.out.println("Equivalencia de notas Realizada con éxito!!!");
    }
}
