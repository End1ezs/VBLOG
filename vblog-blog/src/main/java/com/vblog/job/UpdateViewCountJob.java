package com.vblog.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.vblog.domain.dto.AddArticleDto;
import com.vblog.domain.entity.Article;
import com.vblog.domain.vo.ArticleSelectVo;
import com.vblog.mapper.ArticleMapper;
import com.vblog.service.ArticleService;
import com.vblog.utils.BeanCopyUtils;
import com.vblog.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount() {
        // 获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");

        List<Article> articles = viewCountMap.entrySet().stream().map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue())).collect(Collectors.toList());
        for (Article article : articles) {
            Article article1 = articleMapper.selectById(article);
            article1.setViewCount(article.getViewCount());
            articleMapper.updateById(article1);
        }

        // 更新到数据库中
//        for (Article article : articles) {
//            articleService.updateById(article);
//        }
//        articleService.updateBatchById(articles);
    }
}
