package io.github.prefeituradorecife.jogospessoaidosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JogospessoaidosaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JogospessoaidosaApplication.class, args);
	}

}
