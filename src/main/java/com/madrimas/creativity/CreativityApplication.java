package com.madrimas.creativity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class CreativityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreativityApplication.class, args);
//		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("creativity");
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//		entityManager.close();
//		entityManagerFactory.close();
//
//		System.out.println("Done!");
	}

}
