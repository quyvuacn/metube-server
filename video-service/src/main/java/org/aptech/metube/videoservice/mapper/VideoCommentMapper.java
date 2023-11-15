package org.aptech.metube.videoservice.mapper;

import org.aptech.metube.videoservice.entity.VideoComment;
import org.aptech.metube.videoservice.dto.VideoCommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoCommentMapper {
//    @Mapping(target = "videoComments" , expression = "java(null)")
    VideoComment dtoToEntity(VideoCommentDto videoCommentDto);
    VideoCommentDto entityToDto(VideoComment videoComment);
}
