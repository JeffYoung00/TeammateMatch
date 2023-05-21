package com.jeff.teammate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jeff.teammate.mapper")
public class MyApplication {

    public static void main(String[] args) {

        SpringApplication.run(MyApplication.class, args);

    }

}
