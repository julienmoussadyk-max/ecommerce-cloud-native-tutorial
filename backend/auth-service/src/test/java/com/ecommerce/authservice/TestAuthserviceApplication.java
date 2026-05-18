package com.ecommerce.authservice;

import org.springframework.boot.SpringApplication;

public class TestAuthserviceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuthServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
