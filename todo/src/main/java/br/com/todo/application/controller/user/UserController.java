package br.com.todo.application.controller.user;

import br.com.todo.infraestructure.security.token.TokenDto;
import br.com.todo.application.controller.user.response.UserLoginResponse;
import br.com.todo.application.controller.user.request.PostUserRequest;
import br.com.todo.application.controller.user.request.UserLoginRequest;
import br.com.todo.domain.model.User;
import br.com.todo.domain.repository.UserRepository;
import br.com.todo.domain.service.user.UserCrudService;
import br.com.todo.infraestructure.security.token.TokenService;
import br.com.todo.infraestructure.security.AuthenticationLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository usuarioRepository;

    private final AuthenticationManager authManager;

    private final TokenService tokenService;

    private final UserCrudService userCrudService;

    private final AuthenticationLoginService authLoginService;

    public UserController(UserRepository usuarioRepository, AuthenticationManager authManager,
                          TokenService tokenService, UserCrudService userCrudService,
                          AuthenticationLoginService authLoginService) {
        this.usuarioRepository = usuarioRepository;
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userCrudService = userCrudService;
        this.authLoginService = authLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserLoginResponse> createUser(@RequestBody @Valid PostUserRequest formUsuario){

        User usuario = formUsuario.convertToUserModel();
        usuario = userCrudService.registerUser(usuario);

        UserLoginResponse usuarioDto = new UserLoginResponse(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest loginRequest){

        User user = (User) authLoginService.loadUserByUsername(loginRequest.getEmail());
        user = authLoginService.validatePassword(user, loginRequest.getPassword());

        try{
            UsernamePasswordAuthenticationToken userLogged = loginRequest.convertToAuthentication();
            Authentication userAuthenticated = authManager.authenticate(userLogged);

            String token = tokenService.generateTokenToUserAuthenticated(userAuthenticated);

            UserLoginResponse userLoginResponse = new UserLoginResponse(user);
            userLoginResponse.setTokenDto(new TokenDto(token, "Bearer "));

            return ResponseEntity.ok(userLoginResponse);
        }catch (AuthenticationException ex){
            return ResponseEntity.badRequest().build();
        }

    }

}
