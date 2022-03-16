package br.com.todo.todo.api.controller;

import br.com.todo.todo.dto.form.MetaFormAtualizacao;
import br.com.todo.todo.dto.form.MetaFormCadastro;
import br.com.todo.todo.dto.form.TarefaFormCadastro;
import br.com.todo.todo.exceptions.TarefasInacabadasException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.repository.TarefaRepository;
import br.com.todo.todo.service.MetaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MetaControllerTest {

    static String META_URI = "/api/metas";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MetaRepository metaRepository;

    @MockBean
    TarefaRepository tarefaRepository;

    @MockBean
    MetaService metaService;

    @Test
    @DisplayName("Deverá devolver uma pagina default de metas DTO simples")
    public void buscarPaginaDeMeta() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);

        Meta meta2 = new Meta("Kotlin numerics",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Jorge"),Dificuldade.MEDIO);
        meta2.setId(2L);

        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        List<Meta> metas = Arrays.asList(meta,meta2);
        Page<Meta> metasPaginadas = new PageImpl<>(metas);


        BDDMockito.given(metaRepository.findAll(any(PageRequest.class))).willReturn(metasPaginadas);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(META_URI)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("totalElements").value(2));
    }

    @Test
    @DisplayName("Deve criar uma tarefa relacionada a Meta")
    public void criarTarefaTest() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);
        Tarefa tarefa = new Tarefa("EcmaScript 6");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        ObjectMapper mapper = new ObjectMapper();
        String metaJson = mapper.writeValueAsString(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.criarTarefa(any(Meta.class), any(TarefaFormCadastro.class)))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save(any(Meta.class))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(META_URI +"/criar-tarefa/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }

    @Test
    @DisplayName("Deve atualizar uma tarefa relacionada a Meta")
    public void atualizarTarefaTest() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);
        Tarefa tarefa = new Tarefa("EcmaScript 6");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        ObjectMapper mapper = new ObjectMapper();
        String metaJson = mapper.writeValueAsString(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.atualizarTarefa(any(Meta.class),anyLong(), any(TarefaFormCadastro.class)))
                .willReturn(meta);
        BDDMockito.given(tarefaRepository.save(any(Tarefa.class))).willReturn(tarefa);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI +"/atualizar-tarefa/metaId="+id+"&tarefaId="+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }

    @Test
    @DisplayName("Deve deletar uma tarefa relacionada a Meta")
    public void deletarTarefaTest() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);
        Tarefa tarefa = new Tarefa("EcmaScript 6");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        ObjectMapper mapper = new ObjectMapper();
        String metaJson = mapper.writeValueAsString(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.deletarTarefa(any(Meta.class), anyLong())).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(META_URI +"/deletar-tarefa/metaId="+id +"&tarefaId="+id);


        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }

    @Test
    @DisplayName("Deve concluir uma tarefa existente na Meta")
    public void concluirTarefaTest() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);
        Tarefa tarefa = new Tarefa("EcmaScript 6");
        tarefa.setId(id);
        meta.adicionarTarefa(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.concluirTarefa(any(Meta.class), anyLong())).willReturn(meta);
        BDDMockito.given(tarefaRepository.save(any(Tarefa.class))).willReturn(tarefa);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI +"/concluir-tarefa/metaId="+id +"&tarefaId="+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty());

    }


    @Test
    @DisplayName("Deve criar um resource de Meta e devolver seu Json")
    public void criarRecursoMetaTest() throws Exception {

        MetaFormCadastro formMeta = new MetaFormCadastro("Aprender Microsserviço", LocalDateTime.now(),
                Dificuldade.MEDIO, Arrays.asList("Aprender a base", "Base arquitetural"), 1l );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String metaJson = mapper.writeValueAsString(formMeta);

        Meta metaSalva = formMeta.converterParaMetaEntidade(formMeta);
        metaSalva.setId(1L);

        BDDMockito.given(metaRepository.save((Mockito.any(Meta.class)))).willReturn(metaSalva);
        BDDMockito.given(metaService.salvarMeta((any(Long.class)), (any(Meta.class))))
                .willReturn(metaSalva);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(META_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender Microsserviço"))
                .andExpect(jsonPath("prazoFinalEstipulado").isNotEmpty())
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }

    @Test
    @DisplayName("Deverá lançar erro de validação no formulário não adequado de Meta ")
    public void criarRecursoMetaComErro() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String metaJson = mapper.writeValueAsString(new MetaFormCadastro());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(META_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Deverá devolver o Json da Meta detalhada")
    public void buscarSingletonMeta() throws Exception {
        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.setId(id);

        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);

        meta.adicionarTarefa(tarefa);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(META_URI + "/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("ANDAMENTO"))
                .andExpect(jsonPath("dificuldade").value("FACIL"))
                .andExpect(jsonPath("tarefasDaMeta").isNotEmpty())
                .andExpect(jsonPath("usuario").isNotEmpty());
    }

    @Test
    @DisplayName("Deverá retornar um not found ao não encontrar a Meta buscada")
    public void buscarSingletonMetaNaoExistente() throws Exception {

        BDDMockito.given(metaRepository.findById(any(Long.class))).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(META_URI + "/" +1L );

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar um DTO detalhado de Meta ao concluir ela")
    public void deveConcluirMetaTest() throws Exception {

        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.concluirMeta((any(Meta.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Meta.class)))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/concluir/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("ANDAMENTO"))
                .andExpect(jsonPath("dificuldade").value("FACIL"))
                .andExpect(jsonPath("tarefasDaMeta").isNotEmpty())
                .andExpect(jsonPath("usuario").isNotEmpty());

    }

    @Test
    @DisplayName("Deve retornar um Json de DTO detalhado de Meta apenas com as tarefas que não foram concluidas, resto null")
    public void naoDeveConcluirMetaTest() throws Exception {

        Long id = 1L;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.concluirMeta((any(Meta.class))))
                .willThrow(new TarefasInacabadasException(meta.getTarefasDaMeta()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/concluir/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("id").isEmpty())
                .andExpect(jsonPath("objetivo").isEmpty())
                .andExpect(jsonPath("historicoDatas").isEmpty())
                .andExpect(jsonPath("status").isEmpty())
                .andExpect(jsonPath("dificuldade").isEmpty())
                .andExpect(jsonPath("tarefasDaMeta").isNotEmpty())
                .andExpect(jsonPath("usuario").isEmpty());

    }

    @Test
    @DisplayName("Deve deletar uma Meta")
    public void deletarMetaTest() throws Exception {

        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        meta.adicionarTarefa(new Tarefa("EcmaScript"));
        meta.setId(1L);

        BDDMockito.given(metaRepository.findById(Mockito.any(Long.class)))
                .willReturn(Optional.of(meta));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(META_URI + "/" + 1L);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve paralisar a meta")
    public void deveParalisarMeta() throws Exception {

        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.PARADA,
                new Usuario("Laura"),Dificuldade.FACIL);
        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.paralisarMeta((any(Meta.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Meta.class)))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/paralisar/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("PARADA"));
    }

    @Test
    @DisplayName("Deve retomar a meta")
    public void deveRetomarMeta() throws Exception {

        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.PARADA,
                new Usuario("Laura"),Dificuldade.FACIL);
        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.retomarMeta((any(Meta.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Meta.class)))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/retomar/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("PARADA"));
    }

    @Test
    @DisplayName("Deve atualizar o objetivo da Meta e devolver um Json de DTO detalhado")
    public void atualizarObjetivoMeta() throws Exception {


        MetaFormAtualizacao formAtualizacao = new MetaFormAtualizacao("Aprender automação");

        Long id = 1l;
        Meta meta = new Meta("Aprender JS",new HistoricoDatas(LocalDateTime.now()), Status.ANDAMENTO,
                new Usuario("Laura"),Dificuldade.FACIL);
        Tarefa tarefa = new Tarefa("EcmaScript");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);
        meta.setId(id);

        ObjectMapper mapper = new ObjectMapper();

        String metaJson = mapper.writeValueAsString(formAtualizacao);

        BDDMockito.given(metaRepository.findById(Mockito.anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.atualizarMeta(any(Meta.class), any(MetaFormAtualizacao.class))).willReturn(meta);
        BDDMockito.given(metaRepository.save(Mockito.any(Meta.class))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("ANDAMENTO"));
    }


}
