package com.manager.timezone.timezonemanagerserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.function.Predicate;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.OAS_30).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).paths(Predicate.not(PathSelectors.regex("/error.*"))).build();
    }

    @Bean
    public ApiInfo apiInfo() {
        Contact contact = new Contact("Varun Sharma", "https://github.com/coding-with-binaries",
                "coding.with.binaries@gmail.com");
        return new ApiInfoBuilder().title("Time Zone Manager API").description("Time Zone Manager API").contact(contact)
                .version("1.0.0").build();
    }
}
