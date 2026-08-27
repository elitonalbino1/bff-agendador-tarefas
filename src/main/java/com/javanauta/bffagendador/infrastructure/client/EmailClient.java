package com.javanauta.bffagendador.infrastructure.client;

import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// ✅ CORRIGIDO: URL base + path /email na annotation
@FeignClient(name = "notificacao", url = "${notificacao.url}")
public interface EmailClient {

    @PostMapping("/email")
    void enviaEmail(@RequestBody TarefasDTOResponse dto);
}