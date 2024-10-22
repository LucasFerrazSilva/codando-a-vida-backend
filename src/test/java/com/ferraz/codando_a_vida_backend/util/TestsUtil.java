package com.ferraz.codando_a_vida_backend.util;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestsUtil {

    // Requests //

    public static MockHttpServletRequestBuilder buildPostRequest(String url, String body) {
        return MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(body);
    }

    public static MockHttpServletRequestBuilder buildPostRequest(String url, String body, String token) {
        return MockMvcRequestBuilders
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(body);
    }

    public static MockHttpServletRequestBuilder buildPutRequest(String url, String body, String token) {
        return MockMvcRequestBuilders
                .put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(body);
    }

    public static MockHttpServletRequestBuilder buildGetRequest(String url, String token) {
        return MockMvcRequestBuilders
                .get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);
    }

    public static MockHttpServletRequestBuilder buildDeleteRequest(String url, String token) {
        return MockMvcRequestBuilders
                .delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);
    }


    // Responses //

    public static void validateHttpStatusAndResponseBodyIsNotBlank(
            MockHttpServletResponse response, int httpStatus) throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(httpStatus);
        assertThat(response.getContentAsString()).isNotBlank();
    }

    public static void validateHttpStatusAndResponseBody(
            MockHttpServletResponse response, int httpStatus, String expectedBody) throws UnsupportedEncodingException {

        assertThat(response.getStatus()).isEqualTo(httpStatus);
        assertThat(response.getContentAsString()).isEqualTo(expectedBody);
    }

    public static void validateHttpStatusAndResponseBodyContains(
            MockHttpServletResponse response, int httpStatus, String keyInResponseBody) throws UnsupportedEncodingException {

        assertThat(response.getStatus()).isEqualTo(httpStatus);
        assertThat(response.getContentAsString()).contains(keyInResponseBody);
    }

}
