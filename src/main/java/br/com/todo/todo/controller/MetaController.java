package br.com.todo.todo.controller;

import br.com.todo.todo.dto.MetaDtoDetalhado;
import br.com.todo.todo.dto.MetaDtoSimples;
import br.com.todo.todo.dto.TarefaDtoDetalhado;
import br.com.todo.todo.dto.form.MetaFormCadastro;
import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private MetaService metaService;

    @PostMapping
    public ResponseEntity<MetaDtoSimples> criarMeta(@RequestBody @Valid MetaFormCadastro metaForm,
                                                    @Autowired UriComponentsBuilder uriBuilder){

        try{
            Meta meta = metaForm.converterParaMetaEntidade(metaForm);
            meta = metaService.salvarMeta(metaRepository, metaForm.getIdUsuario(), meta);
            MetaDtoSimples metaDtoSimples = new MetaDtoSimples(meta);

            URI uri = uriBuilder.path("/api/metas/{id}").buildAndExpand(metaDtoSimples.getId()).toUri();
            return ResponseEntity.created(uri).body(metaDtoSimples);
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/concluir/{id}")
    private ResponseEntity<MetaDtoDetalhado> concluirMeta(@PathVariable @Valid @NotNull Long id){
        try{
            Meta meta = metaService.concluirMeta(id);
            MetaDtoDetalhado dtoMetaDetalhada = new MetaDtoDetalhado(meta);
            return ResponseEntity.ok().body(dtoMetaDetalhada);

        }catch (TarefasInacabadasException ex){

            List<TarefaDtoDetalhado> dtoTarefasNaoConcluidas = ex.getTarefasNaoConcluidas();
            MetaDtoDetalhado metaDtoErro = new MetaDtoDetalhado();
            metaDtoErro.setTarefasDaMeta(dtoTarefasNaoConcluidas);

            return ResponseEntity.badRequest().body(metaDtoErro);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
