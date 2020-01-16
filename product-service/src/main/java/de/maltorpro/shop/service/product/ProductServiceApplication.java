package de.maltorpro.shop.service.product;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import de.maltorpro.shop.utils.SSLUtils;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@ComponentScan({ "de.maltorpro.shop.service.product", "de.maltorpro.shop.utils",
        "de.maltorpro.shop.model", "de.maltorpro.shop.configuration" })
@EntityScan({ "de.maltorpro.shop.model" })
public class ProductServiceApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args)
            throws KeyManagementException, NoSuchAlgorithmException {

        String certificateCheckProp = System.getProperty("certificateCheck");
        String certificateCheckEnv = System.getenv("CERTIFICATE_CHECK");

        if (StringUtils.equals(certificateCheckProp, "false")
                || StringUtils.equals(certificateCheckProp, "0")
                || StringUtils.equals(certificateCheckEnv, "false")
                || StringUtils.equals(certificateCheckEnv, "0")) {

            SSLUtils.turnOffSslChecking();
        }

        SpringApplication.run(ProductServiceApplication.class, args);

    }
}