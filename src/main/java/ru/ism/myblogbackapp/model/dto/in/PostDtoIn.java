package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;

import java.util.List;

public record PostDtoIn(String title,
                        String text,
                        List<String> tags) {
    public PostDtoIn {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .hasLength(title, 2, 150)
                .notEmpty(tags)
                .validate();
    }
}
