package com.example.security;

import com.example.security.Authentification.role.Role;
import com.example.security.Authentification.role.RoleRepository;
import com.example.security.entity.ServiceOffert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.security.dao.ServiceOffertRepository;
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
			if(roleRepository.findByName("ADMIN_SECONDAIRE").isEmpty()){
				roleRepository.save(Role.builder().name("ADMIN_SECONDAIRE").build());
			}
			if(roleRepository.findByName("ETUDIANT").isEmpty()){
				roleRepository.save(Role.builder().name("ETUDIANT").build());
			}
			if(roleRepository.findByName("PARTICULIER").isEmpty()){
				roleRepository.save(Role.builder().name("PARTICULIER").build());
			}
			if(roleRepository.findByName("ENTREPRENEUR").isEmpty()){
				roleRepository.save(Role.builder().name("ENTREPRENEUR").build());
			}
			if(roleRepository.findByName("PORTE_VISA").isEmpty()){
				roleRepository.save(Role.builder().name("PORTE_VISA").build());
			}

		});
	}


	@Bean
	public CommandLineRunner serviceInitializer(ServiceOffertRepository serviceOffertRepository) {
		return args -> {
			if (serviceOffertRepository.findByName("Orientation_Académique").isEmpty()) {
				serviceOffertRepository.save(ServiceOffert.builder().name("Orientation_Académique").build());
			}
			if (serviceOffertRepository.findByName("Gestion_des_Visas").isEmpty()) {
				serviceOffertRepository.save(ServiceOffert.builder().name("Gestion_des_Visas").build());
			}
			if (serviceOffertRepository.findByName("Assistance_Administrative").isEmpty()) {
				serviceOffertRepository.save(ServiceOffert.builder().name("Assistance_Administrative").build());
			}
			if (serviceOffertRepository.findByName("Conseils_de_Gestion").isEmpty()) {
				serviceOffertRepository.save(ServiceOffert.builder().name("Conseils_de_Gestion").build());
			}
		};
	}
}