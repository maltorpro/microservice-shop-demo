package de.maltorpro.shop.service.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({
	"de.maltorpro.shop.service.recommendation",
	"de.maltorpro.shop.util",
	"de.maltorpro.shop.model"})
public class RecommendationServiceApplication {


	public static void main(String[] args) {

		SpringApplication.run(RecommendationServiceApplication.class, args);

	}
}