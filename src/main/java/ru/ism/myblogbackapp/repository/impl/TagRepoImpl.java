package ru.ism.myblogbackapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.TagRepo;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {

    private final String INSERT_TAG = "INSERT INTO blog.tags (tag) VALUES (?)";
    private final String INSERT_POST_TAG = "INSERT INTO blog.posts_tags (tag_id, post_id) VALUES (?, ?)";
    private final String SELECT_TAGS_BY_POST_ID = "SELECT * FROM blog.tags t join blog.posts_tags pt using (tag_id) WHERE pt.post_id = ?";
    private final String DELETE_TAGS_BY_POST_ID = "DELETE FROM blog.tags t USING blog.posts_tags pt WHERE t.tag_id = pt.tag_id AND pt.post_id = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Добавить список тегов
     *
     * @param post_Id
     * @param tags
     */
    @Override
    public void save(long post_Id, List<Tag> tags) {
        tags.forEach(tag -> addTag(post_Id, tag));
    }

    /**
     * Получить список тегов по пост Id
     *
     * @param post_id
     * @return
     */
    @Override
    public List<Tag> getTags(long post_id) {
        return jdbcTemplate.query(SELECT_TAGS_BY_POST_ID, (rs, rowNum) -> toTag(rs), post_id);
    }

    /**
     * Удалить Теги к посту
     *
     * @param post_Id
     */
    @Override
    public void deleteTagsByPostId(long post_Id) {
        jdbcTemplate.update(DELETE_TAGS_BY_POST_ID, post_Id);
    }

    /**
     * Обновить теги поста
     *
     * @param post_Id
     * @param tags
     */
    @Override
    public void updateTags(long post_Id, List<Tag> tags) {
        deleteTagsByPostId(post_Id);
        save(post_Id, tags);
    }

    private void addTag(long postId, Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_TAG, new String[]{"tag_id"});
            stmt.setString(1, tag.getName());
            return stmt;
        }, keyHolder);
        long tag_id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        jdbcTemplate.update(INSERT_POST_TAG, tag_id, postId);
    }

    private Tag toTag(ResultSet rs) throws SQLException {
        return Tag.builder()
                .id(rs.getLong("tag_id"))
                .name(rs.getString("tag"))
                .build();
    }
}
