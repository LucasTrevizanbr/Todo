package br.com.todo.application.controller.user.response;

import br.com.todo.infraestructure.security.token.TokenDto;
import br.com.todo.domain.model.User;

import java.time.LocalDateTime;

public class UserLoginResponse {

    private final Long id;

    private final String nome;

    private final String email;

    private final String linkFoto;

    private final String apelido;

    private TokenDto tokenDto;

    private final Integer pontosConclusaoMetas;

    private final LocalDateTime dataCadastro;

    public UserLoginResponse(User usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getName();
        this.email = usuario.getEmail();
        this.linkFoto = usuario.getPhotoLink();
        this.apelido = usuario.getNickName();
        this.pontosConclusaoMetas = usuario.getConclusionPointsGoal();
        this.dataCadastro = usuario.getRegistrationDate();
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
