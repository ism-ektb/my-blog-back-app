package ru.ism.myblogbackapp.model.dto.out;

import java.util.List;

public record PostOutDto(
        long id,
        String title,
        String text,
        List<String> tags,
        int likeCount,
        int commentsCount
) {
}
