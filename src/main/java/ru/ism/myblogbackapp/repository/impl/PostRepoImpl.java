package ru.ism.myblogbackapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.Tag;
import ru.ism.myblogbackapp.repository.PostRepo;
import ru.ism.myblogbackapp.repository.TagRepo;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


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
    private final String DELETE_POST = "DELETE FROM blog.posts WHERE post_id = ? ";
    private final String UPDATE_POST = "UPDATE blog.posts SET title = ?, text = ? WHERE post_id = ?";
    private final String ADD_LIKE = "UPDATE blog.posts SET likecount = likecount + 1 WHERE post_id = ? ";
    private final String SEARCH_ON_TITLE_AND_TAG = """
              SELECT *
                  FROM (SELECT p.post_id,
                   p.title,
                   p.text,
                   COUNT(pc.comment_id),
                   p.likecount
                        FROM blog.posts p
                     left join blog.posts_comments pc on p.post_id = pc.post_id
            WHERE p.post_id IN (SELECT a.post_id
                                FROM (SELECT p.post_id
                                      FROM blog.posts p
                                      WHERE p.title ILIKE '%' || :filter_title || '%') AS a
                                         JOIN (SELECT post_id, COUNT(pt.tag_id) as c
                                               FROM blog.tags t join blog.posts_tags pt  on t.tag_id = pt.tag_id
                                               WHERE t.tag LIKE ANY (ARRAY[ :filter_tags ])
                                               GROUP BY pt.post_id
                                               HAVING COUNT(pt.tag_id) = :tags_count ) AS b
                                              ON a.post_id = b.post_id
                                GROUP BY a.post_id
                                ORDER BY a.post_id
                                OFFSET :offset LIMIT :page_size)
            GROUP BY p.post_id) AS a
               JOIN blog.posts_tags pt ON a.post_id = pt.post_id
               join blog.tags AS t2 ON pt.tag_id = t2.tag_id """;
    private final String SELECT_COUNT_TITLE_AND_TAGS = """
            SELECT COUNT(DISTINCT a.post_id)
            FROM (SELECT p.post_id
                  FROM blog.posts p
                  WHERE p.title ILIKE '%' || :filter_title || '%')
                     AS a
                     JOIN (SELECT post_id, COUNT(pt.tag_id) as c
                           FROM blog.tags t
                                    join blog.posts_tags pt on t.tag_id = pt.tag_id
                           WHERE t.tag LIKE ANY (ARRAY [ :filter_tags ])
                           GROUP BY pt.post_id
                           HAVING COUNT(pt.tag_id) = :tags_count) AS b
                          ON a.post_id = b.post_id""";
    private final String SEARCH_WITHOUT_TAGS = """
            SELECT *
            FROM (SELECT p.post_id,
                         p.title,
                         p.text,
                         COUNT(pc.comment_id),
                         p.likecount
                  FROM blog.posts p
                           left join blog.posts_comments pc on p.post_id = pc.post_id
                  WHERE p.post_id IN (SELECT p.post_id
                                      FROM blog.posts p
                                      WHERE p.title ILIKE '%' || ? || '%'
                                      ORDER BY p.post_id
                                      OFFSET ? LIMIT ?)
                  GROUP BY p.post_id)
                     AS a
                     JOIN blog.posts_tags pt ON a.post_id = pt.post_id
                     join blog.tags AS t2 ON pt.tag_id = t2.tag_id;
            """;
    private final String COUNT_SEARCH_WITHOUT_TAGS = """
            SELECT COUNT(p.post_id)
            FROM  blog.posts p
                  WHERE p.title ILIKE '%' || ? || '%';
            """;

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private final TagRepo tagRepo;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Добавить пост
     *
     * @param post
     * @return
     */
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

    /**
     * Обновить пост
     *
     * @param post
     * @return
     */
    @Override
    public Post updatePost(Post post) {
        jdbcTemplate.update(UPDATE_POST, post.getTitle(), post.getText(), post.getId());
        tagRepo.updateTags(post.getId(), post.getTags());
        return getPost(post.getId());
    }

    /**
     * Удалить пост
     *
     * @param postId
     */
    @Override
    public void deletePost(long postId) {
        jdbcTemplate.update(DELETE_POST, postId);
    }

    /**
     * Получить пост по id
     *
     * @param postId
     * @return
     */
    @Override
    public Post getPost(long postId) {
        List<Post> posts = jdbcTemplate.query(SELECT_POST_BY_ID, (rs, rowNum) -> toPost(rs), postId);
        Post post = Optional.of(posts.getFirst())
                .orElseThrow(() -> new NoFoundException("Post not found"));
        post.setTags(tagRepo.getTags(postId));
        return post;
    }

    /**
     * Поиск списка постов по вхождению строки в название и списку тегов с пагинацией
     *
     * @param filter_title ключевая фраза названия поста
     * @param filter_tags  список тегов для поиска
     * @return
     */
    @Override
    public List<Post> getPosts(String filter_title, List<String> filter_tags, int offset, int page_size) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("filter_title", filter_title);
        namedParameters.addValue("filter_tags", filter_tags);
        namedParameters.addValue("tags_count", filter_tags.size());
        namedParameters.addValue("offset", offset);
        namedParameters.addValue("page_size", page_size);
        List<Post> posts = template.query(SEARCH_ON_TITLE_AND_TAG, namedParameters, (rs, rowNum)
                -> toPost1(rs));
        HashMap<Long, Post> map = new HashMap<>();
        for (Post post : posts) {
            if (map.containsKey(post.getId())) {
                map.get(post.getId()).getTags().add(post.getTags().getFirst());
            } else {
                map.put(post.getId(), post);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * Определяет общее количество найденных постов удовлетворяющих условию поиска
     *
     * @param filter_title ключевая фраза названия поста
     * @param filter_tags  список тегов для поиска
     * @return
     */
    @Override
    public Integer getPostsCount(String filter_title, List<String> filter_tags) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("filter_title", filter_title);
        namedParameters.addValue("filter_tags", filter_tags);
        namedParameters.addValue("tags_count", filter_tags.size());
        return template.queryForObject(SELECT_COUNT_TITLE_AND_TAGS, namedParameters, Integer.class);
    }

    /**
     * Добавляет лайк к посту
     *
     * @param postId
     */
    @Override
    public void addLike(long postId) {
        jdbcTemplate.update(ADD_LIKE, postId);
    }

    /**
     * Поиск постов по ключевой фразе в названии
     *
     * @param filter_title - ключевая фраза в названии
     * @param offset
     * @param page_size
     * @return
     */
    @Override
    public List<Post> searchOnlyTitle(String filter_title, int offset, int page_size) {
        List<Post> posts = jdbcTemplate.query(SEARCH_WITHOUT_TAGS, (rs, rowNum)
                -> toPost1(rs), filter_title, offset, page_size);
        HashMap<Long, Post> map = new HashMap<>();
        for (Post post : posts) {
            if (map.containsKey(post.getId())) {
                map.get(post.getId()).getTags().add(post.getTags().getFirst());
            } else {
                map.put(post.getId(), post);
            }
        }
        return new ArrayList<>(map.values());
    }

    /**
     * Общее число постов название которых включает ключевую фразу
     *
     * @param filter_title
     * @return
     */
    @Override
    public int countSearchOnlyTitle(String filter_title) {
        return jdbcTemplate.queryForObject(COUNT_SEARCH_WITHOUT_TAGS, Integer.class, filter_title);
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

    private Post toPost1(ResultSet rs) throws SQLException {
        return Post.builder()
                .id(rs.getLong("post_id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .commentsCount(rs.getInt("count"))
                .tags(new ArrayList<>(List.of(Tag.builder()
                        .id(rs.getLong("tag_id"))
                        .name(rs.getString("tag"))
                        .build())))
                .likesCount(rs.getInt("likecount"))
                .build();
    }
}
