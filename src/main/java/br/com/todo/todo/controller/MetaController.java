package br.com.todo.todo.controller;

import br.com.todo.todo.dto.MetaDtoSimples;
import br.com.todo.todo.form.MetaFormCadastro;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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
}
