package com.javanauta.bffagendador.business.dto.out;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTOResponse {

    private Long id;
    private String nome;
    private String email;
    // ✅ senha REMOVIDA da resposta
    private List<EnderecoDTOResponse> enderecos;
    private List<TelefoneDTOResponse> telefones;
}