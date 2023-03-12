package com.vblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-12 09:25:00
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}

