package de.maltorpro.shop.service.recommendation;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({
	"de.maltorpro.microservices.recommendation",
	"dde.maltorpro.shop.config",
	"de.maltorpro.microservices.util",
	"de.maltorpro.microservices.model"})
public class RecommendationServiceApplication {

	@Bean
	public Docket recommendationApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(regex("/recommendation.*")).build();
	}

	public static void main(String[] args) {

		SpringApplication.run(RecommendationServiceApplication.class, args);

	}
}