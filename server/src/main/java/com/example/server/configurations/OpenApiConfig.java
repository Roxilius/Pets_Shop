package com.example.server.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI myOpenAPI(){

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Server URL in local environment");
        
        Contact contact = new Contact();
        contact.setEmail("info@gmail.com");
        contact.setName("Info");
        contact.setUrl("http://www.sisd-test.com");
        
        License mitLicense = new License()
        .name("MIT License")
        .url("http://mit-test.com");
        
        Info info = new Info()
        .title("Sistem Informasi Sekolah Dasar")
        .version("1.0")
        .contact(contact)
        .description("This API is use for sample project of java")
        .termsOfService("http://www.test.com/term")
        .license(mitLicense);

        SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
        .bearerFormat("JWT")
        .scheme("bearer");
        
        return new OpenAPI() .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme))
        .info(info).servers(List.of(localServer));
    }
}
