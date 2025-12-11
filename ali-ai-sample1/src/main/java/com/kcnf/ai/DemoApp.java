package com.kcnf.ai;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApp {
    public static void main(String[] args) {
//        // 加载.env文件
//        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
//        // 把.env文件中的变量设置到环境变量中
//        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(DemoApp.class, args);
    }
}