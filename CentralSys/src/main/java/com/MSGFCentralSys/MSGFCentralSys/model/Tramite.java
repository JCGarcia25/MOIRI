package com.MSGFCentralSys.MSGFCentralSys.model;

import javax.persistence.*;

/**
 *
 * @author jacke
 */
@Entity
public class Tramite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean solicitada;
    
    @Column(columnDefinition = "TEXT")
    private String anexo;
    
    // Relación ManyToOne con Estudiante (clave foránea)
    @ManyToOne
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    private Estudiante estudiante; // Esto se refiere al objeto Estudiante
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public boolean isSolicitada() {
        return solicitada;
    }

    public void setSolicitada(boolean solicitada) {
        this.solicitada = solicitada;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    
}
