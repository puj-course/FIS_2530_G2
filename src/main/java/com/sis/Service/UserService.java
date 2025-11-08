package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UsuarioRepo usuarioRepo;

    public List<Usuario> getUsuario() {
        return usuarioRepo.findAll();
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepo.findById(id).orElse(new Usuario());
    }
    // importante diferenciar si id se refiere a documento o al UUID, dependiendo de cual sea, especificar!!!!

    public void addUsuario(Usuario usr) {
        usuarioRepo.save(usr);
    }

    public void updateUsuario(Usuario usr) {
        usuarioRepo.save(usr);


    }

    public void deleteUsuario(int usrId) {
        usuarioRepo.deleteById(usrId);

    }
}
