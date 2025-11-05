package com.sis.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends Usuario {

    public Admin() {
        super();
    }
}