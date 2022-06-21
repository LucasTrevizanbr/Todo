package br.com.todo.aplicacao.excecoes;

public class UsuarioJaCadastradoException extends RuntimeException {

    private String mensagem;

    public UsuarioJaCadastradoException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
