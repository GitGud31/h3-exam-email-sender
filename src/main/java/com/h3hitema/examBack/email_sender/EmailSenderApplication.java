package com.h3hitema.examBack.email_sender;

import com.h3hitema.examBack.email_sender.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {ReactiveSecurityAutoConfiguration.class, SecurityAutoConfiguration.class })
@EnableConfigurationProperties({ApplicationProperties.class})

public class EmailSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSenderApplication.class, args);
	}

}
