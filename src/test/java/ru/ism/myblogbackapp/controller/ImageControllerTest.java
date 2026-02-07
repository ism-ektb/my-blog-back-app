package ru.ism.myblogbackapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import ru.ism.myblogbackapp.config.BaseConfig;
import ru.ism.myblogbackapp.config.WebConfig;
import ru.ism.myblogbackapp.service.PostsService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@SpringJUnitWebConfig(classes = WebConfig.class)
@WebAppConfiguration
class ImageControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @MockitoBean
    private PostsService postsService;
    @MockitoBean
    private BaseConfig baseConfig;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).build();
           }

    @Test
    void uploadAndDownloadAvatar_success() throws Exception {
        byte[] pngStub = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8};
        MockMultipartFile file = new MockMultipartFile("image", "avatar.jpeg", "image/jpg", pngStub);
        doNothing().when(postsService).uploadImage(anyLong(), any());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/posts/1/image");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(file))
                .andExpect(status().isCreated());
    }
}