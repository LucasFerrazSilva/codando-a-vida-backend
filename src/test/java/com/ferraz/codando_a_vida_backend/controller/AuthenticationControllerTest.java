package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<RegisterDTO> registerDTOJacksonTester;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Deve retornar 201 quando chamar /register passando dados validos")
    void testValidRegister() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO("Teste", "teste@mail.com", "1234", "1234");
        String body = registerDTOJacksonTester.write(registerDTO).getJson();
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(body);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isNotBlank();
        assertThat(repository.findByEmail(registerDTO.email())).isNotNull();
    }

    // Register - Erro

    // Login - Sucesso

    // Login - Erro

}