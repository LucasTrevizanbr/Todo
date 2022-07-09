package br.com.todo.domain.service.goal;

import java.util.List;

public interface SendMailService {

    void sendMails(String mensagem, List<String> emails) ;
}
