package br.com.todo.todo.service;

import br.com.todo.todo.form.MetaFormCadastro;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MetaService {

    private UsuarioRepository usuarioRepository;

    private MetaRepository metaRepository;

    public MetaService(UsuarioRepository usuarioRepository, MetaRepository metaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaRepository = metaRepository;
    }

    public Meta salvarMeta(MetaRepository metaRepository, Long idUsuario, Meta meta) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if(usuario.isEmpty()){
            throw new Exception("Não existe usuário para vincular a Meta");

        }else{
            meta.setUsuario(usuario.get());
            return metaRepository.save(meta);
        }
    }
}
