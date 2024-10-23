package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.post.Post;
import com.ferraz.codando_a_vida_backend.domain.post.PostRepository;
import com.ferraz.codando_a_vida_backend.domain.post.dto.NewPostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.PostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.UpdatePostDTO;
import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.infra.security.dto.AuthenticationDTO;
import com.ferraz.codando_a_vida_backend.infra.security.dto.TokenDTO;
import com.ferraz.codando_a_vida_backend.util.CategoryUtil;
import com.ferraz.codando_a_vida_backend.util.LoginUtil;
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

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.ferraz.codando_a_vida_backend.util.TestsUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JacksonTester<NewPostDTO> newPostDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdatePostDTO> updatePostDTOJacksonTester;

    @Autowired
    private JacksonTester<AuthenticationDTO> authenticationDTOJacksonTester;

    @Autowired
    private JacksonTester<PostDTO> postDTOJacksonTester;

    @Autowired
    private JacksonTester<List<PostDTO>> listPostDTOJacksonTester;

    @Autowired
    private JacksonTester<TokenDTO> tokenDTOJacksonTester;

    private String token;

    private final String path = "/post";

    private Category category;


    @BeforeAll
    void beforeAll() throws Exception {
        this.token = LoginUtil.login(mvc, userRepository, authenticationDTOJacksonTester, tokenDTOJacksonTester);
        repository.deleteAll();
        User user = UserUtil.createValidUser(userRepository);
        category = CategoryUtil.createValidCategory(user, categoryRepository);
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    // Create //
    @Test
    @DisplayName("Deve criar quando chamar /post via POST passando dados válidos")
    void testValidCreate() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.CREATED.value());
        PostDTO responseDTO = postDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.path()).isEqualTo(postPath);
        assertThat(responseDTO.title()).isEqualTo(title);
        assertThat(responseDTO.body()).isEqualTo(body);
        assertThat(responseDTO.categoryDTO().id()).isEqualTo(category.getId());

        Optional<Post> postOptional = repository.findById(responseDTO.id());
        assertThat(postOptional).isPresent();
        Post post = postOptional.get();
        assertThat(post.getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(post.getCreateDate()).isNotNull();
        assertThat(post.getCreateUser()).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via POST passando dados vazios")
    void testInvalidCreate_EmptyData() throws Exception {
        // Given
        String postPath = null;
        String title = null;
        String body = null;
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via POST passando categoria invalida")
    void testInvalidCreate_InvalidCategory() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        Category invalidCategory = new Category();
        invalidCategory.setId(99999);
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, invalidCategory);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve criar duas categorias com sucesso")
    void testValidCreate_TwoTimes() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, category);
        mvc.perform(request).andReturn().getResponse();

        postPath += 2;
        title += 2;
        body += 2;
        request = buildCreateRequest(postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via POST passando path duplicado")
    void testInvalidCreate_DuplicatedPath() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, category);
        mvc.perform(request).andReturn().getResponse();

        title += 2;
        body += 2;
        request = buildCreateRequest(postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via POST passando titulo duplicado")
    void testInvalidCreate_DuplicatedTitle() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildCreateRequest(postPath, title, body, category);
        mvc.perform(request).andReturn().getResponse();

        postPath += 2;
        body += 2;
        request = buildCreateRequest(postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    // Read //
    @Test
    @DisplayName("Deve retornar lista quando chamar /post via GET")
    void testValidList() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        PostDTO post2 = createRandomPost();
        MockHttpServletRequestBuilder request = buildGetRequest(path, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.OK.value());
        List<PostDTO> list = listPostDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(list).hasSize(2).contains(post1, post2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando chamar /post via GET")
    void testValidList_EmptyList() throws Exception {
        // Given
        MockHttpServletRequestBuilder request = buildGetRequest(path, token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.OK.value());
        List<PostDTO> list = listPostDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(list).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar post quando chamar /post via GET passando id valido")
    void testValidFindById() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        MockHttpServletRequestBuilder request = buildGetRequest(path + "/" + post1.id(), token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.OK.value());
        PostDTO responseDTO = postDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO).isEqualTo(post1);
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via GET passando id invalido")
    void testInvalidFindById_InvalidId() throws Exception {
        // Given
        createRandomPost();
        MockHttpServletRequestBuilder request = buildGetRequest(path + "/999999", token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.NOT_FOUND.value());
    }

    // Update //
    @Test
    @DisplayName("Deve atualizar quando chamar /post via PUT passando id valido e dados validos")
    void testValidUpdate() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();

        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildUpdateRequest(post1.id(), postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.OK.value());
        PostDTO responseDTO = postDTOJacksonTester.parse(response.getContentAsString()).getObject();
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.path()).isEqualTo(postPath);
        assertThat(responseDTO.title()).isEqualTo(title);
        assertThat(responseDTO.body()).isEqualTo(body);
        assertThat(responseDTO.categoryDTO().id()).isEqualTo(category.getId());

        Optional<Post> postOptional = repository.findById(responseDTO.id());
        assertThat(postOptional).isPresent();
        Post post = postOptional.get();
        assertThat(post.getUpdateDate()).isNotNull();
        assertThat(post.getUpdateUser()).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via PUT passando id valido e dados invalidos")
    void testInvalidUpdate_EmptyData() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        MockHttpServletRequestBuilder request = buildUpdateRequest(post1.id(), null, null, null, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via PUT passando id invalido e dados validos")
    void testInvalidUpdate_InvalidId() throws Exception {
        // Given
        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildUpdateRequest(new Random().nextInt(10000), postPath, title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via PUT passando id valido e categoria invalida")
    void testInvalidUpdate_InvalidCategory() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();

        Category invalidCategory = new Category();
        invalidCategory.setId(new Random().nextInt(10000));

        String postPath = "path";
        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildUpdateRequest(post1.id(), postPath, title, body, invalidCategory);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via PUT passando id valido e path duplicado")
    void testInvalidUpdate_DuplicatedPath() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        PostDTO post2 = createRandomPost();

        String title = "Title";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildUpdateRequest(post1.id(), post2.path(), title, body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via PUT passando id valido e title duplicado")
    void testInvalidUpdate_DuplicatedTitle() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        PostDTO post2 = createRandomPost();

        String postPath = "path";
        String body = "Body";
        MockHttpServletRequestBuilder request = buildUpdateRequest(post1.id(), postPath, post2.title(), body, category);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        validateHttpStatusAndResponseBodyIsNotBlank(response, HttpStatus.BAD_REQUEST.value());
    }

    // Delete //
    @Test
    @DisplayName("Deve inativar quando chamar /post via DELETE passando id valido")
    void testValidDelete() throws Exception {
        // Given
        PostDTO post1 = createRandomPost();
        MockHttpServletRequestBuilder request = buildDeleteRequest(path + "/" + post1.id(), token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Optional<Post> postOptional = repository.findById(post1.id());
        assertThat(postOptional).isPresent();
        Post post = postOptional.get();
        assertThat(post.getStatus()).isEqualTo(EntityStatus.INACTIVE);
        assertThat(post.getUpdateDate()).isNotNull();
        assertThat(post.getUpdateUser()).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar erro quando chamar /post via DELETE passando id invalido")
    void testInvalidDelete_InvalidId() throws Exception {
        // Given
        MockHttpServletRequestBuilder request = buildDeleteRequest(path + "/" + new Random().nextInt(1000000), token);

        // When
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }



    private PostDTO createRandomPost() throws Exception {
        int rand = new Random().nextInt(1000000);
        MockHttpServletRequestBuilder request = buildCreateRequest("path" + rand, "Title" + rand, "Body" + rand, category);
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
        return postDTOJacksonTester.parse(response.getContentAsString()).getObject();
    }

    private MockHttpServletRequestBuilder buildCreateRequest(String postPath, String title, String body, Category category) throws Exception {
        NewPostDTO dto = new NewPostDTO(postPath, title, body, category);
        String requestBody = newPostDTOJacksonTester.write(dto).getJson();
        return buildPostRequest(path, requestBody, token);
    }

    private MockHttpServletRequestBuilder buildUpdateRequest(Integer id, String postPath, String title, String body,
                                                             Category category) throws Exception {
        UpdatePostDTO dto = new UpdatePostDTO(postPath, title, body, category);
        String requestBody = updatePostDTOJacksonTester.write(dto).getJson();
        return buildPutRequest(path + "/" + id, requestBody, token);
    }

}