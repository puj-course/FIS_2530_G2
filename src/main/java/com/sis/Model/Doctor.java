package com.sis.Model;

import com.sis.Model.Enum.CargoDoc;

public class Doctor extends Usuario {
    private CargoDoc especialidad;

    public Doctor() {
        super();
    }

    public CargoDoc getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(CargoDoc especialidad) {
        this.especialidad = especialidad;
    }
}
