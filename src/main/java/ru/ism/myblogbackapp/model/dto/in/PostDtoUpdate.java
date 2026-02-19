package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Запрос на изменение поста")
public record PostDtoUpdate(
        @Schema(description = "Id поста требующего изменения", type = "long", example = "1")
        long id,
        @Schema(description = "Новое название поста", example = "Новое название")
        String title,
        @Schema(description = "Новый текст поста", example = "Новый текст поста")
        String text,
        @Schema(description = "Список новых тегов", type = "array", example = "[\"tag_2\"]")
        List<String> tags) {
    public PostDtoUpdate {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .hasLength(title, 2, 1500)
                .notEmpty(tags)
                .validate();
    }
}
