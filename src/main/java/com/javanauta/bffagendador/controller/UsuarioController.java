package com.javanauta.bffagendador.controller;

import com.javanauta.bffagendador.business.UsuarioService;
import com.javanauta.bffagendador.business.dto.in.*;
import com.javanauta.bffagendador.business.dto.out.*;
import com.javanauta.bffagendador.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Cadastro e login de usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Salvar Usuário", description = "Cria um novo usuário")
    public ResponseEntity<UsuarioDTOResponse> salvarUsuario(
            @Valid @RequestBody UsuarioDTORequest usuarioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Usuário", description = "Login do usuário")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequestDTO usuarioDTO) {
        String token = usuarioService.loginUsuario(usuarioDTO);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping
    @Operation(summary = "Buscar Usuário por Email", description = "Busca dados do usuário pelo email")
    public ResponseEntity<UsuarioDTOResponse> buscaUsuarioPorEmail(
            @RequestParam("email") String email,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email, token));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar Usuário por Email", description = "Remove usuário pelo email")
    public ResponseEntity<Void> deletaUsuarioPorEmail(
            @PathVariable("email") String email,
            @RequestHeader("Authorization") String token) {
        usuarioService.deletaUsuarioPorEmail(email, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "Atualizar Usuário", description = "Atualiza dados do usuário")
    public ResponseEntity<UsuarioDTOResponse> atualizaDadosUsuario(
            @RequestBody UsuarioDTORequest dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaDadosdeUsuario(token, dto));
    }

    @PutMapping("/endereco/{id}")
    @Operation(summary = "Atualizar Endereço", description = "Atualiza endereço do usuário")
    public ResponseEntity<EnderecoDTOResponse> atualizaEndereco(
            @RequestBody EnderecoDTORequest dto,
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto, token));
    }

    @PutMapping("/telefone/{id}")
    @Operation(summary = "Atualizar Telefone", description = "Atualiza telefone do usuário")
    public ResponseEntity<TelefoneDTOResponse> atualizaTelefone(
            @RequestBody TelefoneDTORequest dto,
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto, token));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Cadastrar Endereço", description = "Salva novo endereço do usuário")
    public ResponseEntity<EnderecoDTOResponse> cadastraEndereco(
            @Valid @RequestBody EnderecoDTORequest dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Cadastrar Telefone", description = "Salva novo telefone do usuário")
    public ResponseEntity<TelefoneDTOResponse> cadastraTelefone(
            @Valid @RequestBody TelefoneDTORequest dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastraTelefone(token, dto));
    }
}