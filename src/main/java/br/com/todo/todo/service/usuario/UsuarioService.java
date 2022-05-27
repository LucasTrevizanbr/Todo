package br.com.todo.todo.service.usuario;

import br.com.todo.todo.exceptions.SenhaInvalidaException;
import br.com.todo.todo.exceptions.UsuarioJaCadastradoException;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public Usuario cadastrarUsuario(Usuario usuario) {

        if(repository.findByEmail(usuario.getEmail()).isPresent()){
            throw new UsuarioJaCadastradoException("Esse email ja esta cadastrado");
        }

        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return repository.save(usuario);
    }

    public Usuario validarSenha(Usuario usuario, String senha) {

        if(encoder.matches(senha, usuario.getSenha())){
            return usuario;
        }else{
            throw new SenhaInvalidaException("Senha inválida");
        }
    }
}
