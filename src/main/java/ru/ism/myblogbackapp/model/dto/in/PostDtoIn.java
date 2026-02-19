package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Запрос на создание нового поста")
public record PostDtoIn(
        @Schema(description = "Название поста", example = "Новый пост")
        String title,
        @Schema(description = "Текст поста", example = "Текст поста")
        String text,
        @Schema(description = "Список тегов", type = "array", example = "[\"tag_1\"]")
        List<String> tags) {
    public PostDtoIn {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .hasLength(title, 2, 1500)
                .notEmpty(tags)
                .validate();
    }
}
