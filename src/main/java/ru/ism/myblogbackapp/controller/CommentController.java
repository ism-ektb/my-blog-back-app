package ru.ism.myblogbackapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoUpdate;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;
import ru.ism.myblogbackapp.service.PostsService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final PostsService postsService;

    @GetMapping
    public List<CommentDtoOut> getCommentsByPostId(@PathVariable("id") long id) {
        return postsService.getCommentsByPostId(id);
    }

    @GetMapping("/{commentId}")
    public CommentDtoOut getCommentById(@PathVariable("id") long id,
                                        @PathVariable("commentId") long commentId) {
        return postsService.getCommentById(id, commentId);
    }

    @PostMapping
    public CommentDtoOut createComment(@PathVariable("id") long id,
                                       @RequestBody CommentDtoIn commentDtoIn) {
        return postsService.addComment(id, commentDtoIn);
    }

    @PutMapping("/{commentId}")
    public CommentDtoOut updateComment(@PathVariable("id") long id,
                                       @PathVariable("commentId") long commentId,
                                       @RequestBody CommentDtoUpdate commentDtoUpdate) {
        return postsService.updateComment(id, commentId, commentDtoUpdate);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable("id") long id,
            @PathVariable("commentId") long commentId) {
        postsService.deleteComment(id, commentId);
    }
}
