package org.dabs.back;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppTest {
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
