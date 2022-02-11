package com.gwhn.elasticsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.gwhn.elasticsearch")
@MapperScan("com.gwhn.elasticsearch.mapper*")
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

}
