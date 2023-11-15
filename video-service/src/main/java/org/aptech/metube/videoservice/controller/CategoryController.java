package org.aptech.metube.videoservice.controller;

import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.controller.response.ApiResponse;
import org.aptech.metube.videoservice.dto.CategoryDto;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedHashMap;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // list all category
    @GetMapping("/list-categories")
    public ApiResponse getAllCategory(@RequestParam String search,
                                      @RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize){
        LinkedHashMap<String, Object> result = categoryService.listCategory(search, pageNum, pageSize);
        return new ApiResponse(OK, Translator.toLocale("category.get.success"), result);
    }

    // create new category
    @PostMapping("/create-category")
    public ApiResponse createCategory(@RequestBody CategoryDto categoryDto){
        return new ApiResponse(OK, Translator.toLocale("category.create.success"), categoryService.createCategory(categoryDto));
    }

    // update category
    @PostMapping("/update")
    public ApiResponse updateCategory(@RequestBody CategoryDto request) throws NotFoundException {
        return new ApiResponse(OK, Translator.toLocale("category.update.success"), categoryService.updateCategory(request));
    }
}