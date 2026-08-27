package com.javanauta.bffagendador.business.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefoneDTORequest {

    @NotBlank(message = "Número é obrigatório")
    @Pattern(regexp = "\\d{8,9}", message = "Número deve ter 8 ou 9 dígitos")
    private String numero;

    @NotBlank(message = "DDD é obrigatório")
    @Pattern(regexp = "\\d{2,3}", message = "DDD deve ter 2 ou 3 dígitos")
    private String ddd;
}