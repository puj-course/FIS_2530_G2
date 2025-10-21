package com.sis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LoginController {
    @GetMapping("/login")   //ESTABLECER URL Y METODO PARA EJECUTAR EL METODO (GET ES PARA MOSTRAR DATOS SEGUN HTTP
    public String loginPage(Model model){
        model.addAttribute("username","Juan");
        model.addAttribute("password","1234");
        return "login";
    }
    @GetMapping("/error")
    public String usuarioIncorrecto(){
        return "Usuario Incorrecto";
    }
}

