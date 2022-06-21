package br.com.todo.dominio.servicos.usuario;

import br.com.todo.dominio.repositorios.UsuarioRepository;
import br.com.todo.dominio.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService  implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isEmpty()){
            throw new UsernameNotFoundException("Email invalido");
        }
        return usuario.get();
    }
}
