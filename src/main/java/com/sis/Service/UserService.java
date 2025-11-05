package com.sis.Service;

import com.sis.Model.Usuario;
import com.sis.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public List<Usuario> getUsuario() {
        return userRepo.findAll();
    }

    public Usuario getUsuarioById(int id) {
        return userRepo.findById(id).orElse(new Usuario());
    }

    public void addUsuario(Usuario usr) {
        userRepo.save(usr);
    }

    public void updateUsuario(Usuario usr) {
        userRepo.save(usr);


    }

    public void deleteUsuario(int usrId) {
        userRepo.deleteById(usrId);

    }
}
