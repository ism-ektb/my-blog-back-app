package ru.ism.myblogbackapp.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.repository.ImageRepo;

import javax.sql.DataSource;

@Repository
public class ImageRepoImpl implements ImageRepo {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final String GET_IMAGE = "select image from blog.images where post_id = ?";
    private final String UPDATE_IMAGE = """ 
            INSERT INTO blog.images (image, post_id) VALUES (?, ?) 
            ON CONFLICT (post_id) DO UPDATE SET image = ?            
            """;

    @Override
    public void updateImage(long postId, byte[] image) {
        try {
            jdbcTemplate.update(UPDATE_IMAGE, image, postId, image);
        } catch (DataIntegrityViolationException e) {
            throw new NoFoundException("Post with id = " + postId + "not found");
        }
    }

    /**
     * Получить изображение для поста
     *
     * @param postId
     * @return
     */
    @Override
    public byte[] getImage(long postId) {
        try {
            return jdbcTemplate.queryForObject(GET_IMAGE, byte[].class, postId);
        } catch (EmptyResultDataAccessException e) {
            throw new NoFoundException("Image with id = " + postId + "not found");
        }
    }
}
