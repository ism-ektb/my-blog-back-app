package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;

public record CommentDtoUpdate(
        long id,
        String test,
        long postId) {
    public CommentDtoUpdate {
        ValidCheck.check()
                .hasLength(test, 5, 150)
                .validate();
    }
}
