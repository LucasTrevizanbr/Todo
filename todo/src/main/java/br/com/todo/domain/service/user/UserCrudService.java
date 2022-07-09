package br.com.todo.domain.service.user;

import br.com.todo.application.exception.errors.ApiError;
import br.com.todo.application.exception.errors.NotFoundException;
import br.com.todo.domain.model.User;
import br.com.todo.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCrudService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserCrudService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User registerUser(User user) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("This email is already registered");
        }

        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        return save(user);
    }

    public User findById(Long idUser){
        Optional<User> user = userRepository.findById(idUser);
        if(user.isEmpty()){
            throw new NotFoundException(ApiError.TG001.getMessageError(), ApiError.TG001.name());
        }
        return user.get();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
