package br.com.todo.todo.api.repository;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.repository.MetaRepository;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MetaRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MetaRepository metaRepository;

    private Meta meta;

    private Usuario user;

    @BeforeEach
    public void setUp(){
        LocalDateTime dataFinal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        user = new Usuario("Jorberto");
        user.setEmail("jorgeBlablu@hotmail.com");
        user.setId(1L);
        meta = new Meta("Aprender Kotlin", new HistoricoDatas(dataFinal),
                Status.ANDAMENTO, user, Dificuldade.MEDIO);
        meta.adicionarTarefa(new Tarefa("Aprender paradigma Funcional"));
        meta.adicionarTarefa(new Tarefa("Aprender scope functions"));

        testEntityManager.persist(user);
        testEntityManager.persist(meta);

    }

    @Test
    @DisplayName("Deve retornar uma Meta pelo seu ID")
    public void deveRetornarMeta(){

        Optional<Meta> metaBuscada = metaRepository.findById(meta.getId());
        Assertions.assertThat(metaBuscada.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar a meta com os novos valores ")
    public void deveSalvarMeta(){
        meta.setObjetivo("Gerenciar Agência");

        Meta metaAtualizada = metaRepository.save(meta);

        Assertions.assertThat(metaAtualizada.getObjetivo()).isEqualTo("Gerenciar Agência");
    }
    
    @Test
    @DisplayName("Deve deletar uma meta pelo id")
    public void deveDeletarUmaMeta(){

        metaRepository.deleteById(meta.getId());
        Optional<Meta> metaAtualizada = metaRepository.findById(meta.getId());

        Assertions.assertThat(metaAtualizada.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar metas que estão no dia final do prazo e que não estão concluidas")
    public void deveBuscarMetasNoDiaFinal(){

        LocalDateTime dataFinal = LocalDateTime.of(2022, 4, 3, 12,10,00);
        LocalDateTime inicioDia = dataFinal.toLocalDate().atStartOfDay();
        LocalDateTime fimDoDia = dataFinal.with(LocalTime.of(23,59,59));

        List<Meta> metasPrazoFinal = metaRepository.listarMetasNoDiaFinal(inicioDia,fimDoDia);

        Assertions.assertThat(metasPrazoFinal).hasSize(1).contains(meta);
    }
}
