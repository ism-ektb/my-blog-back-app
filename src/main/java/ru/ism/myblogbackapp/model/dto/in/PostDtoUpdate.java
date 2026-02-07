package ru.ism.myblogbackapp.model.dto.in;

import io.github.aglibs.validcheck.ValidCheck;

import java.util.List;

public record PostDtoUpdate(long id,
                            String title,
                            String text,
                            List<String> tags) {
    public PostDtoUpdate {
        ValidCheck.check()
                .hasLength(text, 5, 150)
                .hasLength(title, 2, 1500)
                .notEmpty(tags)
                .validate();
    }
}
