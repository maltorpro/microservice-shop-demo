package de.maltorpro.shop.configuration;

import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.DiscoveryClient;

@Configuration
public class SslDiscoveryClientConfiguration {

    private static final Logger logger = LoggerFactory
            .getLogger(LoggerConfiguration.class);

    @Value("${server.ssl.discovery-client-key-store}")
    private File trustStore;
    @Value("${server.ssl.key-store-password}")
    private String trustStorePassword;

    @Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs getTrustStoredEurekaClient(
            SSLContext sslContext) {
        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
        args.setSSLContext(sslContext);
        return args;
    }

    @Bean
    public SSLContext sslContext() throws Exception {
        logger.info("initialize ssl context bean with keystore {} ",
                trustStore);
        return new SSLContextBuilder()
                .loadTrustMaterial(trustStore, trustStorePassword.toCharArray())
                .build();
    }
}
