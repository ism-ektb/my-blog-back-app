package ru.ism.myblogbackapp.repository;

import ru.ism.myblogbackapp.model.Comment;

import java.util.List;

public interface CommentRepo {
    /**
     * Добавить комментарий
     * @param comment
     * @return
     */
    Comment addComment(long postId, Comment comment);

    /**
     * Получить комментарий по Id
     * @param post_id
     * @param comment_id
     * @return
     */
    Comment getComment(long post_id, long comment_id);

    /**
     * Изменить комментарий
     * @param post_id
     * @param comment_id
     * @param comment
     * @return
     */
    Comment updateComment(long post_id, long comment_id, String comment);

    /**
     * Удалить комментарий
     * @param post_id
     * @param comment_id
     */
    void deleteComment(long post_id, long comment_id);

    List<Comment> getComments(long post_id);
}
