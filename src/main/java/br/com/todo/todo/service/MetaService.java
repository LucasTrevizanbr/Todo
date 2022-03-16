package br.com.todo.todo.service;

import br.com.todo.todo.dto.form.MetaFormAtualizacao;
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

    @Autowired
    private ParalisacaoMetaService paralisacaoMetaService;

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

    public Meta concluirMeta(Meta meta) throws TarefasInacabadasException {

        if (!conclusaoMetaService.validarTarefas(meta)) {
            throw new TarefasInacabadasException(meta.getTarefasDaMeta());
        } else {
            conclusaoMetaService.concluirMeta(meta);
        }
        return metaRepository.save(meta);

    }

    public void deletarMeta(Long id) throws Exception{
        Optional<Meta> meta = metaRepository.findById(id);
        if(meta.isEmpty()){
            throw new Exception("Meta inexistente") ;
        }else{
            metaRepository.deleteById(id);
        }
    }

    public Meta paralisarMeta(Meta meta) {
        paralisacaoMetaService.paralisar(meta);
        return metaRepository.save(meta);
    }

    public Meta retomarMeta(Meta meta) {
        paralisacaoMetaService.retomar(meta);
        return metaRepository.save(meta);
    }

    public Meta atualizarMeta(Meta meta, MetaFormAtualizacao form) {
        meta.setObjetivo(form.getObjetivo());
        return metaRepository.save(meta);
    }
}
