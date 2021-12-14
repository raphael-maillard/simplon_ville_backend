package com.simplon.api;

import com.simplon.api.Config.AppProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("com.simplon")
@EnableConfigurationProperties(AppProperties.class)
@OpenAPIDefinition(info = @Info(title = "simplon_ville", version = "3.0", description = "Simplon ville Information"))
@SecurityScheme(name = "simplon_ville", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ApiApplication {


    private static String keyStore;

    private static String keyStorePassword;

    @Value("${server.ssl.key-store}")
    public void setKeyStore(String keyStore) {
        ApiApplication.keyStore = keyStore;
    }

    @Value("${server.ssl.key-store-password}")
    public void setKeyStorePassword(String keyStorePassword) {
        ApiApplication.keyStorePassword = keyStorePassword;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        System.setProperty("javax.net.ssl.trustStore", keyStore);
        System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

}
