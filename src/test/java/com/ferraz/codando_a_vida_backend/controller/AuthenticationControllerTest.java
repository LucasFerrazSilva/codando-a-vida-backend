package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.infra.security.dto.AuthenticationDTO;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import com.ferraz.codando_a_vida_backend.util.UserUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;

import static com.ferraz.codando_a_vida_backend.util.TestsUtil.*;
import static com.ferraz.codando_a_vida_backend.util.UserUtil.createValidUser;
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
    private JacksonTester<AuthenticationDTO> authenticationDTOJacksonTester;

    @Autowired
    private UserRepository repository;

    @BeforeAll
    void beforeAll() {
        repository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 201 quando chamar /register passando dados validos")
    void testValidRegister() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO("Teste", "teste@mail.com", "1234", "1234");
        MockHttpServletRequestBuilder request = buildRegisterRequest(registerDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.CREATED.value());
        assertThat(repository.findByEmail(registerDTO.email())).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar 400 quando chamar /register passando dados invalidos")
    void testInvalidRegister_EmptyData() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(null, null, null, null);
        MockHttpServletRequestBuilder request = buildRegisterRequest(registerDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 quando chamar /register passando email ja cadastrado")
    void testInvalidRegister_EmailAlreadyInUse() throws Exception {
        // Given
        User user = createValidUser(repository);

        RegisterDTO registerDTO = new RegisterDTO("Teste", user.getEmail(), "1234", "1234");
        MockHttpServletRequestBuilder request = buildRegisterRequest(registerDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 quando chamar /register passando senhas diferentes")
    void testInvalidRegister_PasswordsDontMatch() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO("Teste", "teste@mail.com", "1234", "12345");
        MockHttpServletRequestBuilder request = buildRegisterRequest(registerDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    // Login - Sucesso
    @Test
    @DisplayName("Deve retornar 200 quando chamar /login passando dados validos")
    void testValidLogin() throws Exception {
        // Given
        User user = createValidUser(repository);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(user.getEmail(), UserUtil.defaultPassword);
        MockHttpServletRequestBuilder request = buildLoginRequest(authenticationDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyContains(response, HttpStatus.OK.value(), "token");
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /login passando senha errada")
    void testInvalidLogin_InvalidPassword() throws Exception {
        // Given
        User user = createValidUser(repository);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(user.getEmail(), "Wrong Password");
        MockHttpServletRequestBuilder request = buildLoginRequest(authenticationDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /login passando dados invalidos")
    void testInvalidLogin_InvalidData() throws Exception {
        // Given
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(null, null);
        MockHttpServletRequestBuilder request = buildLoginRequest(authenticationDTO);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }


    private MockHttpServletRequestBuilder buildRegisterRequest(RegisterDTO registerDTO) throws IOException {
        String body = registerDTOJacksonTester.write(registerDTO).getJson();
        return buildPostRequest("/register", body);
    }

    private MockHttpServletRequestBuilder buildLoginRequest(AuthenticationDTO authenticationDTO) throws IOException {
        String body = authenticationDTOJacksonTester.write(authenticationDTO).getJson();
        return buildPostRequest("/login", body);
    }

}