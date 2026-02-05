package ru.ism.myblogbackapp.repository;

import ru.ism.myblogbackapp.model.Post;

import java.util.List;

public interface PostRepo {

    /**
     * Добавить пост
     * @param post
     * @return
     */
    Post addPost(Post post);

    /**
     * Обновить пост
     * @param post
     * @return
     */
    Post updatePost(Post post);

    /**
     * Удалить пост
     * @param postId
     */
    void deletePost(long postId);

    /**
     * Получить пост
      * @param postId
     * @return
     */
    Post getPost(long postId);

    /**
     * Поиск списка постов по ключевому слову с пагинаций
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    List<Post> getPosts(String filter, int page, int pageSize);

    /**
     * ОБщее количество постов включающие ключевое слово
     * @param filter
     * @return
     */
    Integer getPostsCount(String filter);

    /**
     * Добавить лайк к посту
     * @param postId
     */
    void addLike(long postId);
}
