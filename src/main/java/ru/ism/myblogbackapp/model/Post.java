package ru.ism.myblogbackapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class Post {
    private long id;
    private String title;
    private String text;
    private List<Tag> tags;
    @Builder.Default
    private int commentsCount = 0;
    @Builder.Default
    private int likesCount = 0;
}
