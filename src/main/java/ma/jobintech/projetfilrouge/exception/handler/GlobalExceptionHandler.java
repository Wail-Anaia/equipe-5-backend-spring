package ma.jobintech.projetfilrouge.exception.handler;

import lombok.extern.slf4j.Slf4j;
import ma.jobintech.projetfilrouge.exception.types.BusinessException;
import ma.jobintech.projetfilrouge.exception.types.UserNotFoundException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 400 — Validation ──────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> details = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalide",
                (a, b) -> a
            ));
        return error(HttpStatus.BAD_REQUEST, "Erreur de validation", details);
    }

    // ── 400 — Business ────────────────────────────────
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    // ── 401 — Auth ────────────────────────────────────
    @ExceptionHandler({ BadCredentialsException.class, DisabledException.class })
    public ResponseEntity<Map<String, Object>> handleAuth(RuntimeException ex) {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    // ── 403 — Access denied ───────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return error(HttpStatus.FORBIDDEN, "Accès refusé", null);
    }

    // ── 404 — Not found ───────────────────────────────
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // ── 500 — Fallback ────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue", null);
    }

    // ── Builder ───────────────────────────────────────
    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message,
                                                       Map<String, String> details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    status.value());
        body.put("error",     message);
        if (details != null) body.put("details", details);
        return ResponseEntity.status(status).body(body);
    }
}