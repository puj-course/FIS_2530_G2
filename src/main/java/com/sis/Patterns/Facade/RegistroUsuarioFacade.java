package com.sis.Patterns.Facade;

import com.sis.Model.Usuario;
import com.sis.Model.Enum.TipoDoc;
import com.sis.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class RegistroUsuarioFacade {

    @Autowired
    private UserService userService;

    // Almacenar códigos de verificación temporalmente (en producción usar Redis)
    private Map<String, CodigoVerificacion> codigosVerificacion = new HashMap<>();
    private Random random = new Random();

    private static class CodigoVerificacion {
        String codigo;
        LocalDateTime expiracion;

        public CodigoVerificacion(String codigo, LocalDateTime expiracion) {
            this.codigo = codigo;
            this.expiracion = expiracion;
        }

        public boolean esValido() {
            return LocalDateTime.now().isBefore(expiracion);
        }
    }

    /**
     * Inicia el proceso de registro generando un código
     */
    public String iniciarRegistro(String telefono) throws Exception {
        if (telefono == null || telefono.isEmpty()) {
            throw new Exception("El teléfono es obligatorio");
        }

        // Generar código de verificación de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1000000));

        // Almacenar código con expiración de 5 minutos
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(5);
        codigosVerificacion.put(telefono, new CodigoVerificacion(codigo, expiracion));

        System.out.println("Código de verificación generado para " + telefono + ": " + codigo);

        return codigo;
    }

    /**
     * Confirma el registro después de validar el código
     */
    public Usuario confirmarRegistro(Usuario usuario, String codigoVerificacion) throws Exception {
        if (usuario == null) {
            throw new Exception("Usuario no puede ser nulo");
        }

        if (codigoVerificacion == null || codigoVerificacion.isEmpty()) {
            throw new Exception("El código de verificación es obligatorio");
        }

        // Validar código
        boolean codigoValido = validarCodigo(usuario.getTelefono(), codigoVerificacion);

        if (!codigoValido) {
            throw new Exception("Código de verificación inválido o expirado");
        }

        // Verificar si el usuario ya existe
        if (existeUsuario(usuario.getTipoDocumento(), usuario.getNumeroDocumento())) {
            throw new Exception("El documento ya está registrado");
        }

        // Crear usuario
        userService.addUsuario(usuario);

        return usuario;
    }

    /**
     * Verifica si un usuario existe por documento
     */
    public boolean existeUsuario(TipoDoc tipoDocumento, String numeroDocumento) {
        try {
            return userService.getUsuario().stream()
                    .anyMatch(u -> u.getTipoDocumento() == tipoDocumento &&
                            u.getNumeroDocumento().equals(numeroDocumento));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida un código de verificación
     */
    public boolean validarCodigo(String telefono, String codigo) {
        CodigoVerificacion codigoGuardado = codigosVerificacion.get(telefono);

        if (codigoGuardado == null) {
            return false;
        }

        if (!codigoGuardado.esValido()) {
            codigosVerificacion.remove(telefono);
            return false;
        }

        boolean valido = codigoGuardado.codigo.equals(codigo);

        if (valido) {
            codigosVerificacion.remove(telefono);
        }

        return valido;
    }

    /**
     * Reenvía un código de verificación
     */
    public String reenviarCodigo(String telefono) throws Exception {
        return iniciarRegistro(telefono);
    }
}