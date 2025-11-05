package com.sis.Model;

import com.sis.Model.Enum.NivelEnfermera;

public class Enfermera extends Usuario{
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
