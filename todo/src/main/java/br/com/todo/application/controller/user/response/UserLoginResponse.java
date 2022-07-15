package br.com.todo.application.controller.user.response;

import br.com.todo.infraestructure.security.token.TokenDto;
import br.com.todo.domain.model.User;

import java.time.LocalDateTime;

public class UserLoginResponse {

    private final Long id;

    private final String name;

    private final String email;

    private final String linkPhoto;

    private final String nickName;

    private TokenDto tokenDto;

    private final Integer conclusionPointsGoal;

    private final LocalDateTime registerDate;

    public UserLoginResponse(User usuario) {
        this.id = usuario.getId();
        this.name = usuario.getName();
        this.email = usuario.getEmail();
        this.linkPhoto = usuario.getPhotoLink();
        this.nickName = usuario.getNickName();
        this.conclusionPointsGoal = usuario.getConclusionPointsGoal();
        this.registerDate = usuario.getRegistrationDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getConclusionPointsGoal() {
        return conclusionPointsGoal;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
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
