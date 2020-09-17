package com.syf.develop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserEventApp {

	public static void main(String[] args) {
		SpringApplication.run(UserEventApp.class, args);
	}

}
