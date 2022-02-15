package com.thiki.ec.component.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleWebApp {


    public static void main(String[] args) {
        SpringApplication.run(SampleWebApp.class, args);
    }
}
