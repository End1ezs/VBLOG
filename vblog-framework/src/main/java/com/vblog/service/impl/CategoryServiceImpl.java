package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Article;
import com.vblog.domain.entity.Category;
import com.vblog.domain.vo.CategoryVo;
import com.vblog.mapper.CategoryMapper;
import com.vblog.service.ArticleService;
import com.vblog.service.CategoryService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 18:35:19
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {

        LambdaQueryWrapper<Article> lqm = new LambdaQueryWrapper<>();
        // 1.必须是正式文章
        lqm.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 使用mbp的list方法调用上面的组合查询条件进行查询
        List<Article> articleList = articleService.list(lqm);
        // 2.按照浏览量进行排序
        Set<Long> categoryIds = articleList.stream().map(article -> article.getCategoryId()).collect(Collectors.toSet());

        List<Category> categories = listByIds(categoryIds);

        categories = categories.stream().filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus())).collect(Collectors.toList());
        List<CategoryVo> categoryvo = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryvo);
    }

    @Override
    public ResponseResult<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoriesvo = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoriesvo);
    }
}
