package com.baeldung.application;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.baeldung.application.entities.User;
import com.baeldung.application.repositories.UserRepository;
import com.baeldung.rabbitmq.RabbitStreamProcessor;

@EnableBinding(RabbitStreamProcessor.class)
@SpringBootApplication(scanBasePackages = { "com.baeldung.application", "com.baeldung.ecommerce",
		"com.baeldung.rabbitmq", "com.baeldung" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
				User user = new User(name, name.toLowerCase() + "@domain.com");
				userRepository.save(user);
			});
			userRepository.findAll().forEach(System.out::println);
		};
	}
}
