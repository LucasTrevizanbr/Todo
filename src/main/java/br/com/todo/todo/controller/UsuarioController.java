package br.com.todo.todo.controller;

import br.com.todo.todo.dto.TokenDto;
import br.com.todo.todo.dto.UsuarioDtoSimples;
import br.com.todo.todo.dto.form.UsuarioForm;
import br.com.todo.todo.dto.form.UsuarioFormLogin;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.UsuarioRepository;
import br.com.todo.todo.service.TokenService;
import br.com.todo.todo.service.usuario.AutenticacaoService;
import br.com.todo.todo.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDtoSimples> criarUsuario(@RequestBody @Valid UsuarioForm formUsuario){

        Usuario usuario = formUsuario.converterParaEntidade();
        usuario = usuarioService.cadastrarUsuario(usuario);

        UsuarioDtoSimples usuarioDto = new UsuarioDtoSimples(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);

    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioDtoSimples> logar(@RequestBody @Valid UsuarioFormLogin formUsuarioLogin){

        Usuario usuario = (Usuario) autenticacaoService.loadUserByUsername(formUsuarioLogin.getEmail());
        usuario = usuarioService.validarSenha(usuario, formUsuarioLogin.getSenha());

        try{
            UsernamePasswordAuthenticationToken dadosLogin = formUsuarioLogin.converter();
            Authentication usuarioAutenticado = authManager.authenticate(dadosLogin);

            String token = tokenService.gerarToken(usuarioAutenticado);

            UsuarioDtoSimples usuarioDto = new UsuarioDtoSimples(usuario);
            usuarioDto.setTokenDto(new TokenDto(token, "Bearer "));

            return ResponseEntity.ok(usuarioDto);
        }catch (AuthenticationException ex){
                return ResponseEntity.badRequest().build();
        }

    }

}
