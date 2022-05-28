package br.com.todo.dominio.servicos.meta;

import br.com.todo.dominio.repositorios.MetaRepository;
import br.com.todo.dominio.modelos.Meta;
import br.com.todo.dominio.servicos.EnviarEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VencimentoMetaService {

    private static final String CRON_META_PRAZO_FINAL = "0 0 1 ? * MON-FRI";

    private LocalDateTime hoje = LocalDateTime.now();

    @Value("${application.mail.meta-dia-vencimento.mensagem}")
    private String mensagem ;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private EnviarEmailService emailService;

    @Scheduled(cron = CRON_META_PRAZO_FINAL)
    public void enviarEmailMetaNaoConcluidaDiaFinal(){

        LocalDateTime inicioDia = hoje.toLocalDate().atStartOfDay();
        LocalDateTime fimDia = hoje.with(LocalTime.of(23, 59, 59));

        List<Meta> metasNoPrazoFinal = metaRepository.listarMetasNoDiaFinal(inicioDia, fimDia);

        List<String> emails = metasNoPrazoFinal
                .stream()
                .map(meta -> meta.getUsuario().getEmail())
                .collect(Collectors.toList());

        emailService.enviarEmails(mensagem, emails);
    }

}
