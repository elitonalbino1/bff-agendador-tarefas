package com.javanauta.bffagendador.business.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import lombok.*;

import java.time.LocalDateTime;

// ✅ CORRIGIDO: removidas as validações (inúteis em DTO de resposta)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefasDTOResponse {

    private String id;
    private String nomeTarefa;
    private String descricaoTarefa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;

    private String emailUsuario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataAlteracao;

    private StatusNotificacaoEnum statusNotificacaoEnum;
}