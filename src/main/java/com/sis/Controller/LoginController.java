package com.sis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
    @GetMapping("/login")   //ESTABLECER URL Y METODO PARA EJECUTAR EL METODO (GET ES PARA MOSTRAR DATOS SEGUN HTTP
    public String loginPage(){
        return "login";
    }
}