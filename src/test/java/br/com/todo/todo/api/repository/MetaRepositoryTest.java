package br.com.todo.todo.api.repository;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Tarefa;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.repository.MetaRepository;
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
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class MetaRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MetaRepository metaRepository;

    private Meta meta;

    @BeforeEach
    public void setUp(){
        meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, new Usuario("Jorberto"), Dificuldade.MEDIO);
        meta.adicionarTarefa(new Tarefa("Aprender paradigma Funcional"));
        meta.adicionarTarefa(new Tarefa("Aprender scope functions"));

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
}
