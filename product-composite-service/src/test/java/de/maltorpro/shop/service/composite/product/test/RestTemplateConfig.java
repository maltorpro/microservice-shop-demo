package de.maltorpro.shop.service.composite.product.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("test")
@Configuration
public class RestTemplateConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
