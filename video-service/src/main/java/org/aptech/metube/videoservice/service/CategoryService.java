package org.aptech.metube.videoservice.service;

import org.aptech.metube.videoservice.dto.CategoryDto;
import org.aptech.metube.videoservice.entity.Category;
import org.aptech.metube.videoservice.exception.NotFoundException;

import java.util.LinkedHashMap;
import java.util.List;

public interface CategoryService {
    LinkedHashMap<String, Object> listCategory(String search, Integer pageNum, Integer pageSize);
    CategoryDto createCategory(CategoryDto categoryDto);
    Category updateCategory(CategoryDto request) throws NotFoundException;
}
