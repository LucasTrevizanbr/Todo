package br.com.todo.todo.exceptions.dtoexception;

public class ErroValidacaoDto {

    private final String campo;
    private final String msgErro;

    public ErroValidacaoDto(String campo, String msgErro) {
        this.campo = campo;
        this.msgErro = msgErro;
    }

    public String getCampo() {
        return campo;
    }

    public String getMsgErro() {
        return msgErro;
    }
}
