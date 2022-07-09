package br.com.todo.api.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ConclusaoMetaServiceTest {


    /*
    @Test
    @DisplayName("Deve concluir com sucesso a meta")
    public void concluirMetaTest() throws UnfinishedTasks {

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);
        meta.setPoints(2);

        conclusaoMetaService.concluirMeta(meta);

        Assertions.assertThat(meta.getStatus()).isEqualTo(Status.FINISHED);
        Assertions.assertThat(meta.getDateHistory().getRealFinalizationDate()).isNotNull();

    }

    @Test
    @DisplayName("Deve lançar exceção com tarefas inacabadas da Meta")
    public void naoDeveConcluirMetaTest() {

        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, new User("Jorberto"), Difficulty.MEDIUM);
        meta.addTask(new Task("Aprender paradigma Funcional"));
        meta.addTask(new Task("Aprender scope functions"));

        try {
            conclusaoMetaService.isTasksCompleted(meta);
        } catch (UnfinishedTasks e) {
            Assertions.assertThatExceptionOfType(UnfinishedTasks.class);
            Assertions.assertThat( "Tarefas ainda não concluidas: [Aprender paradigma Funcional," +
                    " Aprender scope functions]".equals(e.getMessage()));
            Assertions.assertThat(meta.getDateHistory().getRealFinalizationDate()).isNull();
            Assertions.assertThat(meta.getStatus()).isNotEqualTo(Status.FINISHED);
        }

    }

    */


}
