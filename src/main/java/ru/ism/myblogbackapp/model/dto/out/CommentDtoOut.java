package ru.ism.myblogbackapp.model.dto.out;

public record CommentDtoOut(Long id,
                            String text,
                            long postId) {
}
