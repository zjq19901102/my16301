package com.qf.sc.cms.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("com.qf.sc.cms.mapper")
@ImportResource("classpath:applocationContext_dubbo.xml")
public class Csz1801ScCmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Csz1801ScCmsServiceApplication.class, args);
    }
}
