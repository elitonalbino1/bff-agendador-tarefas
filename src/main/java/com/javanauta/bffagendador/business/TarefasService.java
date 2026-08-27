package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendador.infrastructure.client.TarefasClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasClient tarefasClient;

    public TarefasDTOResponse gravarTarefa(String token, TarefasDTORequest dto) {
        log.info("Gravando tarefa: {}", dto.getNomeTarefa());
        return tarefasClient.gravaTarefas(dto, token);
    }

    public List<TarefasDTOResponse> buscaTarefasAgendadasPorPeriodo(
            LocalDateTime dataInicial, LocalDateTime dataFinal, String token) {
        log.info("Buscando tarefas entre {} e {}", dataInicial, dataFinal);
        return tarefasClient.buscaListaDeTarefasPorPeriodo(dataInicial, dataFinal, token);
    }

    public List<TarefasDTOResponse> buscaTarefasPorEmail(String token) {
        log.info("Buscando tarefas por email");
        return tarefasClient.buscarTarefasPorEmail(token);
    }

    public void deletaTarefasPorId(String id, String token) {
        log.info("Deletando tarefa ID: {}", id);
        tarefasClient.deletaTarefasPorId(id, token);
    }

    public TarefasDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        log.info("Alterando status da tarefa {} para {}", id, status);
        return tarefasClient.alteraStatusNotificacao(status, id, token);
    }

    public TarefasDTOResponse updateTarefas(TarefasDTORequest dto, String id, String token) {
        log.info("Atualizando tarefa ID: {}", id);
        return tarefasClient.updateTarefas(dto, id, token);
    }
}