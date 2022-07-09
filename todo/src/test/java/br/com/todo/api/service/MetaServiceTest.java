package br.com.todo.api.service;

import br.com.todo.domain.repository.GoalRepository;
import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.service.goal.FinishGoalService;
import br.com.todo.domain.repository.TaskRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class MetaServiceTest {

    @MockBean
    GoalRepository metaRepository;

    @MockBean
    UserRepository usuarioRepository ;

    @MockBean
    TaskRepository tarefaRepository ;

    FinishGoalService metaService;

    /*
    @BeforeEach
    public void setUp(){
        metaService = new MetaService(usuarioRepository, metaRepository, tarefaRepository);

    }*/

    /*
    @Test
    @DisplayName("Deve salvar uma meta vinculada ao usuário e persistir as tarefas caso houver")
    public void salvarMetaTest() throws Exception {

        User usuario = new User("Pedro");
        usuario.setId(1L);

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,usuario,  Difficulty.MEDIUM);
        meta.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Goal metaSalva = metaService.saveGoal(Long.valueOf("1"), meta);

        Assertions.assertThat(metaSalva.getId()).isNotNull();
        Assertions.assertThat(metaSalva.getGoalTasks()).isNotNull();
        Assertions.assertThat(metaSalva.getUser()).isNotNull();
        Assertions.assertThat(metaSalva.getObjective()).isEqualTo("Aprender Microsserviço");
    }*/

    /*
    @Test
    @DisplayName("Deve lançar exception de usuário não presente e não deve persistir meta")
    public void naoDeveSalvarMetaTest(){


        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User(),  Difficulty.MEDIUM);
        meta.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            metaService.saveGoal(Long.valueOf("1"), meta);
        }catch (Exception ex){
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Usuário não encontrado".equals(ex.getMessage()));
            Assertions.assertThat(meta.getPoints()).isNull();
        }

    }*/

    /*
    @Test
    @DisplayName("Deve atualizar a meta com o objetivo do form")
    public void deveAtualizarMeta(){
        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);
        MetaFormAtualizacao form = new MetaFormAtualizacao("Desaprender microsserviço");

        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Goal metaAtualizada = metaService.atualizarMeta(meta, form);

        Assertions.assertThat(metaAtualizada.getObjective()).isEqualTo("Desaprender microsserviço");
        Assertions.assertThat(metaAtualizada.getId()).isNotNull();
        Assertions.assertThat(metaAtualizada.getGoalTasks()).isNotNull();
        Assertions.assertThat(metaAtualizada.getUser()).isNotNull();
    }*/

    /*
    @Test
    @DisplayName("Deve criar tarefa relacionada a meta")
    public void deveCriarTarefa(){

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);

        TarefaFormCadastro tarefaForm = new TarefaFormCadastro("Arquitetura hexagonal");

        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Goal metaSalva = metaService.criarTarefa(meta, tarefaForm);

        Assertions.assertThat(metaSalva.getGoalTasks()).isNotNull();
        Assertions.assertThat(metaSalva.getGoalTasks().get(0).getDescription())
                .isEqualTo("Arquitetura hexagonal");
    }*/

    /*
    @Test
    @DisplayName("Deve concluir a tarefa relacionada a meta")
    public void deveConcluirTarefa() throws TarefaNaoPresenteNaMetaException {

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);
        Task tarefa = new Task("Arquitetura hexagonal");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        Mockito.when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Goal metaSalva = metaService.concluirTarefa(meta, 2L);

        Assertions.assertThat(metaSalva.getGoalTasks()).isNotNull();
        Assertions.assertThat(metaSalva.getGoalTasks().get(0).isCompleted())
                .isTrue();

    }*/


    /*
    @Test
    @DisplayName("Deve Lançar exceção de Tarefa não presente na meta")
    public void naoDeveConcluirTarefa(){

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);

        try {
            metaService.concluirTarefa(meta, 2L);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getGoalTasks()).isEmpty();
        }
    }*/

    /*
    @Test
    @DisplayName("Deve atualizar Tarefa")
    public void deveAtualizarTarefa() throws TarefaNaoPresenteNaMetaException {

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);
        Task tarefa = new Task("Arquitetura hexagonal");
        tarefa.setId(2L);
        meta.addTask(tarefa);

        PostTaskRequest form = new PostTaskRequest("Arquitetura distribuida");

        Mockito.when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Goal metaSalva = metaService.atualizarTarefa(meta, 2L, form);

        Assertions.assertThat(metaSalva.getTasks()).isNotNull();
        Assertions.assertThat(metaSalva.getTasks().get(0).getDescription())
                .isEqualTo("Arquitetura distribuida");

    }

    @Test
    @DisplayName("Deve lançar TarefaNaoPresenteNaMetaException ")
    public void naoDeveAtualizarTarefa() {

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);

        PostTaskRequest form = new PostTaskRequest("Arquitetura distribuida");

        try {
            metaService.atualizarTarefa(meta, 2L, form);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getTasks()).isEmpty();
        }
    }

    @Test
    @DisplayName("Deve lançar TarefaNaoPresenteNaMetaException ")
    public void naoDeveExcluirTarefa() {

        Goal meta = new Goal("Aprender Microsserviço", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING,new User("Palio"),  Difficulty.MEDIUM);
        meta.setId(1L);

        try {
            metaService.deletarTarefa(meta, 2L);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(TarefaNaoPresenteNaMetaException.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getTasks()).isEmpty();
        }
    }


     */
}
