package ru.ism.myblogbackapp.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.ism.myblogbackapp.config.BaseConfig;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.ImageRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig({TestContainerConfig.class, BaseConfig.class, ImageRepoImpl.class, PostRepoImpl.class, TagRepoImpl.class})
class ImageRepoImplTest {

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