package com.telerikacademy.web.photocontest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(new Tag("Contest Rest Controller", "Contest management API"),
                        new Tag("User Rest Controller", "User management API"),
                        new Tag("Photo Rest Controller", "Photo management API"),
                        new Tag("Category Rest Controller", "Photo management API"))
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.telerikacademy.web.photocontest.controllers.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Photo Contest API")
                .description("API for Photo Contest project")
                .contact(new Contact("Team 12", "https://photo-contest.herokuapp.com/", "junkiesyardteam@gmail.com"))
                .license("Apache 2.0")
                .license("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
