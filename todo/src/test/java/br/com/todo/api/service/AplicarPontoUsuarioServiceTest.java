package br.com.todo.api.service;

import br.com.todo.dominio.repositorios.UsuarioRepository;
import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.modelos.Usuario;
import br.com.todo.dominio.modelos.Dificuldade;
import br.com.todo.dominio.modelos.HistoricoDatas;
import br.com.todo.dominio.modelos.Status;
import br.com.todo.dominio.servicos.pontos.AplicarPontoUsuarioService;
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
    UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve conceder pontos da Meta ao usuario")
    public void aplicarPontosDaMetaConcluidaAoUsuarioTest(){
        Usuario usuario = new Usuario("Jorberto");
        usuario.setPontosConclusaoMetas(11);
        usuario.setId(1L);
        Meta meta = new Meta("Aprender Kotlin", new HistoricoDatas(LocalDateTime.now()),
                Status.ANDAMENTO, usuario, Dificuldade.MEDIO);
        meta.setPontos(22);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        aplicarPontoUsuarioService.aplicarPontosDaMetaConcluida(meta, usuarioRepository);

        Assertions.assertThat(usuario.getPontosConclusaoMetas()).isEqualTo(33);

    }
}
