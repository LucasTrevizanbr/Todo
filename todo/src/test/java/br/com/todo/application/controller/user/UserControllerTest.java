package br.com.todo.application.controller.user;

import br.com.todo.application.controller.user.request.PostUserRequest;
import br.com.todo.application.controller.user.request.UserLoginRequest;
import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.domain.model.User;
import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.service.user.UserCrudService;
import br.com.todo.infraestructure.security.AuthenticationLoginService;
import br.com.todo.infraestructure.security.token.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserControllerTest {

    private final String USERS_URI = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  AuthenticationManager authManager;

    @Autowired
    private  TokenService tokenService;

    @Autowired
    private  UserCrudService userCrudService;

    @Autowired
    private  AuthenticationLoginService authLoginService;

    @Autowired
    private  ObjectMapper objectMapper;

    @BeforeEach
    public void cleanDatabase(){
        userRepository.deleteAll();
    }

    @AfterEach
    public void tearDown(){ userRepository.deleteAll(); }

    @Test
    @DisplayName("Should create a user with success")
    public void registerUserSuccessTest() throws Exception {
        PostUserRequest userRequest = new PostUserRequest();
        userRequest.setEmail("test@test.com");
        userRequest.setPassword("test12345678");
        userRequest.setNickName("Pori");
        userRequest.setName("Yori Pori Júnior");

        String postUserRequestJson = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(USERS_URI+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postUserRequestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("Yori Pori Júnior"))
                .andExpect(jsonPath("nickName").value("Pori"))
                .andExpect(jsonPath("email").value("test@test.com"))
                .andExpect(jsonPath("conclusionPointsGoal").value(0));

        List<User> savedUser = userRepository.findAll();
        assertEquals("Yori Pori Júnior", savedUser.get(0).getName());
        assertEquals("Pori", savedUser.get(0).getNickName());

    }

    @Test
    @DisplayName("Should throw exception containing invalid inputs in errors field")
    public void registerUserWithErrorTest() throws Exception {
        PostUserRequest userRequest = new PostUserRequest();
        userRequest.setEmail("invalidEmail.com");
        userRequest.setPassword("invalidPass");
        userRequest.setNickName(" ");
        userRequest.setName(" ");

        String postUserRequestJson = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post(USERS_URI+"/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postUserRequestJson)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message").value("Invalid arguments"))
                .andExpect(jsonPath("internalCode").value(ApiError.TG002.name()))
                .andExpect(jsonPath("errors").isNotEmpty());

        List<User> savedUser = userRepository.findAll();
        assertThat(savedUser).isEmpty();
    }

    @Test
    @DisplayName("Should login a user with success and give back a authentication token")
    public void loginUserSuccessTest() throws Exception {
        this.saveUserToMakeLogin();
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("teste1234567");

        String postUserRequestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(USERS_URI + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postUserRequestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("tokenDto").isNotEmpty())
                .andExpect(jsonPath("email").value("test@test.com"));

    }


    private void saveUserToMakeLogin() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword(encoder.encode("teste1234567"));

        userRepository.save(user);
    }

}