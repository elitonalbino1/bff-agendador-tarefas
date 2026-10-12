package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.LoginRequestDTO;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendador.infrastructure.exceptions.UnauthorizedException;
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
            if (token == null || token.isBlank()) {
                log.warn("Job cancelado: Não foi possível obter o token de sistema.");
                return;
            }

            String bearerToken = "Bearer " + token;

            LocalDateTime horaFutura = LocalDateTime.now().plusHours(1);
            LocalDateTime horaFuturaMaisCinco = horaFutura.plusMinutes(5);

            List<TarefasDTOResponse> listaTarefas = tarefasService
                    .buscaTarefasAgendadasPorPeriodo(horaFutura, horaFuturaMaisCinco, bearerToken);

            log.info("Encontradas {} tarefas para notificar", listaTarefas.size());

            // Processa cada tarefa individualmente
            listaTarefas.forEach(tarefa -> processarTarefa(tarefa, bearerToken));

            log.info("===== Job de notificação finalizado =====");

        } catch (UnauthorizedException e) {
            // ✅ Captura o 401 sem estourar stack trace vermelho
            log.warn("Job de notificação não executado: Usuário de sistema ({}) não encontrado ou credenciais inválidas.", email);
        } catch (Exception e) {
            // ✅ Loga apenas a mensagem do erro sem a pilha inteira
            log.error("Erro ao executar job de notificação: {}", e.getMessage());
        }
    }

    private void processarTarefa(TarefasDTOResponse tarefa, String bearerToken) {
        try {
            emailService.enviaEmail(tarefa);
            log.info("Email enviado para: {}", tarefa.getEmailUsuario());

            tarefasService.alteraStatus(StatusNotificacaoEnum.NOTIFICADO, tarefa.getId(), bearerToken);
            log.info("Status da tarefa {} atualizado para NOTIFICADO", tarefa.getId());

        } catch (Exception e) {
            log.error("Erro ao processar tarefa ID {}: {}", tarefa.getId(), e.getMessage());
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