package ru.ism.myblogbackapp.service.impl;

import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoUpdate;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;
import ru.ism.myblogbackapp.service.PostsService;

import java.util.List;

@Component
public class PostsServiceImpl implements PostsService {
    /**
     * Поиск списка постов по строке
     *
     * @param search - строка поиска
     * @param offset - номер страницы
     * @param limit  - размер страницы
     */
    @Override
    public PostsOutDto searchPosts(String search, int offset, int limit) {
        return null;
    }

    /**
     * Получение поста по id (@param id)
     *
     * @param id
     */
    @Override
    public PostOutDto getPost(long id) {
        return null;
    }

    /**
     * Создание нового поста, возвращается пост с id
     *
     * @param post
     */
    @Override
    public PostOutDto createPost(PostDtoIn post) {
        return null;
    }

    /**
     * Обновление поста
     *
     * @param id
     * @param post
     */
    @Override
    public PostOutDto updatePost(long id, PostDtoUpdate post) {
        return null;
    }

    /**
     * Удаление поста
     *
     * @param id
     */
    @Override
    public void deletePost(long id) {

    }

    /**
     * Добавление лайка к посту
     *
     * @param id
     */
    @Override
    public int incrementLike(long id) {
        return 0;
    }

    /**
     * Обновление картинки поста
     *
     * @param postId
     * @param image
     */
    @Override
    public void uploadImage(long postId, byte[] image) {

    }

    /**
     * Получение картинки поста
     *
     * @param postId
     */
    @Override
    public byte[] getImage(long postId) {
        return new byte[0];
    }

    /**
     * Получение списка комментариев к посту
     *
     * @param postId
     */
    @Override
    public List<CommentDtoOut> getCommentsByPostId(long postId) {
        return List.of();
    }

    /**
     * Добавить комментарий к посту
     *
     * @param postId
     * @param comment
     */
    @Override
    public CommentDtoOut addComment(long postId, CommentDtoIn comment) {
        return null;
    }

    /**
     * Получить комментарий по Id
     *
     * @param commentId
     */
    @Override
    public CommentDtoOut getCommentById(long postId, long commentId) {
        return null;
    }

    /**
     * Обновить комментарий
     *
     * @param postId
     * @param commentId
     * @param comment
     */
    @Override
    public CommentDtoOut updateComment(long postId, long commentId, CommentDtoUpdate comment) {
        return null;
    }

    /**
     * Удалить комментарий
     *
     * @param commentId
     */
    @Override
    public void deleteComment(long postId, long commentId) {

    }
}
