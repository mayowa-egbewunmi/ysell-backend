package com.example.ysell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableFeignClients
public class YsellApplication {
    @RequestMapping("/test")
    String home(){
        return "Yeaaaaah!!! It worked....";
    }

	public static void main(String[] args) {
		SpringApplication.run(YsellApplication.class, args);
	}
}