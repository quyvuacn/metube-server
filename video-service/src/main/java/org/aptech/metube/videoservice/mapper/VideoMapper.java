package org.aptech.metube.videoservice.mapper;

import org.aptech.metube.videoservice.dto.VideoDto;
import org.aptech.metube.videoservice.entity.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {
//    @Mapping(target = "favouriteLists" , expression = "java(null)")
//    @Mapping(target = "listVideoUploads" , expression = "java(null)")
//    @Mapping(target = "categories" , expression = "java(null)")
//    @Mapping(target = "videoComments" , expression = "java(null)")
    VideoDto entityToDto(Video video);
//    @Mapping(target = "favouriteLists" , expression = "java(null)")
//    @Mapping(target = "listVideoUploads" , expression = "java(null)")
//    @Mapping(target = "categories" , expression = "java(null)")
//    @Mapping(target = "videoComments" , expression = "java(null)")
    Video dtoToEntity(VideoDto videoDto);
}