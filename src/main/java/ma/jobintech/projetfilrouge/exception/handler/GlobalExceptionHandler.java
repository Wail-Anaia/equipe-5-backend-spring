package ma.jobintech.projetfilrouge.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ma.jobintech.projetfilrouge.exception.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erreurs de validation Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
        .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
    return errors;
    // Retourne : { "nom": "Nom obligatoire", "email": "Email invalide" }
}

// Erreurs métier (BusinessException)
@ExceptionHandler(BusinessException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Map<String, String> handleBusinessException(BusinessException ex) {
    return Map.of("error", ex.getMessage());
    // Retourne : { "error": "Email déjà utilisé" }
}
}