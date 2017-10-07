package de.maltorpro.shop.service.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({ "de.maltorpro.shop.service.product", "de.maltorpro.shop.util", "de.maltorpro.shop.model",
		"de.maltorpro.shop.dto" })
@EntityScan({ "de.maltorpro.shop.model" })
public class ShopGuiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ShopGuiApplication.class, args);

	}
}