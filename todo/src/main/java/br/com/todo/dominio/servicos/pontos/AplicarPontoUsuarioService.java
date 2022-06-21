package br.com.todo.dominio.servicos.pontos;

import br.com.todo.dominio.repositorios.UsuarioRepository;
import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Usuario;
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
