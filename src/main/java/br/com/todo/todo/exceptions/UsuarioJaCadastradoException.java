package br.com.todo.todo.exceptions;

public class UsuarioJaCadastradoException extends RuntimeException {

    private String mensagem;

    public UsuarioJaCadastradoException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
