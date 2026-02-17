package ru.ism.myblogbackapp.model.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ на запрос поста")
public record PostOutDto(
        @Schema(description = "Порядковый номер поста")
        long id,
        @Schema(description = "Название поста")
        String title,
        @Schema(description = "Текст поста")
        String text,
        @Schema(description = "Список тегов поста")
        List<String> tags,
        @Schema(description = "Количество лайков к посту")
        int likesCount,
        @Schema(description = "Количество комментариев к посту")
        int commentsCount
) {
}
