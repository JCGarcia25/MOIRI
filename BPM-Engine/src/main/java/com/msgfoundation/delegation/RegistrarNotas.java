/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.msgfoundation.delegation;

import com.msgfoundation.annotations.BPMNTask;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

/**
 *
 * @author jacke
 */
@Service
@BPMNTask(type = "serviceTask", name = "Registrar notas en la plataforma SIAU")
public class RegistrarNotas implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Recupera el ID del estudiante del contexto del proceso
        System.out.println("Registrando Notas.....");
        System.out.println("Notas Registradas con éxito!!!");
    }
}