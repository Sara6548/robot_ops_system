package com.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableScheduling
@EnableWebSocket
public class RobotSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RobotSystemApplication.class, args);
        System.out.println(" Robot System Backend is running!");
    }
}