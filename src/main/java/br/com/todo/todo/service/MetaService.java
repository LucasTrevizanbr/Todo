package br.com.todo.todo.service;

import br.com.todo.todo.exceptions.TarefasInacabadasException;
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

    @Autowired
    private ConclusaoMetaService conclusaoMetaService;

    public MetaService(UsuarioRepository usuarioRepository, MetaRepository metaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaRepository = metaRepository;
    }

    public Meta salvarMeta(MetaRepository metaRepository, Long idUsuario, Meta meta) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if(usuario.isEmpty()){
            throw new Exception("Usuário não encontrado");
        }
        else{
            meta.setUsuario(usuario.get());
            return metaRepository.save(meta);
        }
    }

    public Meta concluirMeta(Long idMeta) throws TarefasInacabadasException, Exception {
        Optional<Meta> meta = metaRepository.findById(idMeta);
        if(meta.isEmpty()){
            throw new Exception("Meta não encontrada");
        }else{
            Meta metaPresente = meta.get();
            conclusaoMetaService.validarTarefas(metaPresente);
            return metaRepository.save(metaPresente);
        }
    }



}
