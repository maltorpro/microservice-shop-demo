package de.maltorpro.shop.service.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({ "de.maltorpro.shop.service.review", "de.maltorpro.shop.util", "de.maltorpro.shop.model" })
@EntityScan({ "de.maltorpro.shop.model" })
public class ReviewServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReviewServiceApplication.class, args);

	}
}
