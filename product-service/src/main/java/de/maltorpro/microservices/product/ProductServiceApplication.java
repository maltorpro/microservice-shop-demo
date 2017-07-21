package de.maltorpro.microservices.product;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"de.maltorpro.microservices.util", "de.maltorpro.microservices.product"})
public class ProductServiceApplication {


    public static void main(String[] args) {

       SpringApplication.run(ProductServiceApplication.class, args);

    }
}