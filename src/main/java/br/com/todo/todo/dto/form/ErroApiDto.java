package br.com.todo.todo.dto.form;

public class ErroApiDto {

    private String mensagem;

    public ErroApiDto(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
