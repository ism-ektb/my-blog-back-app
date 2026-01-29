package ru.ism.myblogbackapp.service;

import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoUpdate;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;

import java.util.List;

public interface PostsService {
    /**
     * Поиск списка постов по строке
     *
     * @param search - строка поиска
     * @param offset - номер страницы
     * @param limit  - размер страницы
     */
    PostsOutDto searchPosts(String search, int offset, int limit);

    /**
     * Получение поста по id (@param id)
     */
    PostOutDto getPost(long id);

    /**
     * Создание нового поста, возвращается пост с id
     */
    PostOutDto createPost(PostDtoIn post);

    /**
     * Обновление поста
     */
    PostOutDto updatePost(long id, PostDtoUpdate post);

    /**
     * Удаление поста
     */
    void deletePost(long id);

    /**
     * Добавление лайка к посту
     */
    int incrementLike(long id);

    /**
     * Обновление картинки поста
     */
    void uploadImage(long postId, byte[] image);

    /**
     * Получение картинки поста
     */
    byte[] getImage(long postId);

    /**
     * Получение списка комментариев к посту
     */
    List<CommentDtoOut> getCommentsByPostId(long postId);

    /**
     * Добавить комментарий к посту
     */
    CommentDtoOut addComment(long postId, CommentDtoIn comment);

    /**
     * Получить комментарий по Id
     */
    CommentDtoOut getCommentById(long postId, long commentId);

    /**
     * Обновить комментарий
     */
    CommentDtoOut updateComment(long postId, long commentId, CommentDtoUpdate comment);

    /**
     * Удалить комментарий
     */
    void deleteComment(long postId, long commentId);
}
