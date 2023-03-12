package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Category;



public interface CategoryService extends IService<Category> {
     ResponseResult getCategoryList();
}

