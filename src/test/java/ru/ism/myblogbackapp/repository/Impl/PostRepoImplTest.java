package ru.ism.myblogbackapp.repository.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.ism.myblogbackapp.config.BaseConfig;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.PostRepo;
import ru.ism.myblogbackapp.repository.TagRepo;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {TestContainerConfig.class, BaseConfig.class, PostRepoImpl.class, TagRepoImpl.class})
class PostRepoImplTest {

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM blog.posts");
        jdbcTemplate.execute("DELETE FROM blog.tags");
        jdbcTemplate.execute("DELETE FROM blog.posts_tags");
    }


    @Test
    void addPostAndTag_GetPost() {
        Post post = postRepo.addPost(Post.builder()
                .title("title")
                .text("text")
                .tags(List.of(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build())).build());
        Post getPost = postRepo.getPost(post.getId());
        assertNotNull(getPost);
        assertEquals(post.getTitle(), getPost.getTitle());
        assertEquals(post.getText(), getPost.getText());
        assertEquals("tag1", getPost.getTags().get(0).getName());
        assertEquals("tag2", getPost.getTags().get(1).getName());
    }


    @Test
    void updatePost() {
        Post post = postRepo.addPost(Post.builder()
                .title("title")
                .text("text")
                .tags(List.of(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build())).build());
        postRepo.updatePost(Post.builder()
                .id(post.getId())
                .title("title_updated")
                .text("text_updated")
                .tags(List.of(Tag.builder().name("tag_update").build())).build());
        Post getPost = postRepo.getPost(post.getId());
        assertNotNull(getPost);
        assertEquals("title_updated", getPost.getTitle());
        assertEquals("text_updated", getPost.getText());
        assertEquals("tag_update", getPost.getTags().get(0).getName());

    }

    @Test
    void getPosts() {
        Post post1 = postRepo.addPost(Post.builder().tags(List.of(Tag.builder().name("test").build())).text("text").build());
        Post post2 = postRepo.addPost(Post.builder().text("text1").tags(List.of(Tag.builder().name("test").build())).build());
        Post post3 = postRepo.addPost(Post.builder().text("текст").tags(List.of(Tag.builder().name("test").build())).build());
        List<Post> posts = postRepo.getPosts("text", 0, 10);
        assertEquals(2, posts.size());
        posts = postRepo.getPosts("text", 1, 10);
        assertEquals(1, posts.size());
        posts = postRepo.getPosts("text", 0, 1);
        assertEquals(1, posts.size());

    }

    @Test
    void getPostsCount() {
        Post post1 = postRepo.addPost(Post.builder().tags(List.of(Tag.builder().name("test").build())).text("text").build());
        Post post2 = postRepo.addPost(Post.builder().text("text1").tags(List.of(Tag.builder().name("test").build())).build());
        Post post3 = postRepo.addPost(Post.builder().text("текст").tags(List.of(Tag.builder().name("test").build())).build());
        Integer count = postRepo.getPostsCount("text");
        assertEquals(2, count);
    }

    @Test
    void addLike() {
        Post post1 = postRepo.addPost(Post.builder()
                .tags(List.of(Tag.builder().name("test").build()))
                .text("text")
                .likeCount(0).build());
        postRepo.addLike(post1.getId());
        Post getPost = postRepo.getPost(post1.getId());
        assertNotNull(getPost);
        assertEquals(post1.getLikeCount() + 1, getPost.getLikeCount());
    }
}