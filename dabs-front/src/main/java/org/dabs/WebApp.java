package org.dabs;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * WebApp
 *
 */
@SpringBootApplication(scanBasePackages = "org.dabs")
public class WebApp 
{
	public static void main(String[] args) {
		SpringApplication.run(WebApp.class, args);
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
