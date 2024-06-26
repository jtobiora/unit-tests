package com.swiftfingers.mockitotests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftfingers.mockitotests.entity.Post;
import com.swiftfingers.mockitotests.entity.Topic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@WithMockUser
class TopicControllerIntegrationTest{

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    final String topicsEndpoint = "/api/v1/topics";

    @Test
    @Order(1)
    void whenPostTopic_thenReturnStatusCreatedAndTopic() throws Exception {
        Topic topic = new Topic("Topic");
        mvc.perform(post(topicsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topic))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Topic"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.updated").isNotEmpty());
    }

    @Test
    @Order(2)
    void whenListTopic_thenReturnStatusOkAndTopicList() throws Exception {
        mvc.perform(get(topicsEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @Order(3)
    void whenPutTopic_thenReturnStatusOkAndUpdatedTopic() throws Exception {
        Topic updatedTopic = Topic.builder().topicId(1L).title("Updated Topic").build();
        mvc.perform(put(topicsEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTopic))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.topicId").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Topic"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.updated").isNotEmpty());
    }

    @Test
    @Order(4)
    void whenGetTopic_thenReturnStatusOkAndTopic() throws Exception {
        mvc.perform(get(topicsEndpoint + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.topicId").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Topic"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.updated").isNotEmpty());
    }

    @Test
    @Order(5)
    void givenTopicIdDoesNotExist_whenGetTopic_thenReturn404NotFound() throws Exception {
        mvc.perform(get(topicsEndpoint + "/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    void givenTopicIdDoesNotExist_whenPutTopic_thenReturn404NotFound() throws Exception {
        Topic updatedTopic = Topic.builder().topicId(100L).title("Updated Topic").build();
        mvc.perform(put(topicsEndpoint + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTopic))
                        .with(csrf()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Order(7)
    void givenTopicIdDoesNotExist_whenDeleteTopic_thenReturn404NotFound() throws Exception {
        mvc.perform(delete(topicsEndpoint + "/100").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void givenTopicHasPosts_whenDeleteTopic_thenReturnStatusOk_andAccompanyingPostsAreDeleted() throws Exception {
        // given
        String topicJson = mvc.perform(get(topicsEndpoint + "/1")).andReturn().getResponse().getContentAsString();
        Topic topic = objectMapper.readValue(topicJson, Topic.class);
        topic.setPosts(null);
        Post post = Post.builder().text("New Post").topic(topic).build();
        String postJson = objectMapper.writeValueAsString(post);
        mvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
                        .with(csrf()))
                .andExpect(status().isCreated());

        // when
        mvc.perform(delete(topicsEndpoint + "/1").with(csrf()))
                .andExpect(status().isOk());

        // then
        mvc.perform(get("/api/v1/posts/1")).andExpect(status().isNotFound());
    }

}