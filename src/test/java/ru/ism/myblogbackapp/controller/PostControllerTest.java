package ru.ism.myblogbackapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.ism.myblogbackapp.config.BaseConfig;
import ru.ism.myblogbackapp.config.WebConfig;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.exception.ValidationBaseException;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;
import ru.ism.myblogbackapp.service.PostsService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringJUnitWebConfig(classes = WebConfig.class)
@WebAppConfiguration
@RequiredArgsConstructor
class PostControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @MockitoBean
    private PostsService postsService;
    @MockitoBean
    private BaseConfig baseConfig;
    private final ObjectMapper mapper = new ObjectMapper();
    private PostOutDto postOutDto;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
        postOutDto = new PostOutDto(1, "title",
                "new text", List.of("tag"), 1, 1);
    }

    @Test
    @SneakyThrows
    void searchPosts_returnOk() {
        when(postsService.searchPosts(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PostsOutDto(List.of(postOutDto), true, true, 1));

        mockMvc.perform(get("/api/posts")
                        .param("search", "title")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getPostById_returnOk() {
        when(postsService.getPost(anyLong()))
                .thenReturn(postOutDto);
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    void getPostById_noFoundPost() {
        when(postsService.getPost(anyLong()))
                .thenThrow(new NoFoundException("пост не найден"));
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void createPost_returnOk() {
        PostDtoIn postDtoIn = new PostDtoIn("title", "new text", List.of("tag"));
        when(postsService.createPost(any()))
                .thenReturn(postOutDto);
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(postDtoIn)))
                .andExpect(status().isCreated());

    }

    @Test
    @SneakyThrows
    void updatePost_returnOk() {
        PostDtoUpdate postDtoUpdate = new PostDtoUpdate(1, "title", "new text", List.of("tag"));
        when(postsService.updatePost(anyLong(), any())).thenReturn(postOutDto);
        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(postDtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    void updatePost_return404() {
        PostDtoUpdate postDtoUpdate = new PostDtoUpdate(1, "title", "new text", List.of("tag"));
        when(postsService.updatePost(anyLong(), any())).thenThrow(new NoFoundException("no found"));
        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(postDtoUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void updatePost_return405() {
        PostDtoUpdate postDtoUpdate = new PostDtoUpdate(1, "title", "new text", List.of("tag"));
        when(postsService.updatePost(anyLong(), any())).thenThrow(new ValidationBaseException("Bad request"));
        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(postDtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void deletePostById() {
        doNothing().when(postsService).deletePost(Mockito.anyLong());
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }
}