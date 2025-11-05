package com.sis.Model;

import com.sis.Model.Enum.CargoDoc;
import jakarta.persistence.*;

@Entity
@Table(name = "doctores")
public class Doctor extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "especialidad")
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