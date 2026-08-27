package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendador.infrastructure.client.EmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailClient emailClient;

    public void enviaEmail(TarefasDTOResponse dto) {
        log.info("Solicitando envio de email para: {}", dto.getEmailUsuario());
        emailClient.enviaEmail(dto);
    }
}