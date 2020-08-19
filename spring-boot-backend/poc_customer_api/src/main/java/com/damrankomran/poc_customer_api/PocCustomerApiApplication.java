package com.damrankomran.poc_customer_api;

import com.damrankomran.poc_customer_api.config.JpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages={"com.damrankomran.poc_customer_api"})
@EnableCaching
public class PocCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PocCustomerApiApplication.class, args);
    }

}
