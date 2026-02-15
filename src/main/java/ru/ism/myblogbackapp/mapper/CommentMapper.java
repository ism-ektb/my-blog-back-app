package ru.ism.myblogbackapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.myblogbackapp.model.Comment;
import ru.ism.myblogbackapp.model.dto.in.CommentDtoIn;
import ru.ism.myblogbackapp.model.dto.out.CommentDtoOut;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post.id", source = "commentDtoIn.postId")
    Comment dtoToModel(CommentDtoIn commentDtoIn);

    @Mapping(target = "postId", source = "comment.post.id")
    CommentDtoOut modelToDto(Comment comment);

    List<CommentDtoOut> modelsToDto(List<Comment> comments);
}
