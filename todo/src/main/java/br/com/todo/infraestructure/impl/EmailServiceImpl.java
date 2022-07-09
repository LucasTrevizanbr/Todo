package br.com.todo.infraestructure.impl;

import br.com.todo.domain.service.goal.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMails(String mensagem, List<String> emails) {

        String [] emailsDosUsuarios = emails.toArray(new String[emails.size()]);

        SimpleMailMessage mensagemEmail = new SimpleMailMessage();

        mensagemEmail.setFrom("todoGaming@hotmail.com.br");
        mensagemEmail.setSubject("Meta no dia final de conclusão");
        mensagemEmail.setText(mensagem);
        mensagemEmail.setTo(emailsDosUsuarios);

        mailSender.send(mensagemEmail);
    }

}
