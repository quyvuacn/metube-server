package org.aptech.metube.videoservice.mapper;

import org.aptech.metube.videoservice.dto.CategoryDto;
import org.aptech.metube.videoservice.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
//    @Mapping(target = "categories" , expression = "java(null)")
    Category dtoToEntity(CategoryDto categoryDto);
    CategoryDto entityToDto(Category category);
}
