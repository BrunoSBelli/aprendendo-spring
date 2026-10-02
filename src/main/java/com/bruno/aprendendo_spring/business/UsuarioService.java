package com.bruno.aprendendo_spring.business;


import com.bruno.aprendendo_spring.infrastructure.entity.Usuario;
import com.bruno.aprendendo_spring.infrastructure.exceptions.ConflictException;
import com.bruno.aprendendo_spring.infrastructure.exceptions.ResourceNotFoundException;
import com.bruno.aprendendo_spring.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Gera somente construtor que usa campos com "private final", garantindo imutabilidade sem poder ser modificável
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public Usuario salvaUsuario(Usuario usuario) {
        try {
            emailExiste(usuario.getEmail());
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioRepository.save(usuario);

        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado"+ e.getCause());
        }

    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email já cadastrado"+ email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado"+ e.getCause());
        }
    }

    public  Usuario BuscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não encontrado!" + email));
    }

    public void deletaUsuarioPorEmail(String email){
         usuarioRepository.deleteByEmail(email);
    }


}
