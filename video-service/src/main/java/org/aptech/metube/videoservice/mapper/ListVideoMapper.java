package org.aptech.metube.videoservice.mapper;

import org.aptech.metube.videoservice.dto.ListVideoDto;
import org.aptech.metube.videoservice.entity.ListVideo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListVideoMapper {
//    @Mapping(target = "favouriteLists" , expression = "java(null)")
    ListVideo dtoToEntity(ListVideoDto listVideoDto);
    ListVideoDto entityToDto(ListVideo listVideo);
}
