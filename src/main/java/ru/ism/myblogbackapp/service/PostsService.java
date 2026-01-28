package ru.ism.myblogbackapp.service;

import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;

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
}
