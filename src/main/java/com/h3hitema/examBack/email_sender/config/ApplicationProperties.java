package com.h3hitema.examBack.email_sender.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;

@ConfigurationProperties(prefix = "application")
@PropertySource("classpath:application.properties") // we need to config tomcat
@Getter
@Setter
public class ApplicationProperties {

    private URL url;

    @Value("${application.mail.from}")
    private String mailFrom;
}
