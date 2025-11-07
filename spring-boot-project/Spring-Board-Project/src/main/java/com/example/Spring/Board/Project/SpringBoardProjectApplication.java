package com.example.Spring.Board.Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBoardProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoardProjectApplication.class, args);
	}

}
