package ru.ism.myblogbackapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Comment;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.repository.CommentRepo;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepo {

    private final String INSERT_COMMENT = "INSERT INTO blog.comments (text) VALUES (?) ";
    private final String INSERT_POST_COMMENT = "INSERT INTO blog.posts_comments (post_id, comment_id) VALUES (?, ?)";
    private final String SELECT_COMMENT_BY_ID = """
            SELECT * 
                        FROM blog.comments c JOIN blog.posts_comments pc ON c.comment_id = pc.comment_id 
                        WHERE pc.post_id = ? AND c.comment_id = ?""";
    private final String UPDATE_COMMENT = """
            UPDATE blog.comments SET text = ? 
            WHERE comment_id = ?""";
    private final String DELETE_COMMENT = "DELETE FROM blog.comments WHERE post_id = ? AND comment_id = ?";
    private final String SELECT_COMMENTS_BY_POST_ID = """ 
            SELECT * FROM blog.comments c JOIN blog.posts_comments pc ON c.comment_id = pc.comment_id 
                     WHERE post_id = ?""";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Добавить комментарий
     *
     * @param comment
     * @return
     */
    @Override
    public Comment addComment(long post_id, Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_COMMENT, new String[]{"comment_id"});
            stmt.setString(1, comment.getText());
            return stmt;
        }, keyHolder);
        comment.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        jdbcTemplate.update(INSERT_POST_COMMENT, post_id, comment.getId());
        return comment;
    }

    /**
     * Получить комментарий по Id
     *
     * @param post_id
     * @param comment_id
     * @return
     */
    @Override
    public Comment getComment(long post_id, long comment_id) {
        List<Comment> comments = jdbcTemplate.query(SELECT_COMMENT_BY_ID,
                (rs, rowNum) -> toComment(rs), post_id, comment_id);
        try {
            return comments.getFirst();
        } catch (NoSuchElementException e) {
            throw new NoFoundException("No comment found with id " + post_id + " and id " + comment_id);
        }
    }

    /**
     * Изменить комментарий
     *
     * @param post_id
     * @param comment_id
     * @param comment
     * @return
     */
    @Override
    public Comment updateComment(long post_id, long comment_id, String comment) {
        getComment(post_id, comment_id);
        jdbcTemplate.update(UPDATE_COMMENT, comment, comment_id);
        return getComment(post_id, comment_id);
    }

    /**
     * Удалить комментарий
     *
     * @param post_id
     * @param comment_id
     */
    @Override
    public void deleteComment(long post_id, long comment_id) {
        jdbcTemplate.update(DELETE_COMMENT, post_id, comment_id);
    }

    @Override
    public List<Comment> getComments(long post_id) {
        return jdbcTemplate.query(SELECT_COMMENTS_BY_POST_ID, (rs, rowNum) -> toComment(rs), post_id);
    }

    private Comment toComment(ResultSet rs) throws SQLException {
        return Comment.builder()
                .id(rs.getLong("comment_id"))
                .text(rs.getString("text"))
                .post(Post.builder()
                        .id(rs.getLong("post_id"))
                        .build())
                .build();
    }
}
