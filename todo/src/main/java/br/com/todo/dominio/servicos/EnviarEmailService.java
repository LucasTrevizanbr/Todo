package br.com.todo.dominio.servicos;

import java.util.List;

public interface EnviarEmailService {

    public void enviarEmails(String mensagem, List<String> emails) ;
}
