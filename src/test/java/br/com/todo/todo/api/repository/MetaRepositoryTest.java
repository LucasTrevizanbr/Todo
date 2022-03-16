package br.com.todo.todo.api.repository;

import br.com.todo.todo.repository.MetaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MetaRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    MetaRepository metaRepository;

    @Test
    public void deveRetornarTrueSeMetaExistir(){


    }
}
