package com.javanauta.bffagendador.infrastructure.exceptions;

import feign.FeignException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ✅ IMPORT EXPLÍCITO DA SUA CLASSE
import com.javanauta.bffagendador.infrastructure.exceptions.IllegalArgumentException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ========== MÉTODOS AUXILIARES ==========

    private Map<String, Object> buildError(String message, HttpStatus status, String path) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        if (path != null) {
            error.put("path", path);
        }
        return error;
    }

    private Map<String, Object> buildError(String message, HttpStatus status) {
        return buildError(message, status, null);
    }

    private String extractServiceName(String url) {
        if (url == null) return "unknown-service";
        if (url.contains("8080") || url.contains("usuario")) return "usuario-service";
        if (url.contains("8081") || url.contains("tarefas")) return "agendador-tarefas-service";
        if (url.contains("8082") || url.contains("email")) return "notificacao-service";
        return "unknown-service";
    }

    private String getMessageFromFeignException(FeignException ex, String serviceName) {
        return switch (ex.status()) {
            case 400 -> "Dados inválidos enviados ao " + serviceName;
            case 401 -> "Não autorizado. Token inválido ou expirado";
            case 404 -> "Recurso não encontrado no " + serviceName;
            case 409 -> "Conflito de dados no " + serviceName;
            case 500 -> "Erro interno no " + serviceName;
            case 503 -> serviceName + " temporariamente indisponível";
            default -> "Erro ao comunicar com " + serviceName + ": " + ex.getMessage();
        };
    }

    // ========== HANDLERS DE EXCEÇÕES CUSTOMIZADAS (do ErrorDecoder) ==========

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(
            ConflictException ex, HttpServletRequest request) {
        log.warn("Conflito detectado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(ex.getMessage(), HttpStatus.CONFLICT, request.getRequestURI()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            UnauthorizedException ex, HttpServletRequest request) {
        log.warn("Não autorizado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(
            BusinessException ex, HttpServletRequest request) {
        log.error("Erro de negócio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    // ========== HANDLERS DE EXCEÇÕES DO FEIGN ==========

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(
            FeignException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null || status.is5xxServerError()) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        // Proteção contra NPE
        String serviceName = "unknown-service";
        if (ex.request() != null && ex.request().url() != null) {
            serviceName = extractServiceName(ex.request().url());
        }

        String message = getMessageFromFeignException(ex, serviceName);

        log.error("Erro ao comunicar com {}: {} - {}", serviceName, ex.status(), ex.getMessage());

        Map<String, Object> error = buildError(message, status, request.getRequestURI());
        error.put("serviceName", serviceName);

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({RetryableException.class, SocketTimeoutException.class})
    public ResponseEntity<Map<String, Object>> handleTimeout(
            Exception ex, HttpServletRequest request) {

        log.error("Timeout na comunicação: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(buildError("O serviço demorou muito para responder. Tente novamente.",
                        HttpStatus.REQUEST_TIMEOUT, request.getRequestURI()));
    }

    // ========== HANDLERS DE VALIDAÇÃO ==========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        log.warn("Erros de validação: {}", erros);

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Erro de Validação");
        error.put("message", "Dados inválidos");
        error.put("errors", erros);
        error.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ✅ AGORA PEGA SUA IllegalArgumentException CUSTOMIZADA
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Argumento ilegal: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI()));
    }

    // ========== HANDLER GENÉRICO (ÚLTIMA LINHA DE DEFESA) ==========

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex, HttpServletRequest request) {

        log.error("Erro não tratado: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Erro interno: " + ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI()));
    }
}