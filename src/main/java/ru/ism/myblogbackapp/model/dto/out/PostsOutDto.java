package ru.ism.myblogbackapp.model.dto.out;

import java.util.List;

public record PostsOutDto(List<PostOutDto> posts,
                          boolean hasPrev,
                          boolean hasNext,
                          int lastPage) {
}
