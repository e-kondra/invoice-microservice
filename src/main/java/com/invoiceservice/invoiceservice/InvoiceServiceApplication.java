package com.invoiceservice.invoiceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.invoiceservice.invoiceservice.business.repository.model"})
@ComponentScan(basePackages = {"com.invoiceservice.invoiceservice"})
@EnableDiscoveryClient
public class InvoiceServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvoiceServiceApplication.class, args);
	}

}
