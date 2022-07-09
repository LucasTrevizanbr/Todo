package br.com.todo.api.controller;

import br.com.todo.application.controller.goal.request.PostGoalRequest;
import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.service.goal.FinishGoalService;
import br.com.todo.domain.repository.TaskRepository;
import br.com.todo.infraestructure.security.AuthenticationLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class MetaControllerTest {

    static String META_URI = "/api/metas";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GoalRepository metaRepository;

    @MockBean
    TaskRepository tarefaRepository;

    @MockBean
    AuthenticationLoginService autenticacaoService;

    @MockBean
    FinishGoalService metaService;

    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    /*

    @Test
    @DisplayName("Deverá devolver uma pagina default de metas DTO simples")
    public void buscarPaginaDeMeta() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Jorge"), Difficulty.EASY);
        meta.setId(id);

        Goal meta2 = new Goal("Kotlin numerics",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Jorge"), Difficulty.MEDIUM);
        meta2.setId(2L);

        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        List<Goal> metas = Arrays.asList(meta,meta2);
        Page<Goal> metasPaginadas = new PageImpl<>(metas);

        BDDMockito.given(metaRepository.findAllByUserId(anyLong(),any(PageRequest.class))).willReturn(metasPaginadas);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(META_URI +"/minhas/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("totalElements").value(2));
    }

     */


    /*
    @Test
    @DisplayName("Deve criar uma tarefa relacionada a Meta")
    public void criarTarefaTest() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.setId(id);
        Task tarefa = new Task("EcmaScript 6");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        String metaJson = objectMapper.writeValueAsString(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.criarTarefa(any(Goal.class), any(TarefaFormCadastro.class)))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save(any(Goal.class))).willReturn(meta);

        MockHttpServletRequestBuilder request = post(META_URI +"/"+id+"/tarefa")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }*/

    /*
    @Test
    @DisplayName("Deve atualizar uma tarefa relacionada a Meta e retornar o Json da Meta")
    public void atualizarTarefaTest() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.setId(id);
        Task tarefa = new Task("EcmaScript 6");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        String metaJson = objectMapper.writeValueAsString(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.atualizarTarefa(any(Goal.class),anyLong(), any(PostTaskRequest.class)))
                .willReturn(meta);
        BDDMockito.given(tarefaRepository.save(any(Task.class))).willReturn(tarefa);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI +"/"+id+"/atualizar/tarefa/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }*/

    /*
    @Test
    @DisplayName("Deve deletar uma tarefa relacionada a Meta e retornar o Json da Meta")
    public void deletarTarefaTest() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.setId(id);
        Task tarefa = new Task("EcmaScript 6");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.deletarTarefa(any(Goal.class), anyLong())).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(META_URI +"/"+id+"/deletar/tarefa/"+id);


        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }*/

    /*
    @Test
    @DisplayName("Deve concluir uma tarefa existente na Meta e retornar o Json da Meta")
    public void concluirTarefaTest() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.setId(id);
        Task tarefa = new Task("EcmaScript 6");
        tarefa.setId(id);
        meta.addTask(tarefa);

        BDDMockito.given(metaRepository.findById(anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.concluirTarefa(any(Goal.class), anyLong())).willReturn(meta);
        BDDMockito.given(tarefaRepository.save(any(Task.class))).willReturn(tarefa);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI +"/"+id+"/concluir/tarefa/"+id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("status").value("ANDAMENTO"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty());

    }*/


    /*
    @Test
    @DisplayName("Deve criar um resource de Meta e devolver seu Json")
    public void criarRecursoMetaTest() throws Exception {

        PostGoalRequest formMeta = new PostGoalRequest("Aprender Microsserviço", LocalDateTime.now(),
                Difficulty.MEDIUM, Arrays.asList("Aprender a base", "Base arquitetural"), 1l );

        String metaJson = objectMapper.writeValueAsString(formMeta);

        Goal metaSalva = formMeta.convertToGoalModel(formMeta);
        metaSalva.setId(1L);

        BDDMockito.given(metaRepository.save((Mockito.any(Goal.class)))).willReturn(metaSalva);
        BDDMockito.given(metaService.saveGoal((any(Long.class)), (any(Goal.class))))
                .willReturn(metaSalva);

        MockHttpServletRequestBuilder request = post(META_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("objetivo").value("Aprender Microsserviço"))
                .andExpect(jsonPath("prazoFinalEstipulado").isNotEmpty())
                .andExpect(jsonPath("status").value("ANDAMENTO"));

    }*/

    @Test
    @DisplayName("Deverá lançar erro de validação no formulário não adequado de Meta ")
    public void criarRecursoMetaComErro() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String metaJson = mapper.writeValueAsString(new PostGoalRequest());

        MockHttpServletRequestBuilder request = post(META_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(metaJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /*
    @Test
    @DisplayName("Deverá devolver o Json da Meta detalhada")
    public void buscarSingletonMeta() throws Exception {
        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.setId(id);

        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);

        meta.addTask(tarefa);

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
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.completeGoal((any(Goal.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Goal.class)))).willReturn(meta);

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
    @DisplayName("Deve retornar um Json de TarefaDto detalhado com as tarefas que não foram concluidas")
    public void naoDeveConcluirMetaTest() throws Exception {

        Long id = 1L;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.completeGoal((any(Goal.class))))
                .willThrow(new UnfinishedTasks(meta.getTasks()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/concluir/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("tarefasNaoConcluidas").isNotEmpty());
    }

    @Test
    @DisplayName("Deve deletar uma Meta e retornar no content")
    public void deletarMetaTest() throws Exception {

        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        meta.addTask(new Task("EcmaScript"));
        meta.setId(1L);

        BDDMockito.given(metaRepository.findById(Mockito.any(Long.class)))
                .willReturn(Optional.of(meta));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(META_URI + "/" + 1L);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

    }

     */

    /*
    @Test
    @DisplayName("Deve paralisar a meta retornar seu Json")
    public void deveParalisarMeta() throws Exception {

        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.STOPPED,
                new User("Laura"), Difficulty.EASY);
        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.paralisarMeta((any(Goal.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Goal.class)))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/paralisar/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("PARADA"));
    }*/

    /*
    @Test
    @DisplayName("Deve retomar a meta e retornar seu Json")
    public void deveRetomarMeta() throws Exception {

        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.STOPPED,
                new User("Laura"), Difficulty.EASY);
        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);
        meta.setId(id);

        BDDMockito.given(metaRepository.findById(id)).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.retomarMeta((any(Goal.class))))
                .willReturn(meta);
        BDDMockito.given(metaRepository.save((Mockito.any(Goal.class)))).willReturn(meta);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(META_URI + "/retomar/" +id)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("objetivo").value("Aprender JS"))
                .andExpect(jsonPath("historicoDatas").isNotEmpty())
                .andExpect(jsonPath("status").value("PARADA"));
    }*/

    /*
    @Test
    @DisplayName("Deve atualizar o objetivo da Meta e devolver um Json de DTO detalhado")
    public void atualizarObjetivoMeta() throws Exception {

        MetaFormAtualizacao formAtualizacao = new MetaFormAtualizacao("Aprender automação");

        Long id = 1l;
        Goal meta = new Goal("Aprender JS",new DatesHistory(LocalDateTime.now()), Status.ONGOING,
                new User("Laura"), Difficulty.EASY);
        Task tarefa = new Task("EcmaScript");
        tarefa.setId(2L);
        meta.addTask(tarefa);
        meta.setId(id);

        String metaJson = objectMapper.writeValueAsString(formAtualizacao);

        BDDMockito.given(metaRepository.findById(Mockito.anyLong())).willReturn(Optional.of(meta));
        BDDMockito.given(metaService.atualizarMeta(any(Goal.class), any(MetaFormAtualizacao.class))).willReturn(meta);
        BDDMockito.given(metaRepository.save(Mockito.any(Goal.class))).willReturn(meta);

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
    }*/

    /*
    private String devolverToken() throws Exception {
        UsuarioFormLogin login = new UsuarioFormLogin();
        login.setEmail("jorvaldo@jorvaldo.com");
        login.setSenha("123456789112");

        PostUserRequest usuarioForm = new PostUserRequest("Jorvaldo", "jojo", "jorvaldo@jorvaldo.com",
                "123456789112");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(login.getSenha());

        User usuario = usuarioForm.convertToUserModel();
        usuario.setId(1L);
        usuario.setPassword(senhaCriptografada);

        String loginJson = objectMapper.writeValueAsString(login);

        BDDMockito.given(autenticacaoService.loadUserByUsername(anyString())).willReturn(usuario);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/usuarios/logar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginJson);

        ResultActions result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("nome").value("Jorvaldo"))
                .andExpect(jsonPath("tokenDto").isNotEmpty());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String tokenDto = jsonParser.parseMap(resultString).get("tokenDto").toString();
        String[] a = tokenDto.split("=");
        String[] tokenMesmo = a[1].split(",");
        return tokenMesmo[0];
    }*/

}
