package de.maltorpro.shop.service.product;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({ "de.maltorpro.shop.service.product", "de.maltorpro.shop.util", "de.maltorpro.shop.model" })
@EntityScan({ "de.maltorpro.shop.model" })
public class ProductServiceApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {

		SpringApplication.run(ProductServiceApplication.class, args);

	}
}