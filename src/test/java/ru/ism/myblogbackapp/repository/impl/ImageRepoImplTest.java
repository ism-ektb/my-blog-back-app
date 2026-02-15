package ru.ism.myblogbackapp.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.ImageRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class ImageRepoImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private PostRepoImpl postRepo;

    @Autowired
    private TagRepoImpl tagRepo;

    @Test
    void addImage_addGet() {
        Post post = postRepo.addPost(Post.builder()
                .title("title")
                .text("text")
                .tags(List.of(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build())).build());
        byte[] bytes = new byte[]{1, 2, 3, 4};
        imageRepo.updateImage(post.getId(), bytes);
        byte[] image = imageRepo.getImage(post.getId());
        assertNotNull(image);
        assertArrayEquals(bytes, image);
    }

    @Test
    void addImage_updateImage_Get() {
        Post post = postRepo.addPost(Post.builder()
                .title("title")
                .text("text")
                .tags(List.of(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build())).build());
        byte[] bytes = new byte[]{1, 2, 3, 4};
        imageRepo.updateImage(post.getId(), bytes);
        byte[] bytesUp = new byte[]{4, 5, 6, 7};
        imageRepo.updateImage(post.getId(), bytesUp);
        byte[] image = imageRepo.getImage(post.getId());
        assertNotNull(image);
        assertArrayEquals(bytesUp, image);
    }

    @Test
    void updateImage_return404() {
        byte[] bytes = new byte[]{1, 2, 3, 4};
        assertThrows(NoFoundException.class, () -> {
            imageRepo.updateImage(500000L, bytes);
        });
    }

    @Test
    void addGet_Return404() {
        Post post = postRepo.addPost(Post.builder()
                .title("title")
                .text("text")
                .tags(List.of(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build())).build());
        assertThrows(NoFoundException.class, () -> {
            imageRepo.getImage(500000L);
        });
        assertThrows(NoFoundException.class, () -> {
            imageRepo.getImage(post.getId());
        });
    }
}