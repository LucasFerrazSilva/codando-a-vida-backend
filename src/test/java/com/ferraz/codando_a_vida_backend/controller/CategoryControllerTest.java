package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.category.dto.CategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.UpdateCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.infra.security.dto.AuthenticationDTO;
import com.ferraz.codando_a_vida_backend.infra.security.dto.TokenDTO;
import com.ferraz.codando_a_vida_backend.util.LoginUtil;
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

import java.util.List;
import java.util.Random;

import static com.ferraz.codando_a_vida_backend.util.TestsUtil.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JacksonTester<NewCategoryDTO> newCategoryDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateCategoryDTO> updateCategoryDTOJacksonTester;

    @Autowired
    private JacksonTester<AuthenticationDTO> authenticationDTOJacksonTester;

    @Autowired
    private JacksonTester<CategoryDTO> categoryDTOJacksonTester;

    @Autowired
    private JacksonTester<List<CategoryDTO>> listCategoryJacksonTester;

    @Autowired
    private JacksonTester<TokenDTO> tokenDTOJacksonTester;

    private String token;

    private String path = "/category";


    @BeforeAll
    void beforeAll() throws Exception {
        login();
        repository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }


    @Test
    @DisplayName("Deve criar quando chamar /category via POST passando dados válidos")
    void testValidCreate() throws Exception {
        // Given
        String name = "Categoria " + new Random().nextInt(100000);
        MockHttpServletRequestBuilder request = buildCreateRequest(name);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.CREATED.value());
        assertThat(categoryDTOJacksonTester.parse(response.getContentAsString()).getObject()).isNotNull();
        assertThat(repository.findByNameAndStatus(name, EntityStatus.ACTIVE)).isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /category via POST passando dados inválidos")
    void testInvalidCreate() throws Exception {
        // Given
        MockHttpServletRequestBuilder request = buildCreateRequest(null);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
        assertThat(repository.findByNameAndStatus(null, EntityStatus.ACTIVE)).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /category via POST duas vezes passando os mesmos dados")
    void testInvalidCreate_DuplicatedRequest() throws Exception {
        // Given
        String name = "CategoriaRepetida";
        MockHttpServletRequestBuilder request = buildCreateRequest(name);
        mvc.perform(request).andReturn().getResponse();

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
        assertThat(repository.findByNameAndStatus(name, EntityStatus.ACTIVE)).hasSize(1);
    }

    @Test
    @DisplayName("Deve retornar lista de categorias quando chamar /category via GET")
    void testValidList() throws Exception {
        // Given
        CategoryDTO categoryDTO1 = createCategory();
        CategoryDTO categoryDTO2 = createCategory();
        MockHttpServletRequestBuilder request = buildGetRequest(path, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();

        List<CategoryDTO> list = listCategoryJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(list).hasSize(2).contains(categoryDTO1, categoryDTO2);
    }

    @Test
    @DisplayName("Deve retornar categoria quando chamar /category/{id} via GET passando id valido")
    void testValidFindById() throws Exception {
        // Given
        CategoryDTO originalCategoryDTO = createCategory();
        MockHttpServletRequestBuilder request = buildGetRequest(path + "/" + originalCategoryDTO.id(), token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();

        CategoryDTO categoryDTO = categoryDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(categoryDTO).isEqualTo(originalCategoryDTO);
    }

    @Test
    @DisplayName("Deve atualizar categoria quando chamar /category/{id} via PUT passando dados validos")
    void testValidUpdate() throws Exception {
        // Given
        CategoryDTO originalCategoryDTO = createCategory();
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Novo nome");
        String body = updateCategoryDTOJacksonTester.write(updateCategoryDTO).getJson();
        MockHttpServletRequestBuilder request = buildPutRequest(path + "/" + originalCategoryDTO.id(), body, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();

        CategoryDTO categoryDTO = categoryDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(categoryDTO.name()).isEqualTo(updateCategoryDTO.name());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar atualizar categoria chamando /category/{id} via PUT passando ID invalido")
    void testInvalidUpdate_InvalidId() throws Exception {
        // Given
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Novo nome");
        String body = updateCategoryDTOJacksonTester.write(updateCategoryDTO).getJson();
        MockHttpServletRequestBuilder request = buildPutRequest(path + "/9999", body, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar atualizar categoria chamando /category/{id} via PUT passando nome vazio")
    void testInvalidUpdate_InvalidName() throws Exception {
        // Given
        CategoryDTO originalCategoryDTO = createCategory();
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO(null);
        String body = updateCategoryDTOJacksonTester.write(updateCategoryDTO).getJson();
        MockHttpServletRequestBuilder request = buildPutRequest(path + "/" + originalCategoryDTO.id(), body, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

    @Test
    @DisplayName("Deve inativar categoria quando chamar /category/{id} via DELETE passando Id valido")
    void testValidDelete() throws Exception {
        // Given
        CategoryDTO originalCategoryDTO = createCategory();
        MockHttpServletRequestBuilder request = buildDeleteRequest(path + "/" + originalCategoryDTO.id(), token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(repository.findByNameAndStatus(originalCategoryDTO.name(), EntityStatus.ACTIVE)).isEmpty();
        assertThat(repository.findByNameAndStatus(originalCategoryDTO.name(), EntityStatus.INACTIVE)).isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar inativar categoria chamando /category/{id} via DELETE passando Id invalido")
    void testInvalidDelete() throws Exception {
        // Given
        MockHttpServletRequestBuilder request = buildDeleteRequest(path + "/999999", token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    private void login() throws Exception {
        this.token = LoginUtil.login(mvc, userRepository, authenticationDTOJacksonTester, tokenDTOJacksonTester);
    }

    private MockHttpServletRequestBuilder buildCreateRequest(String name) throws Exception {
        NewCategoryDTO newCategoryDTO = new NewCategoryDTO(name);
        String body = newCategoryDTOJacksonTester.write(newCategoryDTO).getJson();
        return buildPostRequest(path, body, token);
    }

    private CategoryDTO createCategory() throws Exception {
        String name = "Categoria " + new Random().nextInt(100000);
        MockHttpServletRequestBuilder request = buildCreateRequest(name);
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
        return categoryDTOJacksonTester.parse(response.getContentAsString()).getObject();
    }

}