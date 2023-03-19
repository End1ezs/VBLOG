package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.dto.AddArticleDto;
import com.vblog.domain.entity.Article;
import com.vblog.domain.entity.ArticleTag;
import com.vblog.domain.entity.Category;
import com.vblog.domain.entity.Menu;
import com.vblog.domain.vo.*;
import com.vblog.mapper.ArticleMapper;
import com.vblog.service.ArticleService;

import com.vblog.service.ArticleTagService;
import com.vblog.service.CategoryService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleMapper articleMapper;


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
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }
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
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }
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

        //改成从redis中获取浏览信息
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            // 查到数据再去设置
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto article) {
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        save(article1);
//        Map<Long, Long> map=new Map<>();
        List<ArticleTag> collect = article.getTags().stream().map(new Function<Long, ArticleTag>() {
            @Override
            public ArticleTag apply(Long tagId) {
                return new ArticleTag(article1.getId(), tagId);
            }
        }).collect(Collectors.toList());
        articleTagService.saveBatch(collect);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> listArticle(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(title), Article::getTitle, title);
        lambdaQueryWrapper.eq(StringUtils.hasText(summary), Article::getSummary, summary);
        Page<Article> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page, lambdaQueryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<ArticleSelectVo> selectArticle(Long id) {
        Article article = articleMapper.selectById(id);
        ArticleSelectVo ao = BeanCopyUtils.copyBean(article, ArticleSelectVo.class);

        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = articleTagService.list(lqw);
        List<Long> tagsid = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagsid.add(articleTag.getTagId());
        }
        ao.setTags(tagsid);
        return ResponseResult.okResult(ao);
    }

    @Override
    public ResponseResult updateArticle(AddArticleDto addArticleDto) {
        Article article1 = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        updateById(article1);
//        Map<Long, Long> map=new Map<>();
        List<ArticleTag> collect = addArticleDto.getTags().stream().map(new Function<Long, ArticleTag>() {
            @Override
            public ArticleTag apply(Long tagId) {
                return new ArticleTag(addArticleDto.getId(), tagId);
            }
        }).collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        articleMapper.deleteById(id);
        return ResponseResult.okResult();
    }


}
