package br.com.todo.aplicacao.dto;

import br.com.todo.dominio.modelos.Usuario;

import java.time.LocalDateTime;

public class UsuarioDtoSimples {

    private Long id;

    private String nome;

    private String email;

    private String linkFoto;

    private String apelido;

    private TokenDto tokenDto;

    private Integer pontosConclusaoMetas;

    private LocalDateTime dataCadastro;

    public UsuarioDtoSimples(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.linkFoto = usuario.getLinkFoto();
        this.apelido = usuario.getApelido();
        this.pontosConclusaoMetas = usuario.getPontosConclusaoMetas();
        this.dataCadastro = usuario.getDataCadastro();
    }

    public Long getId() {
        return id;
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

    public Integer getPontosConclusaoMetas() {
        return pontosConclusaoMetas;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public String getEmail() {
        return email;
    }

    public TokenDto getTokenDto() {
        return tokenDto;
    }

    public void setTokenDto(TokenDto tokenDto) {
        this.tokenDto = tokenDto;
    }
}