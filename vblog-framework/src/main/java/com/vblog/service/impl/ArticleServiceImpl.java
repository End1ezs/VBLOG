package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Article;
import com.vblog.domain.entity.Category;
import com.vblog.domain.vo.ArticleDetailVo;
import com.vblog.domain.vo.ArticleListVo;
import com.vblog.domain.vo.HotArticleVo;
import com.vblog.domain.vo.PageVo;
import com.vblog.mapper.ArticleMapper;
import com.vblog.service.ArticleService;

import com.vblog.service.CategoryService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        // 使用LambdaQueryWrapper封装查询条件
        LambdaQueryWrapper<Article> lqm = new LambdaQueryWrapper<>();
        // 1.必须是正式文章
        lqm.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 2.按照浏览量进行排序
        lqm.orderByDesc(Article::getViewCount);
        // 3.最多只能查询10条记录
        Page<Article> page = new Page(1, 10);
        page(page, lqm);

        //bean拷贝 将Article中拥有的全部字段，只保留必要的，重新封装传给前端
        List<Article> articles = page.getRecords();
//        List<HotArticleVo> articlesvo = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articlesvo.add(vo);
//        }
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件 如果categoryiId存在，加上对这个属性的判断，如果没有则不判断
        // 状态是正式发布的文章
        // 如果置顶为1的文章 则将其进行排序 方法对isTOP字段降序排列
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        lqw.orderByDesc(Article::getIsTop);
        // 分页查询 page
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lqw);

        List<Article> articles = page.getRecords();
        // articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
      articles.stream().map(article ->
            article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())
        ).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        // 查询分类名称

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        // 转换成vo
        // 根据分类id查询对应的分类名称
        // 封装响应返回
        Article article = getById(id);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();

        Category category = categoryService.getById(categoryId);
        if(category!=null){
            // 查到数据再去设置
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }
}
