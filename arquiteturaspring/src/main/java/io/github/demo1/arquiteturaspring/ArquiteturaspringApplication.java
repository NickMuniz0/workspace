package io.github.demo1.arquiteturaspring;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class ArquiteturaspringApplication {

	public static void main(String[] args) {

//		SpringApplication.run(ArquiteturaspringApplication.class, args);

		SpringApplicationBuilder builder = new SpringApplicationBuilder(ArquiteturaspringApplication.class);
		builder.bannerMode(Banner.Mode.OFF);
		builder.profiles("producao");
		builder.run(args);



		ConfigurableApplicationContext applicationContext = builder.context();

//		var produtoRepository = applicationContext.getBean("produtoRepository");

//		builder.properties("spring.datasource.url=jdbc://");


//		ConfigurableEnvironment environment = applicationContext.getEnvironment();
//		String applicationName  = environment.getProperty("spring.application.name");
//		System.out.println(applicationName);



	}

}
