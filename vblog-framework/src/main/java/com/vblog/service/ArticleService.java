package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddArticleDto;
import com.vblog.domain.entity.Article;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.vo.ArticleSelectVo;
import com.vblog.domain.vo.CategoryVo;
import com.vblog.domain.vo.PageVo;
import io.swagger.models.auth.In;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryiId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);


    ResponseResult<PageVo> listArticle(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult<ArticleSelectVo> selectArticle(Long id);

    ResponseResult updateArticle(AddArticleDto addArticleDto);

    ResponseResult deleteArticle(Long id);


    ResponseResult getCategoryList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryVo categoryVo);

    ResponseResult getCategory(Long id);

    ResponseResult updateCategory(CategoryVo categoryVo);

    ResponseResult deleteCategory(Long id);
}
