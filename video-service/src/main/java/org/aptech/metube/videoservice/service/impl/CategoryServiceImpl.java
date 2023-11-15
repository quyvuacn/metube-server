package org.aptech.metube.videoservice.service.impl;

import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.controller.response.CategoryResponse;
import org.aptech.metube.videoservice.dto.CategoryDto;
import org.aptech.metube.videoservice.entity.Category;
import org.aptech.metube.videoservice.entity.Video;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.mapper.CategoryMapper;
import org.aptech.metube.videoservice.repo.CategoryRepository;
import org.aptech.metube.videoservice.service.CategoryService;
import org.aptech.metube.videoservice.specification.CategorySpecification;
import org.aptech.metube.videoservice.specification.VideoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper mapper;

    @Override
    public LinkedHashMap<String, Object> listCategory(String search, Integer pageNum, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<Category> listCategory = categoryRepository.findAll(
                Specification.where(CategorySpecification.hasName(search)), paging);
        List<CategoryResponse> categoryDtos = listCategory.getContent().stream()
                .map(category -> {
                    CategoryResponse categoryResponse = CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getCategoryName())
                            .build();
                    return categoryResponse;
                })
                .collect(Collectors.toList());

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("pageNum", listCategory.getNumber());
        result.put("pageSize", listCategory.getSize());
        result.put("totalPage", listCategory.getTotalPages());
        result.put("data", categoryDtos);
        return result;
//        return categoryRepository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(mapper.dtoToEntity(categoryDto));
        return mapper.entityToDto(category);
    }

    @Override
    public Category updateCategory(CategoryDto request) throws NotFoundException {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("category.not.found")));

        category.setCategoryName(request.getCategoryName());

        Category result = categoryRepository.save(category);
        return result;
    }
}
