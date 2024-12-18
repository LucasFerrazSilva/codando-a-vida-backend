package com.ferraz.codando_a_vida_backend.infra.security.exception;

import com.ferraz.codando_a_vida_backend.infra.exception.ExceptionMessageDTO;
import com.ferraz.codando_a_vida_backend.infra.exception.ExceptionsHandler;
import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;
import com.ferraz.codando_a_vida_backend.infra.exception.ValidationExceptionDataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsHandlerTest {

    private ExceptionsHandler exceptionsHandler = new ExceptionsHandler();

    @Test
    void handleBadCredentials() {
        //Given

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.handleBadCredentials();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Credenciais inválidas");
    }

    @Test
    void handleNoSuchElement() {
        //Given
        NoSuchElementException exception = new NoSuchElementException("message");

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.handleNoSuchElement(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
    }

    @Test
    void handleConstraintViolation() {
        //Given
        DisabledException exception = new DisabledException("message");

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.handleConstraintViolation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
    }

    @Test
    void testHandleConstraintViolation() {
        //Given
        LockedException exception = new LockedException("message");

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.handleLocked(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Sua conta ainda está bloqueada. Favor confirmá-la pelo link enviado ao seu email.");
    }

    @Test
    void testHandleConstraintViolation1() {
        //Given
        SQLIntegrityConstraintViolationException exception = new SQLIntegrityConstraintViolationException("message");

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.handleConstraintViolation(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
    }

    @Test
    void tratarErroAcessoNegado() {
        //Given

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.tratarErroAcessoNegado();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Acesso negado");
    }

    @Test
    void tratarErroAuthentication() {
        //Given

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.tratarErroAuthentication();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Credenciais inválidas");
    }

    @Test
    void tratarErro500() {
        //Given
        Exception exception = new Exception("message");

        // When
        ResponseEntity<ExceptionMessageDTO> response = exceptionsHandler.tratarErro500(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo(exception.getMessage());
    }

    @Test
    void tratarErroValidacao() {
        //Given
        ValidationException exception = new ValidationException("campo", "message");

        // When
        ResponseEntity<List<ValidationExceptionDataDTO>> response = exceptionsHandler.tratarErroValidacao(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst().field()).isEqualTo(exception.getValidationExceptionList().get(0).field());
        assertThat(response.getBody().getFirst().message()).isEqualTo(exception.getValidationExceptionList().get(0).message());
    }

}