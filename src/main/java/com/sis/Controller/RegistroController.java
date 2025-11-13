package com.sis.Controller;

import com.sis.DTO.UsuarioRegistroDTO;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Model.Enum.TipoUsuario;
import com.sis.Service.UsuarioRegistroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRegistroService registroService;

    /**
     * Muestra el formulario de registro
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        model.addAttribute("tiposDocumento", TipoDoc.values());
        model.addAttribute("tiposUsuario", TipoUsuario.values());
        return "registro";
    }

    /**
     * Procesa el formulario de registro
     */
    @PostMapping("/registro")
    public String registrarUsuario(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDTO usuarioDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validar errores de validación de Bean Validation
        if (result.hasErrors()) {
            model.addAttribute("tiposDocumento", TipoDoc.values());
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "registro";
        }

        // Validaciones de negocio (contraseñas, duplicados, etc.)
        Map<String, String> errores = registroService.validarRegistro(usuarioDTO);

        if (!errores.isEmpty()) {
            // Agregar errores al modelo
            errores.forEach((campo, mensaje) ->
                    model.addAttribute(campo + "Error", mensaje)
            );

            model.addAttribute("tiposDocumento", TipoDoc.values());
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "registro";
        }

        try {
            // Registrar usuario (con hash de contraseña)
            registroService.registrarUsuario(usuarioDTO);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje",
                    "Usuario registrado exitosamente. Ahora puedes iniciar sesión.");

            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error",
                    "Ocurrió un error al registrar el usuario. Por favor, intenta nuevamente.");
            model.addAttribute("tiposDocumento", TipoDoc.values());
            model.addAttribute("tiposUsuario", TipoUsuario.values());
            return "registro";
        }
    }
}