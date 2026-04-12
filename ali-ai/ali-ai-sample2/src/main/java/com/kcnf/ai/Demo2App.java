package com.kcnf.ai;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:config.xml"})
public class Demo2App {
    public static void main(String[] args) {

        SpringApplication.run(Demo2App.class, args);
    }
}