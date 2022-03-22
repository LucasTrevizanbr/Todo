package br.com.todo.todo.controller;

import br.com.todo.todo.dto.UsuarioDtoSimples;
import br.com.todo.todo.dto.form.UsuarioForm;
import br.com.todo.todo.dto.form.UsuarioFormLogin;
import br.com.todo.todo.model.Usuario;
import br.com.todo.todo.repository.UsuarioRepository;
import br.com.todo.todo.service.usuario.AutenticacaoService;
import br.com.todo.todo.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDtoSimples> criarUsuario(@RequestBody @Valid UsuarioForm formUsuario){

        Usuario usuario = formUsuario.converterParaEntidade();
        usuario = usuarioService.cadastrarUsuario(usuario);

        UsuarioDtoSimples usuarioDto = new UsuarioDtoSimples(usuario);
        return ResponseEntity.ok(usuarioDto);

    }

    @PostMapping("/logar")
    public ResponseEntity<UsuarioDtoSimples> logar(@RequestBody @Valid UsuarioFormLogin formUsuarioLogin){

        Usuario usuario = (Usuario) autenticacaoService.loadUserByUsername(formUsuarioLogin.getEmail());
        usuario = usuarioService.validarSenha(usuario, formUsuarioLogin.getSenha());

        UsuarioDtoSimples usuarioDto = new UsuarioDtoSimples(usuario);
        return ResponseEntity.ok(usuarioDto);
    }

}
