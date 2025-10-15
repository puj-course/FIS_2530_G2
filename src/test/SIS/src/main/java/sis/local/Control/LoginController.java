package sis.local.Control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Nombre de la vista (login.html)
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        // Aquí iría la lógica de autenticación (usualmente delegada a Spring Security)
        // Si falla, puedes agregar un mensaje de error al modelo
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }
}
