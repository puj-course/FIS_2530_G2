package com.sis.Controller;

import com.sis.Model.Usuario;
import com.sis.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

        @Autowired
        UserService service;

        @GetMapping("/Usuario")
        public List<Usuario> getUsuario() {
            return service.getUsuario();
        }

        @GetMapping("/Usuario/{id}")
        public Usuario getUsuarioById(@PathVariable UUID id) {
            return service.getUsuarioById(id);
        }


        @PostMapping("/Usuario")
        public void addUsuario(@RequestBody Usuario usr) {
            service.addUsuario(usr);
        }


        //update Usuario
        @PutMapping("/Usuario")
        public void updateUsuario(@RequestBody Usuario usr) {
            service.updateUsuario(usr);
        }

        //delete Usuario
        @DeleteMapping("/Usuario/{id}")
        public void deleteUsuario(@PathVariable UUID id) {
            service.deleteUsuario(id);
        }
}

