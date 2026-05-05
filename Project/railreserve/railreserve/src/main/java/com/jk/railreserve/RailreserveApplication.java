package com.jk.railreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RailreserveApplication {

	public static void main(String[] args) {
		SpringApplication.run(RailreserveApplication.class, args);
	}

}
