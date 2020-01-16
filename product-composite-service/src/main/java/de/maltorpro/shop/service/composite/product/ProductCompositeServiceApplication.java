package de.maltorpro.shop.service.composite.product;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import de.maltorpro.shop.utils.SSLUtils;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class })
@ComponentScan({ "de.maltorpro.shop.service.composite.product",
        "de.maltorpro.shop.utils", "de.maltorpro.shop.model",
        "de.maltorpro.shop.configuration" })
public class ProductCompositeServiceApplication {

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

        SpringApplication.run(ProductCompositeServiceApplication.class, args);

    }
}