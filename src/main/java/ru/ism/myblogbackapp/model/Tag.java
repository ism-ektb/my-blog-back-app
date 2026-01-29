package ru.ism.myblogbackapp.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Tag {
    private long id;
    private String name;
}
