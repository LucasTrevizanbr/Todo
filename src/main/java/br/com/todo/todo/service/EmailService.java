package br.com.todo.todo.service;

import java.util.List;

public interface EmailService {

    public void enviarEmails(String mensagem, List<String> emails) ;
}
