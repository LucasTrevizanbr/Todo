package br.com.todo.api.service;

import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.model.Goal;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Difficulty;
import br.com.todo.domain.model.DatesHistory;
import br.com.todo.domain.model.enums.Status;
import org.assertj.core.api.Assertions;
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
public class AplicarPontoUsuarioServiceTest {

    @Autowired
    private AplicarPontoUsuarioService aplicarPontoUsuarioService;

    @MockBean
    UserRepository usuarioRepository;

    @Test
    @DisplayName("Deve conceder pontos da Meta ao usuario")
    public void aplicarPontosDaMetaConcluidaAoUsuarioTest(){
        User usuario = new User("Jorberto");
        usuario.setConclusionPointsGoal(11);
        usuario.setId(1L);
        Goal meta = new Goal("Aprender Kotlin", new DatesHistory(LocalDateTime.now()),
                Status.ONGOING, usuario, Difficulty.MEDIUM);
        meta.setPoints(22);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        aplicarPontoUsuarioService.aplicarPontosDaMetaConcluida(meta, usuarioRepository);

        Assertions.assertThat(usuario.getConclusionPointsGoal()).isEqualTo(33);

    }
}
