package br.com.todo.todo.controller;

import br.com.todo.todo.dto.MetaDtoDetalhado;
import br.com.todo.todo.dto.MetaDtoSimples;
import br.com.todo.todo.dto.TarefaDtoDetalhado;
import br.com.todo.todo.dto.form.MetaFormAtualizacao;
import br.com.todo.todo.dto.form.MetaFormCadastro;
import br.com.todo.todo.dto.form.TarefaFormCadastro;
import br.com.todo.todo.exceptions.TarefaNaoPresenteNaMetaException;
import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private MetaService metaService;

    @GetMapping
    public ResponseEntity<Page<MetaDtoSimples>> buscarMetasPaginadas(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC,page = 0, size = 10) Pageable paginacao){
        Page<Meta> metas = metaRepository.findAll(paginacao);
        Page<MetaDtoSimples> metasDto = metas.map(MetaDtoSimples::new);
        return ResponseEntity.ok(metasDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaDtoDetalhado> buscarMeta(@PathVariable Long id) {
        Optional<Meta> meta = metaRepository.findById(id);
        if (meta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MetaDtoDetalhado(meta.get()));
    }

    @PostMapping
    public ResponseEntity<MetaDtoSimples> criarMeta(@RequestBody @Valid MetaFormCadastro metaForm,
                                                    @Autowired UriComponentsBuilder uriBuilder) {

        try {
            Meta meta = metaForm.converterParaMetaEntidade(metaForm);
            meta = metaService.salvarMeta(metaForm.getIdUsuario(), meta);
            MetaDtoSimples metaDtoSimples = new MetaDtoSimples(meta);

            URI uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(metaDtoSimples.getId()).toUri();
            return ResponseEntity.created(uri).body(metaDtoSimples);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/criar-tarefa/{id}")
    public ResponseEntity<MetaDtoDetalhado> criarTarefa(@PathVariable Long id,
                                                        @RequestBody @Valid TarefaFormCadastro form){
        Optional<Meta> meta = metaRepository.findById(id);
        if(meta.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Meta metaPresente = meta.get();
        metaPresente = metaService.criarTarefa(metaPresente, form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MetaDtoDetalhado(metaPresente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaDtoDetalhado> atualizarMeta(@PathVariable Long id,
                                                          @RequestBody @Valid MetaFormAtualizacao form){
        Optional<Meta> meta = metaRepository.findById(id);
        if(meta.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Meta metaPresente = meta.get();
        metaPresente = metaService.atualizarMeta(metaPresente, form);
        return ResponseEntity.ok(new MetaDtoDetalhado(metaPresente));
    }

    @PutMapping("/concluir/{id}")
    public ResponseEntity<MetaDtoDetalhado> concluirMeta(@PathVariable Long id) {
        try {
            Optional<Meta> meta = metaRepository.findById(id);
            if (meta.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Meta metaConcluida = metaService.concluirMeta(meta.get());
            MetaDtoDetalhado dtoMetaDetalhada = new MetaDtoDetalhado(metaConcluida);
            return ResponseEntity.ok().body(dtoMetaDetalhada);

        } catch (TarefasInacabadasException ex) {

            List<TarefaDtoDetalhado> dtoTarefasNaoConcluidas = ex.getTarefasNaoConcluidas();
            MetaDtoDetalhado metaDtoErro = new MetaDtoDetalhado();
            metaDtoErro.setTarefasDaMeta(dtoTarefasNaoConcluidas);

            return ResponseEntity.badRequest().body(metaDtoErro);
        }
    }

    @PutMapping("/concluir-tarefa/metaId={idMeta}&tarefaId={idTarefa}")
    public ResponseEntity<MetaDtoDetalhado> concluirTarefa(@PathVariable Long idMeta,
                                                           @PathVariable Long idTarefa){
        try{
            Optional<Meta> meta = metaRepository.findById(idMeta);
            if(meta.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            Meta metaPresente = meta.get();
            metaPresente = metaService.concluirTarefa(metaPresente, idTarefa);
            return ResponseEntity.ok(new MetaDtoDetalhado(metaPresente));

        }catch (TarefaNaoPresenteNaMetaException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atualizar-tarefa/metaId={idMeta}&tarefaId={idTarefa}")
    public ResponseEntity<MetaDtoDetalhado> atualizarTarefa(@PathVariable Long idMeta,
                                                            @PathVariable Long idTarefa,
                                                            @RequestBody @Valid TarefaFormCadastro form){
        try{
            Optional<Meta> meta = metaRepository.findById(idMeta);
            if(meta.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            Meta metaPresente = meta.get();
            metaPresente = metaService.atualizarTarefa(metaPresente, idTarefa, form);
            return ResponseEntity.ok(new MetaDtoDetalhado(metaPresente));

        }catch (TarefaNaoPresenteNaMetaException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/paralisar/{id}")
    public ResponseEntity<MetaDtoDetalhado> paralisarMeta(@PathVariable Long id) {
        Optional<Meta> meta = metaRepository.findById(id);
        if (meta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Meta metaParalisada = metaService.paralisarMeta(meta.get());
        MetaDtoDetalhado dtoMetaDetalhada = new MetaDtoDetalhado(metaParalisada);
        return ResponseEntity.ok().body(dtoMetaDetalhada);
    }

    @PutMapping("/retomar/{id}")
    public ResponseEntity<MetaDtoDetalhado> retomarMeta(@PathVariable Long id) {
        Optional<Meta> meta = metaRepository.findById(id);
        if (meta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Meta metaParalisada = metaService.retomarMeta(meta.get());
        MetaDtoDetalhado dtoMetaDetalhada = new MetaDtoDetalhado(metaParalisada);
        return ResponseEntity.ok().body(dtoMetaDetalhada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarMeta(@PathVariable Long id) {

            Optional<Meta> meta = metaRepository.findById(id);
            if(meta.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            metaService.deletarMeta(id);
            return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar-tarefa/metaId={idMeta}&tarefaId={idTarefa}")
    public ResponseEntity<MetaDtoDetalhado> excluirTarefa(@PathVariable Long idMeta,
                                                          @PathVariable Long idTarefa){
        try{
            Optional<Meta> meta = metaRepository.findById(idMeta);
            if(meta.isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            Meta metaPresente = meta.get();
            metaPresente = metaService.deletarTarefa(metaPresente, idTarefa);
            return ResponseEntity.ok(new MetaDtoDetalhado(metaPresente));

        }catch (TarefaNaoPresenteNaMetaException ex){
            return ResponseEntity.badRequest().build();
        }
    }


}
