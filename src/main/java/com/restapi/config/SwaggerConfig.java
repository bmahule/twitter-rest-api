package com.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by bmahule on 10/17/17.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket twitterRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.restapi"))
                //.paths(regex("/twitter-api.*"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot REST API for Twitter-like application",
                "Spring Boot REST API for Twitter-like application",
                "1.0",
                "Terms of service",
                new Contact("Bhagyashri Mahule", "www.intuit.com", "bhagyashri_mahule@intuit.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }
}
