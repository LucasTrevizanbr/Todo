package br.com.todo.infraestructure.security;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.InvalidPasswordException;
import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationLoginService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isEmpty()){
            throw new UsernameNotFoundException("Invalid Email");
        }
        return usuario.get();
    }

    public User validatePassword(User usuario, String senha) {
        if(!encoder.matches(senha, usuario.getPassword())){
            throw new InvalidPasswordException(ApiError.TG201.getMessageError(), ApiError.TG201.name());
        }
        return usuario;
    }
}
