package com.ferraz.codando_a_vida_backend.infra.security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionMessageDTO> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO("Credenciais inválidas"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionMessageDTO> handleNoSuchElement(NoSuchElementException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO(exception.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionMessageDTO> handleConstraintViolation(DisabledException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO(exception.getMessage()));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionMessageDTO> handleLocked(LockedException exception) {
        log.error(exception.getMessage(), exception);
        String message = "Sua conta ainda está bloqueada. Favor confirmá-la pelo link enviado ao seu email.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO(message));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionMessageDTO> handleConstraintViolation(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionMessageDTO> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionMessageDTO("Acesso negado"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionMessageDTO> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(new ExceptionMessageDTO("Credenciais inválidas"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessageDTO> tratarErro500(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionMessageDTO(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<ValidationExceptionDataDTO>> tratarErroValidacao(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ex.getValidationExceptionList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationExceptionDataDTO>> handleError400(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);

        List<FieldError> errors = exception.getFieldErrors();
        List<ValidationExceptionDataDTO> errorsDTO = errors.stream().map(ValidationExceptionDataDTO::new).toList();
        return ResponseEntity.badRequest().body(errorsDTO);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessageDTO> tratarErro400(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ExceptionMessageDTO(ex.getMessage()));
    }

}
