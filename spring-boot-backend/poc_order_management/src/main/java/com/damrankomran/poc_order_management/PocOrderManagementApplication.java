package com.damrankomran.poc_order_management;

import com.damrankomran.poc_order_management.config.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(JpaConfiguration.class)
@SpringBootApplication
public class PocOrderManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocOrderManagementApplication.class, args);
    }

}
