package com.example.security;

import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.role.RoleRepository;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class SecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialization(RoleRepository roleRepository){
		return (args -> {
			if(roleRepository.findByName("ADMIN").isEmpty()){
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
			if(roleRepository.findByName("ETUDIANT").isEmpty()){
				roleRepository.save(Role.builder().name("ETUDIANT").build());
			}
		});
	}

	}



