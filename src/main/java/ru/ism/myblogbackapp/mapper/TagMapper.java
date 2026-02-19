package ru.ism.myblogbackapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ism.myblogbackapp.model.Tag;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(target = "id", ignore = true)
    Tag stringToModel(String name);

    List<Tag> modelToString(List<String> strings);

    List<String> modelsToString(List<Tag> tags);


    default String modelToString(Tag tag) {
        return tag.getName();
    }
}
