package com.swiftfingers.mockitotests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftfingers.mockitotests.entity.Topic;
import com.swiftfingers.mockitotests.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PostController.class)
//@ContextConfiguration(classes = { ForumSecurityConfiguration.class, PostController.class })
@WithMockUser
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    final String api = "/api/v1/posts";

    @Test
    void givenPostDoesNotExists_whenGetPost_thenReturn404NotFound() throws Exception {
        given(postService.get(1L)).willThrow(IllegalArgumentException.class);
        mockMvc.perform(get(api + "/1"))
                .andExpect(status().isNotFound());

        verify(postService).get(1L);
    }

    @Test
    void givenPostDoesNotExists_whenPutPost_thenReturn404NotFound() throws Exception {
        given(postService.update(any())).willThrow(NoSuchElementException.class);
        Topic topic = new Topic("topic");
        mockMvc.perform(
                        put(api)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(topic))
                                .with(csrf()))
                .andExpect(status().isNotFound());
        verify(postService, times(1)).update(any());
    }

    @Test
    void givenPostDoesNotExists_whenDeletePost_thenReturn404NotFound() throws Exception {
        doThrow(NoSuchElementException.class).when(postService).delete(1L);
        mockMvc.perform(delete(api + "/1").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
