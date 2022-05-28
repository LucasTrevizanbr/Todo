package br.com.todo.aplicacao.excecoes;

public class MetaParadaException extends RuntimeException {

    private String mensagem;

    public MetaParadaException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
