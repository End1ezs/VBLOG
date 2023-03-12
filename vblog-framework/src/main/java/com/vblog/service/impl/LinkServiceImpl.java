package com.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Link;
import com.vblog.domain.vo.LinkVo;
import com.vblog.mapper.LinkMapper;
import com.vblog.service.LinkService;
import com.vblog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 09:25:00
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的友链
        LambdaQueryWrapper<Link> lqm = new LambdaQueryWrapper<>();
        lqm.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(lqm);

        // 转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);


    }
}
