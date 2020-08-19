package com.damrankomran.poc_product_api;

import com.damrankomran.poc_product_api.config.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(JpaConfiguration.class)
@SpringBootApplication
public class PocProductApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocProductApiApplication.class, args);
    }

}
