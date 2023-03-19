package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Category;
import com.vblog.domain.vo.CategoryVo;


public interface CategoryService extends IService<Category> {


    ResponseResult getCategoryList();

    ResponseResult<CategoryVo> listAllCategory();
}

