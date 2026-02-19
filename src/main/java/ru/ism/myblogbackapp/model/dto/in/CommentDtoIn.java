package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание комментария")
public record CommentDtoIn(
        @Schema(description = "Текст комментария", example = "Хороший пост")
        String text,
        @Schema(description = "Id поста", example = "1")
        long postId
) {
    public CommentDtoIn {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .validate();
    }
}
