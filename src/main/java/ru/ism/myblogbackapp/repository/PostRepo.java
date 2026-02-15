package ru.ism.myblogbackapp.repository;

import ru.ism.myblogbackapp.model.Post;

import java.util.List;

public interface PostRepo {

    /**
     * Добавить пост
     *
     * @param post
     * @return
     */
    Post addPost(Post post);

    /**
     * Обновить пост
     *
     * @param post
     * @return
     */
    Post updatePost(Post post);

    /**
     * Удалить пост
     *
     * @param postId
     */
    void deletePost(long postId);

    /**
     * Получить пост
     *
     * @param postId
     * @return
     */
    Post getPost(long postId);

    /**
     * Поиск списка постов по ключевой фразе названия и не пустому списку тегов с пагинаций
     *
     * @param filter_title ключевая фраза
     * @param filter_tags  список тегов
     * @return
     */
    List<Post> getPosts(String filter_title, List<String> filter_tags, int offset, int pageSize);

    /**
     * Общее количество постов включающие ключевую фразу в названии и непустому списку тегов
     *
     * @param filter      ключевая фраза
     * @param filter_tags список тегов
     * @return
     */
    Integer getPostsCount(String filter, List<String> filter_tags);

    /**
     * Добавить лайк к посту
     *
     * @param postId
     */
    void addLike(long postId);

    /**
     * Поиск постов по ключевой фразе в названии
     * @param filter_title - ключевая фраза в названии
     * @param offset
     * @param page_size
     * @return
     */
    List<Post> searchOnlyTitle(String filter_title, int offset, int page_size);

    /**
     * Общее число постов название которых включает ключевую фразу
     * @param filter_title
     * @return
     */
    int countSearchOnlyTitle(String filter_title);
}
