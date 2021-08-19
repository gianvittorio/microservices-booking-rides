package com.gianvittorio.usersservice.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {

    @Value("${app.api.common.title}")
    private String apitTitle;

    @Value("${app.api.common.description}")
    private String apiDescription;

    @Value("${app.api.common.version}")
    private String apiVersion;

    @Value("${app.api.common.termsOfServiceUrl}")
    private String apiTermsOfServiceUrl;

    @Value("$app.api.common.contactName}")
    private String apiContactName;

    @Value("${app.api.common.contactUrl}")
    private String apiContactUrl;

    @Value("$app.api.common.contactEmail}")
    private String apiContactEmail;

    @Value("${app.api.common.apiLicense}")
    private String apiLicense;

    @Value("${app.api.common.apiLicenseUrl}")
    private String apiLicenseUrl;

    @Bean
    public Docket apiDocumentation(final ApiInfo apiInfo) {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gianvittorio.usersservice"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo);
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfo(
                apitTitle,
                apiDescription,
                apiVersion,
                apiTermsOfServiceUrl,
                new Contact(
                        apiContactName,
                        apiContactUrl,
                        apiContactEmail
                ),
                apiLicense,
                apiLicenseUrl,
                Collections.emptyList()
        );
    }
}
