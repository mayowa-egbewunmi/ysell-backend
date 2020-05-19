package com.example.ysell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class YsellApplication {

    @RequestMapping("/test")
    String home(){
        return "Yaaaay!!! It worked....";
    }
	public static void main(String[] args) {
		SpringApplication.run(YsellApplication.class, args);
	}
}
