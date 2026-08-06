package com.javanauta.bffagendador.controller;

import com.javanauta.bffagendador.business.UsuarioService;
import com.javanauta.bffagendador.business.dto.EnderecoDTO;
import com.javanauta.bffagendador.business.dto.TelefoneDTO;
import com.javanauta.bffagendador.business.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
@Tag(name = "usuario", description = "Cadastro e login de usuarios")

public class UsuarioController {
    private final UsuarioService usuarioService;


    @PostMapping
    @Operation(summary = "Salvar Usuarios", description = "Cria um novo Usuario")
    @ApiResponse(responseCode = "200", description = "Usuario salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuario ja cadastrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login Usuarios", description = "Login do Usuario")
    @ApiResponse(responseCode = "200", description = "Usuario Logado")
    @ApiResponse(responseCode = "401", description = "Credenciais Invalidas")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.loginUsuario(usuarioDTO);
    }


    @GetMapping
    @Operation(summary = "Buscar dados de  Usuarios por Email", description = "Buscar dados do Usuario")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario não encontrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email, token));

    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar Usuarios por Id", description = "Deleta Usuario")
    @ApiResponse(responseCode = "200", description = "Usuario Deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email,
                                                      @RequestHeader("Authorization") String token) {
        usuarioService.deletaUsuarioPorEmail(email, token);
        return ResponseEntity.ok().build();

    }

    @PutMapping
    @Operation(summary = "Atualiza dados de Usuarios", description = "Atualiza dados de Usuario")
    @ApiResponse(responseCode = "200", description = "Dados Atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(@RequestBody UsuarioDTO dto,
                                                           @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(usuarioService.atualizaDadosdeUsuario(token, dto));

    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualiza Endereço", description = "Atualiza Endereço")
    @ApiResponse(responseCode = "200", description = "Endereço Atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<EnderecoDTO> atualizaEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestParam("id") Long id,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto, token));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualiza Telefone", description = "Atualiza Telefone")
    @ApiResponse(responseCode = "200", description = "Telefone Atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<TelefoneDTO> atualizaTelefone(@RequestBody TelefoneDTO dto,
                                                        @RequestParam("id") Long id,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto, token));

    }

    @PostMapping("/endereco")
    @Operation(summary = "Salva Endereço", description = "Salva Endereço")
    @ApiResponse(responseCode = "200", description = "Endereço Salvo com sucesso")
    @ApiResponse(responseCode = "401", description = "Endereço ja cadastrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));

    }

    @PostMapping("/telefone")
    @Operation(summary = "Salva Telefone", description = "Salva Telefone")
    @ApiResponse(responseCode = "200", description = "Telefone Salvo com sucesso")
    @ApiResponse(responseCode = "401", description = "Telefone ja cadastrado")
    @ApiResponse(responseCode = "500", description = "erro no Servidor")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO dto,
                                                         @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }
}
