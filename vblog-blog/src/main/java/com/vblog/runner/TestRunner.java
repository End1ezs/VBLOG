package com.vblog.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
// 预初始化
@Component
public class TestRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化");
    }
}
