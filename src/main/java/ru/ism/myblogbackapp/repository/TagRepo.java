package ru.ism.myblogbackapp.repository;

import ru.ism.myblogbackapp.model.Tag;

import java.util.List;

public interface TagRepo {
    /**
     * Добавить список тегов
     * @param post_Id
     * @param tags
     */
    void save(long post_Id, List<Tag> tags);

    /**
     * Получить список тегов по пост Id
     * @param post_Id
     * @return
     */
    List<Tag> getTags(long post_Id);

    /**
     * Удалить Теги к посту
     * @param post_Id
     */
    void deleteTagsByPostId(long post_Id);

    /**
     * Обновить тэги поста
     * @param post_Id
     * @param tags
     */
    void updateTags(long post_Id, List<Tag> tags);
}
