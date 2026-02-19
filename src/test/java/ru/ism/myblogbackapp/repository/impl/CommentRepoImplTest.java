package ru.ism.myblogbackapp.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.ism.myblogbackapp.config.BaseConfig;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Comment;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.CommentRepo;
import ru.ism.myblogbackapp.repository.PostRepo;
import ru.ism.myblogbackapp.repository.TagRepo;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(classes = {TestContainerConfig.class, BaseConfig.class, CommentRepoImpl.class, PostRepoImpl.class, TagRepoImpl.class})
class CommentRepoImplTest {
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private Post post1;
    private Post post2;
    private Post post3;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void setUp() {
        post1 = postRepo.addPost(Post.builder().tags(List.of(Tag.builder().name("test").build())).text("text").build());
        post2 = postRepo.addPost(Post.builder().text("text1").tags(List.of(Tag.builder().name("test").build())).build());
        post3 = postRepo.addPost(Post.builder().text("текст").tags(List.of(Tag.builder().name("test").build())).build());
        jdbcTemplate.update("DELETE FROM blog.comments");
        jdbcTemplate.update("DELETE FROM blog.posts_comments");
    }

    @Test
    void addComment_and_getComment() {

        Comment comment = Comment.builder()
                .text("test")
                .post(Post.builder().id(post1.getId()).build())
                .build();
        Comment comment1 = commentRepo.addComment(post1.getId(), comment);
        Comment getComment = commentRepo.getComment(post1.getId(), comment1.getId());
        Assertions.assertEquals(comment.getText(), getComment.getText());
    }

    @Test
    void getComment_return_404() {
        Comment comment = Comment.builder()
                .text("test")
                .post(Post.builder().id(post1.getId()).build())
                .build();
        Comment comment1 = commentRepo.addComment(post1.getId(), comment);
        assertThrows(NoFoundException.class, () -> commentRepo.getComment(10000L, comment1.getId()));
        assertThrows(NoFoundException.class, () -> commentRepo.getComment(post1.getId(), 100000L));

    }

    @Test
    void updateComment() {
        Comment comment = Comment.builder()
                .text("test")
                .post(Post.builder().id(post1.getId()).build())
                .build();
        Comment comment1 = commentRepo.addComment(post1.getId(), comment);

        commentRepo.updateComment(post1.getId(), comment1.getId(), "text_updated");
        Comment getComment = commentRepo.getComment(post1.getId(), comment1.getId());
        Assertions.assertEquals("text_updated", getComment.getText());

    }

}