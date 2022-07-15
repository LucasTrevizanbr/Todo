package br.com.todo.application.controller.user.request;

import br.com.todo.domain.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostUserRequest {

    @NotNull @Size(max = 200)
    private String name;

    private final String linkPhoto = "";

    @Size(max = 30)
    private String nickName;

    @Email
    private String email;

    @NotNull @Size(min = 12, max = 30)
    private String password;


    public User convertToUserModel() {
        User usuarioEntidade = new User();
        usuarioEntidade.setName(this.name);
        usuarioEntidade.setPhotoLink(this.linkPhoto);
        usuarioEntidade.setNickName(this.nickName);
        usuarioEntidade.setEmail(this.email);
        usuarioEntidade.setPassword(this.password);

        return  usuarioEntidade;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
