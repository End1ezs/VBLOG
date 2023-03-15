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
import com.vblog.service.UserService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId, articleId);
        lqw.eq(Comment::getRootId, -1);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lqw);
        List<CommentVo> commentVoList = toCommenVolist(page.getRecords());
        //查询所有根评论对应的子评论的集合，并赋值相应的属性
        for (CommentVo commentVo : commentVoList) {
            // 查询对应的子评论对应的值，并赋值
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);

        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    private List<CommentVo> getChildren(Long id) {
        //传入根评论id，根据根评论id查询所对应的子评论的集合
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getRootId, id);
        lqw.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(lqw);
        List<CommentVo> commentVos = toCommenVolist(comments);
        return commentVos;
    }

    private List<CommentVo> toCommenVolist(List<Comment> list) {
        // 通过creatby查询用户的昵称并赋值
        // 通过toCommentUserId
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        // 遍历vo集合
        for (CommentVo commentVo : commentVos) {
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            // 通过toCommentUserId查询用户的昵称并赋值
            // 如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentId() != -1) {
                String toCommentnickName = userService.getById(commentVo.getToCommentId()).getNickName();
                commentVo.setToCommentUserName(toCommentnickName);
            }
        }
        return commentVos;
    }
}
