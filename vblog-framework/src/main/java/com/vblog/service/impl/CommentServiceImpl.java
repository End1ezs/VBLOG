package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Comment;
import com.vblog.domain.vo.CommentVo;
import com.vblog.domain.vo.PageVo;
import com.vblog.mapper.CommentMapper;
import com.vblog.service.CommentService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.comments.CommentEventsCollector;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 20:03:03
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId, articleId);
        lqw.eq(Comment::getRootId, -1);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lqw);
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(page.getRecords(), CommentVo.class);
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }
}
