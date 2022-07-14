package br.com.todo.domain.service.user;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.User;
import br.com.todo.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class UserCrudServiceTest {

    @MockBean
    private  UserRepository userRepository;

    @MockBean
    private  PasswordEncoder encoder;

    @Autowired
    private UserCrudService userCrudService;

    @Test
    @DisplayName("Should return a user")
    public void findUserByIdTest(){
        User user = new User();
        user.setEmail("teste@teste.com");
        user.setPassword("teste1111");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User retrievedUser = userCrudService.findById(anyLong());

        assertThat(retrievedUser).isNotNull();
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should register a user")
    public void registerUserTest(){
        User user = new User();
        user.setEmail("teste@teste.com");
        user.setPassword("teste1111");

        when(userRepository.findByEmail("teste@teste.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userCrudService.registerUser(user);

        assertThat(savedUser.getEmail()).isEqualTo("teste@teste.com");
        verify(userRepository, Mockito.times(1)).findByEmail(anyString());
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw a error when a email is already in use")
    public void registerUserErrorTest(){
        User user = new User();
        user.setEmail("teste@teste.com");

        when(userRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userCrudService.registerUser(user),
                "This email is already registered");

        verify(userRepository, Mockito.times(1)).findByEmail(anyString());
        verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    @DisplayName("Should return a not found exception with message ApiError code TG001")
    public void findUserByIdErrorTest(){
        User user = new User();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userCrudService.findById(anyLong()),
                ApiError.TG001.getMessageError());

        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

}