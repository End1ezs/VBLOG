package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.service.CategoryService;
import org.apache.ibatis.annotations.Mapper;
import com.vblog.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult CategoryList(){
        return categoryService.getCategoryList();
    }
}
