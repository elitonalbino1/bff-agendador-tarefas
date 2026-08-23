package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendador.infrastructure.client.TarefasClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  // ✅ Adicionar
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j  // ✅ Adicionar
@Service
@RequiredArgsConstructor
public class TarefasService {
    private final TarefasClient tarefasClient;

    public TarefasDTOResponse gravarTarefa(String token, TarefasDTORequest dto) {
        log.info("Gravando tarefa: {}", dto.getNomeTarefa());
        TarefasDTOResponse resultado = tarefasClient.gravaTarefas(dto, token);
        log.info("Tarefa gravada com ID: {}", resultado.getId());
        return resultado;
    }

    public List<TarefasDTOResponse> buscaTarefasAgendadasPorPeriodo(
            LocalDateTime dataInicial, LocalDateTime dataFinal, String token) {
        log.info("Buscando tarefas entre {} e {}", dataInicial, dataFinal);
        List<TarefasDTOResponse> tarefas = tarefasClient.buscaListaDeTarefasPorPeriodo(
                dataInicial, dataFinal, token);
        log.info("Encontradas tarefas agendadas {} tarefas", tarefas.size());
        return tarefas;
    }

    public List<TarefasDTOResponse> buscaTarefasPorEmail(String token) {
        log.info("Buscando tarefas por email");
        List<TarefasDTOResponse> tarefas = tarefasClient.buscarTarefasPorEmail(token);
        log.info("Encontradas por email {} tarefas", tarefas.size());
        return tarefas;
    }

    public void deletaTarefasPorId(String id, String token) {
        log.info("Deletando tarefa ID: {}", id);
        tarefasClient.deletaTarefasPorId(id, token);
        log.info("Tarefa deletada com sucesso");
    }

    public TarefasDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        log.info("Alterando status da tarefa {} para {}", id, status);
        TarefasDTOResponse resultado = tarefasClient.alteraStatusNotificacao(status, id, token);
        log.info("Status alterado com sucesso");
        return resultado;
    }

    public TarefasDTOResponse updateTarefas(TarefasDTORequest dto, String id, String token) {
        log.info("Atualizando tarefa ID: {}", id);
        TarefasDTOResponse resultado = tarefasClient.updateTarefas(dto, id, token);
        log.info("Tarefa atualizada com sucesso");
        return resultado;
    }
}