package ru.ism.myblogbackapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Tag {
    private long id;
    private String name;
}
