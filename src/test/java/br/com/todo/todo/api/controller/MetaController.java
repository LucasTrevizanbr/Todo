package br.com.todo.todo.api.controller;

import br.com.todo.todo.dto.form.MetaFormCadastro;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.service.MetaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MetaController {

    static String META_URI = "/api/metas";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MetaRepository metaRepository;

    @MockBean
    MetaService metaService;

    @Test
    @DisplayName("Deve criar um resource de Meta e devolver status 201")
    public void criarRecursoMetaTest() throws Exception {

        MetaFormCadastro formMeta = new MetaFormCadastro("Aprender Microsserviço",
                LocalDateTime.of(LocalDate.now(), LocalTime.now()),
                Dificuldade.MEDIO, Arrays.asList("Aprender a base", "Base arquitetural"), 1l );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String metaJson = mapper.writeValueAsString(formMeta);

        Meta metaSalva = formMeta.converterParaMetaEntidade(formMeta);
        metaSalva.setId(1L);

        BDDMockito.given(metaRepository.save((Mockito.any(Meta.class)))).willReturn(metaSalva);
        BDDMockito.given(metaService.salvarMeta(any(MetaRepository.class),(any(Long.class)), (any(Meta.class))))
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
    @DisplayName("Deverá devolver o Json da meta detalhada concluida")
    public void deveConcluirMetaTest() throws Exception {

    }

    @Test
    @DisplayName("Deve lançar exceção de tarefas não concluidas da meta")
    public void deveFalharAoConcluirMetaTest(){

    }


}
