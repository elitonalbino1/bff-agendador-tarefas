package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.*;
import com.javanauta.bffagendador.business.dto.out.*;
import com.javanauta.bffagendador.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient client;

    public UsuarioDTOResponse salvaUsuario(UsuarioDTORequest usuarioDTO) {
        log.info("Salvando novo usuário: {}", usuarioDTO.getEmail());
        return client.salvarUsuario(usuarioDTO);
    }

    public String loginUsuario(LoginRequestDTO usuarioDTO) {
        log.info("Realizando login do usuário: {}", usuarioDTO.getEmail());
        // ✅ CORRIGIDO: extrai o token do Map retornado
        Map<String, String> response = client.login(usuarioDTO);
        return response.get("token");
    }

    public UsuarioDTOResponse buscarUsuarioPorEmail(String email, String token) {
        log.info("Buscando usuário por email: {}", email);
        return client.buscaUsuarioPorEmail(email, token);
    }

    public void deletaUsuarioPorEmail(String email, String token) {
        log.info("Deletando usuário: {}", email);
        client.deletaUsuarioPorEmail(email, token);
    }

    public UsuarioDTOResponse atualizaDadosdeUsuario(String token, UsuarioDTORequest dto) {
        log.info("Atualizando dados do usuário");
        return client.atualizaDadosUsuario(dto, token);
    }

    public EnderecoDTOResponse atualizaEndereco(Long idEndereco, EnderecoDTORequest dto, String token) {
        log.info("Atualizando endereço ID: {}", idEndereco);
        return client.atualizaEndereco(dto, idEndereco, token);
    }

    public TelefoneDTOResponse atualizaTelefone(Long idTelefone, TelefoneDTORequest dto, String token) {
        log.info("Atualizando telefone ID: {}", idTelefone);
        return client.atualizaTelefone(dto, idTelefone, token);
    }

    public EnderecoDTOResponse cadastraEndereco(String token, EnderecoDTORequest dto) {
        log.info("Cadastrando novo endereço");
        return client.cadastraEndereco(dto, token);
    }

    public TelefoneDTOResponse cadastraTelefone(String token, TelefoneDTORequest dto) {
        log.info("Cadastrando novo telefone");
        return client.cadastraTelefone(dto, token);
    }
}