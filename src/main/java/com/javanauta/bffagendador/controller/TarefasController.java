package com.javanauta.bffagendador.controller;

import com.javanauta.bffagendador.business.TarefasService;
import com.javanauta.bffagendador.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendador.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    @Operation(summary = "Salvar Tarefa", description = "Cria uma nova tarefa")
    public ResponseEntity<TarefasDTOResponse> gravaTarefas(
            @Valid @RequestBody TarefasDTORequest dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tarefasService.gravarTarefa(token, dto));
    }

    @GetMapping("/eventos")
    @Operation(summary = "Buscar Tarefas por Período")
    public ResponseEntity<List<TarefasDTOResponse>> buscaListaDeTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.buscaTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token));
    }

    @GetMapping
    @Operation(summary = "Buscar Tarefas do Usuário Logado")
    public ResponseEntity<List<TarefasDTOResponse>> buscarTarefasPorEmail(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.buscaTarefasPorEmail(token));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Tarefa")
    public ResponseEntity<Void> deletaTarefasPorId(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String token) {
        tarefasService.deletaTarefasPorId(id, token);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Alterar Status da Tarefa")
    public ResponseEntity<TarefasDTOResponse> alteraStatusNotificacao(
            @RequestParam("status") StatusNotificacaoEnum status,
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.alteraStatus(status, id, token));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Tarefa")
    public ResponseEntity<TarefasDTOResponse> updateTarefas(
            @Valid @RequestBody TarefasDTORequest dto,
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.updateTarefas(dto, id, token));
    }
}