package br.com.todo.todo.exceptions;

public class TarefaNaoPresenteNaMetaException extends Exception {

    private String mensagem;

    public TarefaNaoPresenteNaMetaException(String mensagem) {
        this.mensagem = mensagem;
    }

    public TarefaNaoPresenteNaMetaException() {

    }
}
