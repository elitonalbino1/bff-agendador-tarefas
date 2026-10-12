package com.javanauta.bffagendador.infrastructure.client.config;

import com.javanauta.bffagendador.infrastructure.exceptions.*;
import com.javanauta.bffagendador.infrastructure.exceptions.IllegalArgumentException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeingError implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        return switch (status) {
            case 400 -> {
                // Se for erro de CEP, usa IllegalArgumentException
                if (methodKey != null && methodKey.toLowerCase().contains("cep")) {
                    yield new IllegalArgumentException("Erro ao buscar CEP");
                }
                // Senão, usa BusinessException
                yield new BusinessException("Dados inválidos enviados ao serviço");
            }
            case 401 -> new UnauthorizedException("Não autorizado. Token inválido ou expirado");
            case 403 -> new BusinessException("Acesso proibido. Sem permissão para este recurso");
            case 404 -> new ResourceNotFoundException("Recurso não encontrado: " + methodKey);
            case 409 -> new ConflictException("Conflito: recurso já existente");
            case 500, 502, 503 -> new BusinessException("Erro interno no serviço: " + status);
            default -> new BusinessException("Erro inesperado do serviço: " + status + " - " + methodKey);
        };
    }
}