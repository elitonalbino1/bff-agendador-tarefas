package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.LoginRequestDTO;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronService {

    private final TarefasService tarefasService;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    @Value("${sistema.usuario.email}")
    private String email;

    @Value("${sistema.usuario.senha}")
    private String senha;

    @Scheduled(cron = "${cron.horario}")
    public void buscaTarefasProximaHora() {
        log.info("===== Iniciando job de notificação =====");

        try {
            String token = login(converterParaRequestDTO());
            String bearerToken = "Bearer " + token;

            LocalDateTime horaFutura = LocalDateTime.now().plusHours(1);
            LocalDateTime horaFuturaMaisCinco = horaFutura.plusMinutes(5);

            List<TarefasDTOResponse> listaTarefas = tarefasService
                    .buscaTarefasAgendadasPorPeriodo(horaFutura, horaFuturaMaisCinco, bearerToken);

            log.info("Encontradas {} tarefas para notificar", listaTarefas.size());

            // ✅ try/catch individual: uma tarefa com erro não para as outras
            listaTarefas.forEach(tarefa -> processarTarefa(tarefa, bearerToken));

            log.info("===== Job de notificação finalizado =====");

        } catch (Exception e) {
            // ✅ try/catch geral: falha não derruba o scheduler
            log.error("Erro ao executar job de notificação", e);
        }
    }

    private void processarTarefa(TarefasDTOResponse tarefa, String bearerToken) {
        try {
            emailService.enviaEmail(tarefa);
            log.info("Email enviado para: {}", tarefa.getEmailUsuario());

            tarefasService.alteraStatus(StatusNotificacaoEnum.NOTIFICADO, tarefa.getId(), bearerToken);
            log.info("Status da tarefa {} atualizado para NOTIFICADO", tarefa.getId());

        } catch (Exception e) {
            log.error("Erro ao processar tarefa ID: {}", tarefa.getId(), e);
        }
    }

    private String login(LoginRequestDTO dto) {
        return usuarioService.loginUsuario(dto);
    }

    private LoginRequestDTO converterParaRequestDTO() {
        return LoginRequestDTO.builder()
                .email(email)
                .senha(senha)
                .build();
    }
}