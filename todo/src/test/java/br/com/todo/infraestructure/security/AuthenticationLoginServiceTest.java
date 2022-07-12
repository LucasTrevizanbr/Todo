package br.com.todo.infraestructure.security;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.InvalidPasswordException;
import br.com.todo.application.exception.errors.UnfinishedTasksException;
import br.com.todo.domain.model.User;
import br.com.todo.domain.model.enums.Status;
import br.com.todo.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class AuthenticationLoginServiceTest {

    @MockBean
    private UserRepository usuarioRepository;

    @MockBean
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationLoginService authenticationLoginService;

    @Test
    @DisplayName("Should return a UserDetails")
    public void findByEmailWithSuccess(){
        User user = new User();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails existentUser =  authenticationLoginService.loadUserByUsername(anyString());

        assertThat(existentUser).isNotNull();
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should return a valid user")
    public void validatePasswordWithSuccess(){
        User user = new User();
        user.setPassword(encoder.encode("teste"));
        when(encoder.matches("teste", user.getPassword())).thenReturn(true);

        User validUser = authenticationLoginService.validatePassword(user,"teste");

        assertThat(validUser).isNotNull();
    }

    @Test
    @DisplayName("Should throw a exception when trying to authenticate a user not registered")
    public void findByEmailWithError(){

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,() -> authenticationLoginService.loadUserByUsername(anyString()),
                "Invalid Email");

        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should throw a exception when password doesn't match")
    public void validatePasswordWIthErro(){
        User user = new User();
        CharSequence charSequence = new StringBuffer();
        when(encoder.matches(charSequence, "teste")).thenReturn(false);

        assertThrows(InvalidPasswordException.class,() -> authenticationLoginService.validatePassword(user,"teste"),
                ApiError.TG201.getMessageError());
    }
}