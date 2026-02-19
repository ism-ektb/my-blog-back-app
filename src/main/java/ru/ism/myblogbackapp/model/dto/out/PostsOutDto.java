package ru.ism.myblogbackapp.model.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ на запрос списка постов с пагинацией")
public record PostsOutDto(
        @Schema(description = "Страницы списка постов")
        List<PostOutDto> posts,
        @Schema(description = "Наличие постов впереди")
        boolean hasPrev,
        @Schema(description = "Наличие постов позади")
        boolean hasNext,
        @Schema(description = "Всего страниц в списке")
        int lastPage) {
}
