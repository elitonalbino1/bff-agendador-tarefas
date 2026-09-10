package com.javanauta.bffagendador.infrastructure.client;

import com.javanauta.bffagendador.business.dto.in.*;
import com.javanauta.bffagendador.business.dto.out.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    // Buscar usuário por email
    @GetMapping
    UsuarioDTOResponse buscaUsuarioPorEmail(@RequestParam("email") String email,
                                            @RequestHeader("Authorization") String token);

    // Salvar novo usuário
    @PostMapping
    UsuarioDTOResponse salvarUsuario(@RequestBody UsuarioDTORequest usuarioDTO);

    // Login do usuário
    @PostMapping("/login")
    Map<String, String> login(@RequestBody LoginRequestDTO usuarioDTO);

    // Deletar usuário por email
    @DeleteMapping("/{email}")
    void deletaUsuarioPorEmail(@PathVariable("email") String email,
                               @RequestHeader("Authorization") String token);

    // Atualizar dados do usuário
    @PutMapping
    UsuarioDTOResponse atualizaDadosUsuario(@RequestBody UsuarioDTORequest dto,
                                            @RequestHeader("Authorization") String token);

    // Atualizar endereço
    @PutMapping("/endereco/{id}")
    EnderecoDTOResponse atualizaEndereco(@RequestBody EnderecoDTORequest dto,
                                         @PathVariable("id") Long id,
                                         @RequestHeader("Authorization") String token);

    // Atualizar telefone
    @PutMapping("/telefone/{id}")
    TelefoneDTOResponse atualizaTelefone(@RequestBody TelefoneDTORequest dto,
                                         @PathVariable("id") Long id,
                                         @RequestHeader("Authorization") String token);

    // Cadastrar endereço
    @PostMapping("/endereco")
    EnderecoDTOResponse cadastraEndereco(@RequestBody EnderecoDTORequest dto,
                                         @RequestHeader("Authorization") String token);

    // Cadastrar telefone
    @PostMapping("/telefone")
    TelefoneDTOResponse cadastraTelefone(@RequestBody TelefoneDTORequest dto,
                                         @RequestHeader("Authorization") String token);
}
