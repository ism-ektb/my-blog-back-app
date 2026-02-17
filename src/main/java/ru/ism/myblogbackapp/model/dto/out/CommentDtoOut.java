package ru.ism.myblogbackapp.model.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ на запрос комментария")
public record CommentDtoOut(
        @Schema(description = "Порядковый номер комментария")
        Long id,
        @Schema(description = "текст комментария")
        String text,
        @Schema(description = "Порядковый номер поста")
        long postId) {
}
