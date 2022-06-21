package br.com.todo.aplicacao.excecoes;

public class TarefaNaoPresenteNaMetaException extends Exception {

    private String mensagem;

    public TarefaNaoPresenteNaMetaException(String mensagem) {
        this.mensagem = mensagem;
    }

    public TarefaNaoPresenteNaMetaException() {

    }

    public String getMensagem() {
        return mensagem;
    }
}
