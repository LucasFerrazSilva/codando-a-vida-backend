package com.ferraz.codando_a_vida_backend.util;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.infra.security.dto.AuthenticationDTO;
import com.ferraz.codando_a_vida_backend.infra.security.dto.TokenDTO;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class LoginUtil {

    public static String login(MockMvc mvc, UserRepository userRepository,
                        JacksonTester<AuthenticationDTO> authenticationDTOJacksonTester,
                        JacksonTester<TokenDTO> tokenDTOJacksonTester) throws Exception {

        User user = UserUtil.createValidUser(userRepository);
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(user.getEmail(), UserUtil.defaultPassword);
        String body = authenticationDTOJacksonTester.write(authenticationDTO).getJson();
        MockHttpServletRequestBuilder request = TestsUtil.buildPostRequest("/login", body);
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
        TokenDTO tokenDTO = tokenDTOJacksonTester.parse(response.getContentAsString()).getObject();
        return tokenDTO.token();
    }

}
