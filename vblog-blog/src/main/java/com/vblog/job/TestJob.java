package com.vblog.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
// 定时任务
@Component
public class TestJob {
    // 使用cron属性 指定定时任务执行时间
    @Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
        System.out.println("定时任务执行了");
    }
}
