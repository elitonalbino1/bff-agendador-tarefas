package com.javanauta.bffagendador.business.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javanauta.bffagendador.business.enums.StatusNotificacaoEnum;
import lombok.*;

import jakarta.validation.constraints.*;  // ✅ Usar jakarta.validation

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefasDTOResponse {

    private String id;

    @NotBlank(message = "Nome da tarefa é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nomeTarefa;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String descricaoTarefa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;

    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email inválido")
    private String emailUsuario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataAlteracao;

    private StatusNotificacaoEnum statusNotificacaoEnum;
}