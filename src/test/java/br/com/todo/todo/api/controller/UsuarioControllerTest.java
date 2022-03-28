package br.com.todo.todo.api.controller;

import br.com.todo.todo.dto.form.UsuarioForm;
import br.com.todo.todo.dto.form.UsuarioFormLogin;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.UsuarioRepository;
import br.com.todo.todo.service.usuario.AutenticacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsuarioControllerTest {

    static String USUARIO_URI = "/api/usuarios";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsuarioRepository usuarioRepository;

    @MockBean
    AutenticacaoService autenticacaoService;

    private ObjectMapper objectMapper;

    private UsuarioForm usuarioForm;

    @BeforeEach
    public void setUp(){

        usuarioForm = new UsuarioForm("Jorvaldo", "jojo", "jorvaldo@jorvaldo.com",
                "123456789112");

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso e devolver")
    public void deveCadastrarUsuario() throws Exception {

        Usuario usuario = usuarioForm.converterParaEntidade();
        usuario.setId(1L);

        String usuarioFormJson = objectMapper.writeValueAsString(usuarioForm);

        BDDMockito.given(usuarioRepository.findByEmail(anyString())).willReturn(Optional.empty());
        BDDMockito.given(usuarioRepository.save(any(Usuario.class))).willReturn(usuario);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USUARIO_URI +"/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(usuarioFormJson);


        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value("Jorvaldo"))
                .andExpect(MockMvcResultMatchers.jsonPath("apelido").value("jojo"))
                .andExpect(jsonPath("email").value("jorvaldo@jorvaldo.com"));
    }

    @Test
    @DisplayName("Deve devolver exceção de usuario ja cadastrado")
    public void naoDeveCadastrarUsuario() throws Exception {

        Usuario usuario = usuarioForm.converterParaEntidade();
        usuario.setId(1L);

        String usuarioFormJson = objectMapper.writeValueAsString(usuarioForm);

        BDDMockito.given(usuarioRepository.findByEmail(anyString())).willReturn(Optional.of(usuario));
        BDDMockito.given(usuarioRepository.save(any(Usuario.class))).willReturn(usuario);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USUARIO_URI +"/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(usuarioFormJson);


        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("erro").value("Esse email ja esta cadastrado"));
    }

    @Test
    @DisplayName("Deve logar o usuario com sucesso e devolver o token")
    public void deveLogar() throws Exception {
        UsuarioFormLogin login = new UsuarioFormLogin();
        login.setEmail("jorvaldo@jorvaldo.com");
        login.setSenha("123456789112");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(login.getSenha());

        Usuario usuario = usuarioForm.converterParaEntidade();
        usuario.setId(1L);
        usuario.setSenha(senhaCriptografada);

        String loginJson = objectMapper.writeValueAsString(login);

        BDDMockito.given(autenticacaoService.loadUserByUsername(anyString())).willReturn(usuario);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USUARIO_URI +"/logar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("nome").value("Jorvaldo"))
                .andExpect(jsonPath("tokenDto").isNotEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção de senha invalida ")
    public void naoDeveLogar() throws Exception {
        UsuarioFormLogin login = new UsuarioFormLogin();
        login.setEmail("jorvaldo@jorvaldo.com");
        login.setSenha("123456789112");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode("blavlasedw22wds");

        Usuario usuario = usuarioForm.converterParaEntidade();
        usuario.setId(1L);
        usuario.setSenha(senhaCriptografada);

        String loginJson = objectMapper.writeValueAsString(login);

        BDDMockito.given(autenticacaoService.loadUserByUsername(anyString())).willReturn(usuario);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USUARIO_URI +"/logar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("erro").value("Senha inválida"));
    }
}
