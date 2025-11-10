package com.sis.Model.Enum;

public enum TipoUsuario {
    ADMIN("Administrador"),
    DOCTOR("Doctor"),
    ENFERMERA("Enfermera"),
    PACIENTE("Paciente");

    private final String displayName;

    TipoUsuario(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
