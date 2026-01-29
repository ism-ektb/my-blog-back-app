package ru.ism.myblogbackapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.myblogbackapp.model.Comment;
import ru.ism.myblogbackapp.model.Post;
import ru.ism.myblogbackapp.model.dto.in.PostDtoIn;
import ru.ism.myblogbackapp.model.dto.out.PostOutDto;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {TagMapper.class, CommentMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Post dtoToModel(PostDtoIn postDtoIn);

    @Mapping(target = "commentsCount", expression = "java(post.getComments().size())")
    PostOutDto modelToPostOutDto(Post post);

    List<PostOutDto> modelsToPostDtos(List<Post> posts);

}