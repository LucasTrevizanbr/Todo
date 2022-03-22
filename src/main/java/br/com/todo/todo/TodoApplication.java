package br.com.todo.todo;

import br.com.todo.todo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableSpringDataWebSupport
@SpringBootApplication
@EnableScheduling
public class TodoApplication {

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}
}
