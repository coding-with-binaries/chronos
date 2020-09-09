package com.manager.timezone.timezonemanagerserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.OAS_30).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).paths(Predicate.not(PathSelectors.regex("/error.*"))).build()
                .securitySchemes(Collections.singletonList(apiKey())).apiInfo(apiInfo()).groupName("timezone-manager")
                .securityContexts(Collections.singletonList(securityContext()));
    }

    public ApiInfo apiInfo() {
        Contact contact = new Contact("Varun Sharma", "https://github.com/coding-with-binaries",
                "coding.with.binaries@gmail.com");
        return new ApiInfoBuilder().title("Time Zone Manager API").description("Time Zone Manager API").contact(contact)
                .version("1.0.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
    }
}
