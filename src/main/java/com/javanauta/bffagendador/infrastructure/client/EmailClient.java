package com.javanauta.bffagendador.infrastructure.client;

import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", url = "${notificacao.url}")
public interface EmailClient {

    void enviaEmail(@RequestBody TarefasDTOResponse dto);
}