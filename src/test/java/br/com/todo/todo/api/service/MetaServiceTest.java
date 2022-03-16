package br.com.todo.todo.api.service;

import br.com.todo.todo.model.Meta;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.model.complemento.Dificuldade;
import br.com.todo.todo.model.complemento.HistoricoDatas;
import br.com.todo.todo.model.complemento.Status;
import br.com.todo.todo.repository.MetaRepository;
import br.com.todo.todo.repository.UsuarioRepository;
import br.com.todo.todo.service.MetaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MetaServiceTest {

    @MockBean
    MetaRepository metaRepository;

    @MockBean
    UsuarioRepository usuarioRepository ;

    MetaService metaService;

    @BeforeEach
    public void setUp(){
        metaService = new MetaService(usuarioRepository, metaRepository);

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
}
