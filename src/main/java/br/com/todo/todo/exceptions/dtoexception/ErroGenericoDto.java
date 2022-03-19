package br.com.todo.todo.exceptions.dtoexception;

public class ErroGenericoDto {

    private String erro;

    public ErroGenericoDto(String mensagem) {
        this.erro = mensagem;
    }

    public String getErro() {
        return erro;
    }
}
