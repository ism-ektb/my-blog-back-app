package ru.ism.myblogbackapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ism.myblogbackapp.exception.NoFoundException;
import ru.ism.myblogbackapp.mapper.CommentMapper;
import ru.ism.myblogbackapp.mapper.PostMapper;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoUpdate;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.in.PostDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;
import ru.ism.myblogbackapp.model.dto.out.PostsOutDto;
import ru.ism.myblogbackapp.repository.CommentRepo;
import ru.ism.myblogbackapp.repository.ImageRepo;
import ru.ism.myblogbackapp.repository.PostRepo;
import ru.ism.myblogbackapp.service.PostsService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostRepo postRepo;
    private final PostMapper postMapper;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final ImageRepo imageRepo;

    /**
     * Поиск списка постов по строке
     *
     * @param search   - строка поиска
     * @param pageNum  - номер страницы
     * @param pageSize - размер страницы
     */
    @Override
    public PostsOutDto searchPosts(String search, int pageNum, int pageSize) {
        List<Post> posts = postRepo.getPosts(search, pageNum * pageSize, pageSize);
        long postCount = postRepo.getPostsCount(search);
        boolean hasPrev = pageNum > 0;
        boolean hasNext = pageNum < (postCount - 1) / pageSize;
        return new PostsOutDto(postMapper.modelsToPostDtos(posts), hasPrev, hasNext, (int) (postCount - 1) / pageSize);
    }

    /**
     * Получение поста по id (@param id)
     *
     * @param id
     */
    @Override
    public PostOutDto getPost(long id) {
        return postMapper.modelToPostOutDto(postRepo.getPost(id));
    }

    /**
     * Создание нового поста, возвращается пост с id
     *
     * @param post
     */
    @Override
    public PostOutDto createPost(PostDtoIn post) {

        return postMapper.modelToPostOutDto(postRepo.addPost(postMapper.dtoToModel(post)));
    }

    /**
     * Обновление поста
     *
     * @param id
     * @param post
     */
    @Override
    public PostOutDto updatePost(long id, PostDtoUpdate post) {
        Post postForUp = postMapper.dtoUpToModel(Optional.of(post)
                .filter(p -> p.id() == id)
                .orElseThrow(() -> new NoFoundException("Bad postId")));
        return postMapper.modelToPostOutDto(postRepo.updatePost(postForUp));
    }

    /**
     * Удаление поста
     *
     * @param id
     */
    @Override
    public void deletePost(long id) {
        postRepo.deletePost(id);
    }

    /**
     * Добавление лайка к посту
     *
     * @param id
     */
    @Override
    public int incrementLike(long id) {
        postRepo.addLike(id);
        return postRepo.getPost(id).getLikesCount();
    }

    /**
     * Обновление картинки поста
     *
     * @param postId
     * @param image
     */
    @Override
    public void uploadImage(long postId, byte[] image) {
        imageRepo.updateImage(postId, image);
    }

    /**
     * Получение картинки поста
     *
     * @param postId
     */
    @Override
    public byte[] getImage(long postId) {
        return imageRepo.getImage(postId);
    }

    /**
     * Получение списка комментариев к посту
     *
     * @param postId
     */
    @Override
    public List<CommentDtoOut> getCommentsByPostId(long postId) {

        return commentMapper.modelsToDto(commentRepo.getComments(postId));
    }

    /**
     * Добавить комментарий к посту
     *
     * @param postId
     * @param comment
     */
    @Override
    public CommentDtoOut addComment(long postId, CommentDtoIn comment) {

        return commentMapper.modelToDto(commentRepo.addComment(postId, commentMapper.dtoToModel(comment)));
    }

    /**
     * Получить комментарий по Id
     *
     * @param commentId
     */
    @Override
    public CommentDtoOut getCommentById(long postId, long commentId) {

        return commentMapper.modelToDto(commentRepo.getComment(postId, commentId));
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
        String commentText = Optional.of(comment)
                .filter(c -> postId == c.postId() && commentId == c.id())
                .map(CommentDtoUpdate::test)
                .orElseThrow(() -> new NoFoundException("Bad commentId"));
        return commentMapper.modelToDto(commentRepo.updateComment(postId, commentId, commentText));
    }

    /**
     * Удалить комментарий
     *
     * @param commentId
     */
    @Override
    public void deleteComment(long postId, long commentId) {
        commentRepo.deleteComment(postId, commentId);
    }
}
