package br.com.todo.todo.api.service;

import br.com.todo.todo.dto.form.MetaFormAtualizacao;
import br.com.todo.todo.dto.form.MetaFormCadastro;
import br.com.todo.todo.dto.form.TarefaFormCadastro;
import br.com.todo.todo.exceptions.TarefaNaoPresenteNaMetaException;
import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.repository.TarefaRepository;
import br.com.todo.todo.repository.UsuarioRepository;
import br.com.todo.todo.service.MetaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class MetaServiceTest {

    @MockBean
    MetaRepository metaRepository;

    @MockBean
    UsuarioRepository usuarioRepository ;

    @MockBean
    TarefaRepository tarefaRepository ;

    MetaService metaService;

    @BeforeEach
    public void setUp(){
        metaService = new MetaService(usuarioRepository, metaRepository, tarefaRepository);

    }

    @Test
    @DisplayName("Deve salvar uma meta vinculada ao usuário e persistir as tarefas caso houver")
    public void salvarMetaTest() throws Exception {

        Usuario usuario = new Usuario("Pedro");
        usuario.setId(1L);

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,usuario,  Dificuldade.MEDIO);
        meta.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Meta metaSalva = metaService.salvarMeta(Long.valueOf("1"), meta);

        Assertions.assertThat(metaSalva.getId()).isNotNull();
        Assertions.assertThat(metaSalva.getTarefasDaMeta()).isNotNull();
        Assertions.assertThat(metaSalva.getUsuario()).isNotNull();
        Assertions.assertThat(metaSalva.getObjetivo()).isEqualTo("Aprender Microsserviço");
    }

    @Test
    @DisplayName("Deve lançar exception de usuário não presente e não deve persistir meta")
    public void naoDeveSalvarMetaTest(){


        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario(),  Dificuldade.MEDIO);
        meta.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            metaService.salvarMeta(Long.valueOf("1"), meta);
        }catch (Exception ex){
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Usuário não encontrado".equals(ex.getMessage()));
            Assertions.assertThat(meta.getPontos()).isNull();
        }

    }

    @Test
    @DisplayName("Deve atualizar a meta com o objetivo do form")
    public void deveAtualizarMeta(){
        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);
        MetaFormAtualizacao form = new MetaFormAtualizacao("Desaprender microsserviço");

        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Meta metaAtualizada = metaService.atualizarMeta(meta, form);

        Assertions.assertThat(metaAtualizada.getObjetivo()).isEqualTo("Desaprender microsserviço");
        Assertions.assertThat(metaAtualizada.getId()).isNotNull();
        Assertions.assertThat(metaAtualizada.getTarefasDaMeta()).isNotNull();
        Assertions.assertThat(metaAtualizada.getUsuario()).isNotNull();
    }

    @Test
    @DisplayName("Deve criar tarefa relacionada a meta")
    public void deveCriarTarefa(){

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);

        TarefaFormCadastro tarefaForm = new TarefaFormCadastro("Arquitetura hexagonal");

        Mockito.when(metaRepository.save(meta)).thenReturn(meta);

        Meta metaSalva = metaService.criarTarefa(meta, tarefaForm);

        Assertions.assertThat(metaSalva.getTarefasDaMeta()).isNotNull();
        Assertions.assertThat(metaSalva.getTarefasDaMeta().get(0).getDescricao())
                .isEqualTo("Arquitetura hexagonal");
    }

    @Test
    @DisplayName("Deve concluir a tarefa relacionada a meta")
    public void deveConcluirTarefa() throws TarefaNaoPresenteNaMetaException {

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);
        Tarefa tarefa = new Tarefa("Arquitetura hexagonal");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        Mockito.when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Meta metaSalva = metaService.concluirTarefa(meta, 2L);

        Assertions.assertThat(metaSalva.getTarefasDaMeta()).isNotNull();
        Assertions.assertThat(metaSalva.getTarefasDaMeta().get(0).isConcluida())
                .isTrue();

    }


    @Test
    @DisplayName("Deve Lançar exceção de Tarefa não presente na meta")
    public void naoDeveConcluirTarefa(){

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);

        try {
            metaService.concluirTarefa(meta, 2L);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getTarefasDaMeta()).isEmpty();
        }
    }

    @Test
    @DisplayName("Deve atualizar Tarefa")
    public void deveAtualizarTarefa() throws TarefaNaoPresenteNaMetaException {

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);
        Tarefa tarefa = new Tarefa("Arquitetura hexagonal");
        tarefa.setId(2L);
        meta.adicionarTarefa(tarefa);

        TarefaFormCadastro form = new TarefaFormCadastro("Arquitetura distribuida");

        Mockito.when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Meta metaSalva = metaService.atualizarTarefa(meta, 2L, form);

        Assertions.assertThat(metaSalva.getTarefasDaMeta()).isNotNull();
        Assertions.assertThat(metaSalva.getTarefasDaMeta().get(0).getDescricao())
                .isEqualTo("Arquitetura distribuida");

    }

    @Test
    @DisplayName("Deve lançar TarefaNaoPresenteNaMetaException ")
    public void naoDeveAtualizarTarefa() {

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);

        TarefaFormCadastro form = new TarefaFormCadastro("Arquitetura distribuida");

        try {
            metaService.atualizarTarefa(meta, 2L, form);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getTarefasDaMeta()).isEmpty();
        }
    }

    @Test
    @DisplayName("Deve lançar TarefaNaoPresenteNaMetaException ")
    public void naoDeveExcluirTarefa() {

        Meta meta = new Meta("Aprender Microsserviço", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO,new Usuario("Palio"),  Dificuldade.MEDIO);
        meta.setId(1L);

        try {
            metaService.deletarTarefa(meta, 2L);
        } catch (TarefaNaoPresenteNaMetaException e) {
            Assertions.assertThatExceptionOfType(Exception.class);
            Assertions.assertThat( "Tarefa não presente na meta".equals(e.getMessage()));
            Assertions.assertThat(meta.getTarefasDaMeta()).isEmpty();
        }
    }
}
