package com.javanauta.bffagendador.infrastructure.client;

import com.javanauta.bffagendador.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "agendador-tarefas", url = "${agendador-tarefas.url}")
public interface TarefasClient {

    @PostMapping
    TarefasDTOResponse gravaTarefas(@RequestBody TarefasDTORequest dto,
                                    @RequestHeader("Authorization") String token);

    @GetMapping("/eventos")
    List<TarefasDTOResponse> buscaListaDeTarefasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader("Authorization") String token);

    @GetMapping
    List<TarefasDTOResponse> buscarTarefasPorEmail(@RequestHeader("Authorization") String token);

    @DeleteMapping("/{id}")
    void deletaTarefasPorId(@PathVariable("id") String id,
                            @RequestHeader("Authorization") String token);

    @PatchMapping("/{id}")
    TarefasDTOResponse alteraStatusNotificacao(@RequestParam("status") StatusNotificacaoEnum status,
                                               @PathVariable("id") String id,
                                               @RequestHeader("Authorization") String token);

    @PutMapping("/{id}")
    TarefasDTOResponse updateTarefas(@RequestBody TarefasDTORequest dto,
                                     @PathVariable("id") String id,
                                     @RequestHeader("Authorization") String token);
}
