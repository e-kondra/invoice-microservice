package com.invoiceservice.invoiceservice.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.invoiceservice"))
                .paths(PathSelectors.ant("/error").negate())
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.INVOICE,
                        "Used to get, create, update and delete invoices"),
                new Tag(DescriptionVariables.ORDER_DETAILS,
                        "Controller used to get, create, update and delete order details"),
                new Tag(DescriptionVariables.CASH_RECEIPT,
                        "Controller used to get, create, update and delete cash receipt of invoice"),
                new Tag(DescriptionVariables.DOCUMENT_NUMBER,
                        "Controller used to get numbers of cash receipts and invoices")
        );
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Client systems API")
                .description("Client management systems API")
                .version("1.0")
                .build();
    }
}