package ru.ism.myblogbackapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Comment {
    private long id;
    private Post post;
    private String text;
}
