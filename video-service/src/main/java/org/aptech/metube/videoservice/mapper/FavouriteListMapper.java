package org.aptech.metube.videoservice.mapper;

import org.aptech.metube.videoservice.dto.FavouriteListDto;
import org.aptech.metube.videoservice.entity.FavouriteList;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavouriteListMapper {
//    @Mapping(target = "favouriteLists" , expression = "java(null)")
    FavouriteList dtoToEntity(FavouriteListDto favouriteListDto);
    FavouriteListDto entityToDto(FavouriteList favouriteList);
}
