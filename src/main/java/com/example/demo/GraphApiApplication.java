package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphApiApplication {
	public static void main(String[] args) {
		System.setProperty("datastax-java-driver.basic.request.timeout", "300 seconds");
		SpringApplication.run(GraphApiApplication.class, args);
	}
}
