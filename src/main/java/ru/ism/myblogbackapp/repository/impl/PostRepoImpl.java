package ru.ism.myblogbackapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.repository.PostRepo;
import ru.ism.myblogbackapp.repository.TagRepo;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class PostRepoImpl implements PostRepo {

    private final String INSERT_POST = "INSERT INTO blog.posts (title, text) VALUES (?, ?) ";
    private final String SELECT_POST_BY_ID = """
            SELECT
               p.post_id,
               p.title,
               p.text,
               COUNT(pc.comment_id),
               p.likecount
            FROM blog.posts p left outer join blog.posts_comments pc on p.post_id = pc.post_id
            WHERE p.post_id = ? GROUP BY p.post_id """;

    private final String SELECT_POST_BY_STRING = """
            SELECT
               p.post_id,
               p.title,
               p.text,
               COUNT(pc.comment_id),
               p.likecount
            FROM blog.posts p left join blog.posts_comments pc on p.post_id = pc.post_id
            WHERE p.text ILIKE '%' || ? || '%' GROUP BY p.post_id
            ORDER BY post_id
            OFFSET ? LIMIT ?""";

    private final String SELECT_COUNT = "SELECT COUNT(*) FROM blog.posts p WHERE p.text ILIKE '%' || ? || '%'";
    private final String DELETE_POST = "DELETE FROM blog.posts WHERE post_id = ? ";
    private final String UPDATE_POST = "UPDATE blog.posts SET title = ?, text = ? WHERE post_id = ?";
    private final String ADD_LIKE = "UPDATE blog.posts SET likecount = likecount + 1 WHERE post_id = ? ";

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private final TagRepo tagRepo;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Post addPost(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_POST, new String[]{"post_id"});
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getText());
            return stmt;
        }, keyHolder);
        post.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        tagRepo.save(post.getId(), post.getTags());
        return post;
    }

    @Override
    public Post updatePost(Post post) {
        jdbcTemplate.update(UPDATE_POST, post.getTitle(), post.getText(), post.getId());
        tagRepo.updateTags(post.getId(), post.getTags());
        return getPost(post.getId());
    }

    @Override
    public void deletePost(long postId) {
        jdbcTemplate.update(DELETE_POST, postId);
    }

    @Override
    public Post getPost(long postId) {
        List<Post> posts = jdbcTemplate.query(SELECT_POST_BY_ID, (rs, rowNum) -> toPost(rs), postId);
        Post post = Optional.of(posts.getFirst())
                .orElseThrow(() -> new NoFoundException("Post not found"));
        post.setTags(tagRepo.getTags(postId));
        return post;
    }

    @Override
    public List<Post> getPosts(String filter, int page, int pageSize) {
        return jdbcTemplate.query(SELECT_POST_BY_STRING,
                        (rs, rowNum) -> toPost(rs), filter, page, pageSize)
                .stream()
                .peek(post -> post.setTags(tagRepo.getTags(post.getId())))
                .toList();
    }

    @Override
    public Integer getPostsCount(String filter) {
        return jdbcTemplate.queryForObject(SELECT_COUNT, Integer.class, filter);
    }

    @Override
    public void addLike(long postId) {
        jdbcTemplate.update(ADD_LIKE, postId);
    }

    private Post toPost(ResultSet rs) throws SQLException {
        return Post.builder()
                .id(rs.getLong("post_id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .commentsCount(rs.getInt("count"))
                .likesCount(rs.getInt("likecount"))
                .build();
    }
}
