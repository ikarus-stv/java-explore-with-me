package ru.practicum.ewm.comments;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper {
    public abstract Comment mapToEntity(NewCommentDto dto);

    public abstract CommentDto mapToDto(Comment entity);

    public abstract  List<CommentDto> mapToListDto(List<Comment> listEntity);

    public Comment updateFields(Comment entity, NewCommentDto newCommentDto) {
        entity.setContent(newCommentDto.getContent());
        return entity;
    }
}
