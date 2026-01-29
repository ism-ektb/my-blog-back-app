package ru.ism.myblogbackapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Comment {
    private long id;
    private Post post;
    private String text;
}
