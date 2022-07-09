package br.com.todo.api.controller;

import br.com.todo.domain.repository.UserRepository;
import br.com.todo.application.controller.user.request.PostUserRequest;
import br.com.todo.application.controller.user.request.UserLoginRequest;
import br.com.todo.domain.model.User;
import br.com.todo.infraestructure.security.AuthenticationLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    UserRepository usuarioRepository;

    @MockBean
    AuthenticationLoginService autenticacaoService;

    private ObjectMapper objectMapper;

    private PostUserRequest usuarioForm;

    /*
    @BeforeEach
    public void setUp(){

        usuarioForm = new PostUserRequest("Jorvaldo", "jojo", "jorvaldo@jorvaldo.com",
                "123456789112");

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }*/

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso e devolver")
    public void deveCadastrarUsuario() throws Exception {

        User usuario = usuarioForm.convertToUserModel();
        usuario.setId(1L);

        String usuarioFormJson = objectMapper.writeValueAsString(usuarioForm);

        BDDMockito.given(usuarioRepository.findByEmail(anyString())).willReturn(Optional.empty());
        BDDMockito.given(usuarioRepository.save(any(User.class))).willReturn(usuario);

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

        User usuario = usuarioForm.convertToUserModel();
        usuario.setId(1L);

        String usuarioFormJson = objectMapper.writeValueAsString(usuarioForm);

        BDDMockito.given(usuarioRepository.findByEmail(anyString())).willReturn(Optional.of(usuario));
        BDDMockito.given(usuarioRepository.save(any(User.class))).willReturn(usuario);

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
        UserLoginRequest login = new UserLoginRequest();
        login.setEmail("jorvaldo@jorvaldo.com");
        login.setPassword("123456789112");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(login.getPassword());

        User usuario = usuarioForm.convertToUserModel();
        usuario.setId(1L);
        usuario.setPassword(senhaCriptografada);

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
        UserLoginRequest login = new UserLoginRequest();
        login.setEmail("jorvaldo@jorvaldo.com");
        login.setPassword("123456789112");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode("blavlasedw22wds");

        User usuario = usuarioForm.convertToUserModel();
        usuario.setId(1L);
        usuario.setPassword(senhaCriptografada);

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
