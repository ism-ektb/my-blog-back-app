package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление комментария")
public record CommentDtoUpdate(
        @Schema(description = "id комментария", example = "1")
        long id,
        @Schema(description = "Текст обновленного комментария", example = "Текст обновленного комментария")
        String test,
        @Schema(description = "id поста", example = "1")
        long postId) {
    public CommentDtoUpdate {
        ValidCheck.check()
                .hasLength(test, 5, 150)
                .validate();
    }
}
