package br.com.todo.todo.service.pontos;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AplicarPontoUsuarioService {

    public void aplicarPontosDaMetaConcluida(Meta meta, UsuarioRepository usuarioRepository) {

        Optional<Usuario> usuario = usuarioRepository.findById(meta.getUsuario().getId());
        if(usuario.isPresent()){
            int pontosAplicaveis = usuario.get().getPontosConclusaoMetas() + meta.getPontos();
            usuario.get().setPontosConclusaoMetas(pontosAplicaveis);
            usuarioRepository.save(usuario.get());
        }
    }
}
