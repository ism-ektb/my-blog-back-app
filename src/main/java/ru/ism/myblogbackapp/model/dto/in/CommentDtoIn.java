package ru.ism.myblogbackapp.model.dto.in;


import io.github.aglibs.validcheck.ValidCheck;

public record CommentDtoIn(
        String text,
        long postId
) {
    public CommentDtoIn {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .validate();
    }
}
