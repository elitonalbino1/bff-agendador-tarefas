package com.javanauta.bffagendador.business.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefasDTORequest {

    @NotBlank(message = "Nome da tarefa é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nomeTarefa;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String descricaoTarefa;

    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataEvento;
}