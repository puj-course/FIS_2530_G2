package com.sis.Controller;

import com.sis.Model.Enum.TipoUsuario;
import com.sis.Model.Paciente;
import com.sis.Model.Enfermera;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
public class PostLoginController {

    @GetMapping("/post-login")
    public String redirectByRole(Authentication auth) {

        if (auth == null) {
            return "redirect:/login";
        }

        String rol = auth.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("");

        switch (rol) {

            case "ROLE_ENFERMERA":
                return "redirect:/enfermera/dashboard";

            case "ROLE_DOCTOR":
                return "redirect:/doctor/dashboard";

            case "ROLE_PACIENTE":
                return "redirect:/paciente/dashboard";

            case "ROLE_ADMIN":
                return "redirect:/admin/dashboard";

            default:
                return "redirect:/login?error";
        }
    }
}
