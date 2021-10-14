package br.com.site.springbootadminmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
// @SpringBootApplication
public class SpringBootAdminMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAdminMonitorApplication.class, args);
	}

}
