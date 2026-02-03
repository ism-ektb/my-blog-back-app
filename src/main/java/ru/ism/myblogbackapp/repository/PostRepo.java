package ru.ism.myblogbackapp.repository;

import ru.ism.myblogbackapp.model.Post;

import java.util.List;

public interface PostRepo {

    Post addPost(Post post);
    Post updatePost(Post post);
    void deletePost(long postId);
    Post getPost(long postId);
    List<Post> getPosts(String filter, int page, int pageSize);
    Integer getPostsCount(String filter);
    void addLike(long postId);


}
