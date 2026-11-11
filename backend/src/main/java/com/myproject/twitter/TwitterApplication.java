package com.myproject.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TwitterApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TwitterApplication.class, args);

		for(String beanName : context.getBeanDefinitionNames()){

			System.out.println(beanName);
		}
	}

}
