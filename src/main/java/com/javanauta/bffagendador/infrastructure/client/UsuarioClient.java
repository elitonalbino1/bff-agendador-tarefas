package com.javanauta.bffagendador.infrastructure.client;

import com.javanauta.bffagendador.business.dto.in.*;
import com.javanauta.bffagendador.business.dto.out.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// ✅ URL base sem path — paths completos nas annotations
@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    @GetMapping("/usuario")
    UsuarioDTOResponse buscaUsuarioPorEmail(@RequestParam("email") String email,
                                            @RequestHeader("Authorization") String token);

    @PostMapping("/usuario")
    UsuarioDTOResponse salvarUsuario(@RequestBody UsuarioDTORequest usuarioDTO);

    // ✅ CORRIGIDO: retorna Map (serviço usuario retorna {"token": "..."})
    @PostMapping("/usuario/login")
    Map<String, String> login(@RequestBody LoginRequestDTO usuarioDTO);

    @DeleteMapping("/usuario/{email}")
    void deletaUsuarioPorEmail(@PathVariable("email") String email,
                               @RequestHeader("Authorization") String token);

    @PutMapping("/usuario")
    UsuarioDTOResponse atualizaDadosUsuario(@RequestBody UsuarioDTORequest dto,
                                            @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/endereco/{id}")
    EnderecoDTOResponse atualizaEndereco(@RequestBody EnderecoDTORequest dto,
                                         @PathVariable("id") Long id,
                                         @RequestHeader("Authorization") String token);

    @PutMapping("/usuario/telefone/{id}")
    TelefoneDTOResponse atualizaTelefone(@RequestBody TelefoneDTORequest dto,
                                         @PathVariable("id") Long id,
                                         @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/endereco")
    EnderecoDTOResponse cadastraEndereco(@RequestBody EnderecoDTORequest dto,
                                         @RequestHeader("Authorization") String token);

    @PostMapping("/usuario/telefone")
    TelefoneDTOResponse cadastraTelefone(@RequestBody TelefoneDTORequest dto,
                                         @RequestHeader("Authorization") String token);
}