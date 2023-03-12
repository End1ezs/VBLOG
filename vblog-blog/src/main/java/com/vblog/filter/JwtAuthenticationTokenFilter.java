package com.vblog.filter;

import com.alibaba.fastjson.JSON;
import com.vblog.constants.SystemConstants;
import com.vblog.domain.ResponseResult;
import com.vblog.domain.entity.LoginUser;
import com.vblog.enums.AppHttpCodeEnum;
import com.vblog.utils.JwtUtil;
import com.vblog.utils.RedisCache;
import com.vblog.utils.WebUtils;
import io.jsonwebtoken.Claims;
import jdk.nashorn.internal.parser.Token;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.util.Objects;

// 认证过滤器
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 说明该接口不需要登录 直接放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token超时
            // token非法
            // 响应告诉前端需要重新登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        String userId = claims.getSubject();
        // 从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        // 如果获取不到
        if (Objects.isNull(loginUser)) {
            // 说明登录过期 提示重新登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        filterChain.doFilter(request, response);
    }
}
