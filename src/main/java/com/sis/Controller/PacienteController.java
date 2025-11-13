package com.sis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @GetMapping("/dashboard")
    public String pacienteDashboard() {
        return "paciente/dashboard";
    }
}
