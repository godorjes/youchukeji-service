package com.daiqi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.daiqi.mapper")
public class DaiqiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DaiqiApplication.class, args);
    }
}
