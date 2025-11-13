package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UsuarioRepo usuarioRepo;

    public List<Usuario> getUsuario() {
        return usuarioRepo.findAll();
    }

    public Usuario getUsuarioById(UUID id) {
        return usuarioRepo.findById(id).orElse(new Usuario());
    }
    // importante diferenciar si id se refiere a documento o al UUID, dependiendo de cual sea, especificar!!!!

    public void addUsuario(Usuario usr) {
        usuarioRepo.save(usr);
    }

    public void updateUsuario(Usuario usr) {
        usuarioRepo.save(usr);


    }

    public void deleteUsuario(UUID usrId) {
        usuarioRepo.deleteById(usrId);

    }
}
