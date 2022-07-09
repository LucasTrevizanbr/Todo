package br.com.todo.application.controller.user.request;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotNull;

public class UserLoginRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

    public UsernamePasswordAuthenticationToken convertToAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
