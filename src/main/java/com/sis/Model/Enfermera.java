package com.sis.Model;

import com.sis.Model.Enum.NivelEnfermera;
import jakarta.persistence.*;

@Entity
@Table(name = "enfermeras")
public class Enfermera extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel")
    private NivelEnfermera nivel;

    public Enfermera() {
        super();
    }

    public NivelEnfermera getNivel() {
        return nivel;
    }

    public void setNivel(NivelEnfermera nivel) {
        this.nivel = nivel;
    }
}