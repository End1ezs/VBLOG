package com.vblog.utils;

import com.vblog.domain.entity.Article;
import com.vblog.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {

    }

    // 使用泛型，在传入字节码文件的类型的时候获取到要返回对象的类型V 最后返回V对象
    public static <V> V copyBean(Object source, Class<V> calzz) {
        // 创建目标对象
        V target = null;
        try {
            // 字节码对象，通过反射获取要接收属性的对象
            target = calzz.newInstance();
            // 实现属性拷贝
            BeanUtils.copyProperties(source, target);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 实现属性拷贝
        // 返回结果
        return target;
    }

    public static<O,V> List<V> copyBeanList(List<O> list, Class<V> calzz){
        return list.stream().map(o ->copyBean(o,calzz)).collect(Collectors.toList());
    }
}
//    public static void main(String[] args) {
//        Article article = new Article();
//        article.setId(1L);
//        article.setTitle("ss");
//        HotArticleVo hotArticleVo = copyBean(article,HotArticleVo.class);
//        System.out.println(hotArticleVo);
//    }
