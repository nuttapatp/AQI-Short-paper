package com.api.des;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@ComponentScan
@EnableScheduling
@SpringBootApplication
@EnableAsync

public class DesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesApplication.class, args);
	}

}
