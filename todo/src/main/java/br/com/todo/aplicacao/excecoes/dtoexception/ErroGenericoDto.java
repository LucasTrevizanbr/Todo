package br.com.todo.aplicacao.excecoes.dtoexception;

public class ErroGenericoDto {

    private String erro;

    public ErroGenericoDto(String mensagem) {
        this.erro = mensagem;
    }

    public String getErro() {
        return erro;
    }
}
