package com.vblog.controller;

import com.vblog.domain.ResponseResult;
import com.vblog.domain.vo.LinkVo;
import com.vblog.domain.vo.PageVo;
import com.vblog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.getLinkList(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody LinkVo linkVo){
        return linkService.addLink(linkVo);
    }
    @GetMapping("{id}")
    public ResponseResult getLink(@PathVariable Long id){
        return linkService.getLink(id);
    }
    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkVo linkVo){
        return linkService.updateLink(linkVo);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
}
