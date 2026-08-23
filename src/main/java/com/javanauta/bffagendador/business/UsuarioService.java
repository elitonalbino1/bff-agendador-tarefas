package com.javanauta.bffagendador.business;

import com.javanauta.bffagendador.business.dto.in.EnderecoDTORequest;
import com.javanauta.bffagendador.business.dto.in.LoginRequestDTO;
import com.javanauta.bffagendador.business.dto.in.TelefoneDTORequest;
import com.javanauta.bffagendador.business.dto.in.UsuarioDTORequest;
import com.javanauta.bffagendador.business.dto.out.EnderecoDTOResponse;
import com.javanauta.bffagendador.business.dto.out.TelefoneDTOResponse;
import com.javanauta.bffagendador.business.dto.out.UsuarioDTOResponse;
import com.javanauta.bffagendador.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioClient client;

    public UsuarioDTOResponse salvaUsuario(UsuarioDTORequest usuarioDTO) {
        log.info("Salvando novo usuário: {}", usuarioDTO.getEmail());
        UsuarioDTOResponse resultado = client.salvarUsuario(usuarioDTO);
        log.info("Usuário salvo com sucesso");
        return resultado;
    }

    public String loginUsuario(LoginRequestDTO usuarioDTO) {
        log.info("Realizando login do usuário: {}", usuarioDTO.getEmail());
        String token = client.login(usuarioDTO);
        log.info("Login realizado com sucesso");
        return token;
    }

    public UsuarioDTOResponse buscarUsuarioPorEmail(String email, String token) {
        log.info("Buscando usuário por email: {}", email);
        UsuarioDTOResponse usuario = client.buscaUsuarioPorEmail(email, token);
        log.info("Usuário encontrado");
        return usuario;
    }

    public void deletaUsuarioPorEmail(String email, String token) {
        log.info("Deletando usuário: {}", email);
        client.deletaUsuarioPorEmail(email, token);
        log.info("Usuário deletado com sucesso");
    }

    public UsuarioDTOResponse atualizaDadosdeUsuario(String token, UsuarioDTORequest dto) {
        log.info("Atualizando dados do usuário: {}", dto.getEmail());
        UsuarioDTOResponse resultado = client.atualizaDadosUsuario(dto, token);
        log.info("Dados atualizados com sucesso");
        return resultado;
    }

    public EnderecoDTOResponse atualizaEndereco(Long idEndereco, EnderecoDTORequest enderecoDTO, String token) {
        log.info("Atualizando endereço ID: {}", idEndereco);
        EnderecoDTOResponse resultado = client.atualizaEndereco(enderecoDTO, idEndereco, token);
        log.info("Endereço atualizado com sucesso");
        return resultado;
    }

    public TelefoneDTOResponse atualizaTelefone(Long idTelefone, TelefoneDTORequest dto, String token) {
        log.info("Atualizando telefone ID: {}", idTelefone);
        TelefoneDTOResponse resultado = client.atualizaTelefone(dto, idTelefone, token);
        log.info("Telefone atualizado com sucesso");
        return resultado;
    }

    public EnderecoDTOResponse cadastraEndereco(String token, EnderecoDTORequest dto) {
        log.info("Cadastrando novo endereço");
        EnderecoDTOResponse resultado = client.cadastraEndereco(dto, token);
        log.info("Endereço cadastrado com sucesso");
        return resultado;
    }

    public TelefoneDTOResponse cadastraTelefone(String token, TelefoneDTORequest dto) {
        log.info("Cadastrando novo telefone");
        TelefoneDTOResponse resultado = client.cadastraTelefone(dto, token);
        log.info("Telefone cadastrado com sucesso");
        return resultado;
    }
}
