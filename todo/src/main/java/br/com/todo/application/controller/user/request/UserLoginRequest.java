package br.com.todo.application.controller.user.request;

import javax.validation.constraints.NotNull;

public class UserLoginRequest {

    @NotNull
    private String email;

    @NotNull
    private String senha;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
