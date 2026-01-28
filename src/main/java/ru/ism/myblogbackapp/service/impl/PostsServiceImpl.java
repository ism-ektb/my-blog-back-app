package ru.ism.myblogbackapp.service.impl;

import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;
import ru.ism.myblogbackapp.service.PostsService;

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
}
