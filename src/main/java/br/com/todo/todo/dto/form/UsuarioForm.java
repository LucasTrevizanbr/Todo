package br.com.todo.todo.dto.form;

import br.com.todo.todo.model.Usuario;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UsuarioForm {

    @NotNull @Size(max = 200)
    private String nome;

    private String linkFoto = "";

    @Size(max = 30)
    private String apelido;

    @Email
    private String email;

    @NotNull @Size(min = 12, max = 30)
    private String senha;

    private Integer pontosConclusaoMetas = 0;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    public Usuario converterParaEntidade() {
        Usuario usuarioEntidade = new Usuario();
        usuarioEntidade.setNome(this.nome);
        usuarioEntidade.setLinkFoto(this.linkFoto);
        usuarioEntidade.setApelido(this.apelido);
        usuarioEntidade.setEmail(this.email);
        usuarioEntidade.setSenha(this.senha);
        usuarioEntidade.setPontosConclusaoMetas(this.pontosConclusaoMetas);
        usuarioEntidade.setDataCadastro(this.dataCadastro);

        return  usuarioEntidade;
    }

    public String getNome() {
        return nome;
    }

    public String getLinkFoto() {
        return linkFoto;
    }

    public String getApelido() {
        return apelido;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Integer getPontosConclusaoMetas() {
        return pontosConclusaoMetas;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
