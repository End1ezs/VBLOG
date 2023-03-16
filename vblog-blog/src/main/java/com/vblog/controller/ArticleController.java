package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Article;
import com.vblog.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    //    @GetMapping("/list")
//    public List<Article> test() {
//        return articleService.list();
//    }
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id) {
        return articleService.updateViewCount(id);
    }

    @GetMapping("{id}")
    public ResponseResult getArticleDetail(@PathVariable Long id) {
        return articleService.getArticleDetail(id);
    }
}
