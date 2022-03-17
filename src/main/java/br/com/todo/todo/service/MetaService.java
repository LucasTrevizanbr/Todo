package br.com.todo.todo.service;

import br.com.todo.todo.dto.form.MetaFormAtualizacao;
import br.com.todo.todo.dto.form.TarefaFormCadastro;
import br.com.todo.todo.exceptions.TarefaNaoPresenteNaMetaException;
import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.repository.TarefaRepository;
import br.com.todo.todo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MetaService {

    private UsuarioRepository usuarioRepository;

    private MetaRepository metaRepository;


    private TarefaRepository tarefaRepository;

    @Autowired
    private ConclusaoMetaService conclusaoMetaService;

    @Autowired
    private ParalisacaoMetaService paralisacaoMetaService;

    public MetaService(UsuarioRepository usuarioRepository, MetaRepository metaRepository,
                       TarefaRepository tarefaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.metaRepository = metaRepository;
        this.tarefaRepository = tarefaRepository;
    }

    public Meta salvarMeta(Long idUsuario, Meta meta) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if(usuario.isEmpty()){
            throw new Exception("Usuário não encontrado");
        }
        else{
            meta.setUsuario(usuario.get());
            return metaRepository.save(meta);
        }
    }

    public Meta atualizarMeta(Meta meta, MetaFormAtualizacao form) {
        meta.setObjetivo(form.getObjetivo());
        return metaRepository.save(meta);
    }

    public void deletarMeta(Long id) {
        metaRepository.deleteById(id);
    }

    public Meta concluirMeta(Meta meta) throws TarefasInacabadasException {

        if (!conclusaoMetaService.validarTarefas(meta)) {
            throw new TarefasInacabadasException(meta.getTarefasDaMeta());
        } else {
            conclusaoMetaService.concluirMeta(meta);
        }
        return metaRepository.save(meta);
    }

    public Meta paralisarMeta(Meta meta) {
        paralisacaoMetaService.paralisar(meta);
        return metaRepository.save(meta);
    }

    public Meta retomarMeta(Meta meta) {
        paralisacaoMetaService.retomar(meta);
        return metaRepository.save(meta);
    }

    public Meta criarTarefa(Meta metaPresente, TarefaFormCadastro form) {
        Tarefa tarefa = form.converterParaEntidade();
        metaPresente.adicionarTarefa(tarefa);
        return metaRepository.save(metaPresente);
    }

    public Meta concluirTarefa(Meta metaPresente, Long idTarefa) throws TarefaNaoPresenteNaMetaException {

        Optional<Tarefa> tarefaMeta = metaPresente.getTarefasDaMeta()
                .stream()
                .filter(tarefa -> tarefa.getId() == idTarefa)
                .findFirst();
        if(tarefaMeta.isEmpty()){
            throw new TarefaNaoPresenteNaMetaException("Tarefa não presente na meta");
        }

        tarefaMeta.get().setConcluida(true);
        tarefaRepository.save(tarefaMeta.get());
        return metaPresente;
    }

    public Meta atualizarTarefa(Meta metaPresente, Long idTarefa, TarefaFormCadastro form)
            throws TarefaNaoPresenteNaMetaException {

        Optional<Tarefa> tarefaMeta = metaPresente.getTarefasDaMeta()
                .stream()
                .filter(tarefa -> tarefa.getId() == idTarefa)
                .findFirst();
        if(tarefaMeta.isEmpty()){
            throw new TarefaNaoPresenteNaMetaException("Tarefa não presente na meta");
        }

        tarefaMeta.get().setDescricao(form.getDescricao());
        tarefaRepository.save(tarefaMeta.get());

        return metaPresente;
    }

    public Meta deletarTarefa(Meta metaPresente, Long idTarefa) throws TarefaNaoPresenteNaMetaException {

        Optional<Tarefa> tarefaMeta = metaPresente.getTarefasDaMeta()
                .stream()
                .filter(tarefa -> tarefa.getId() == idTarefa)
                .findFirst();
        if(tarefaMeta.isEmpty()){
            throw new TarefaNaoPresenteNaMetaException("Tarefa não presente na meta");
        }

        tarefaMeta.get().setConcluida(true);
        tarefaRepository.deleteById(idTarefa);
        return metaPresente;
    }


}
