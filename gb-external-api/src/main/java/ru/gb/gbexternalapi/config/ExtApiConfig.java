package ru.gb.gbexternalapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.gb.gbexternalapi.client.CategoryClient;
import ru.gb.gbexternalapi.client.ManufacturerClient;
import ru.gb.gbexternalapi.client.ProductClient;

@Configuration
@EnableFeignClients(basePackageClasses = {ProductClient.class, CategoryClient.class, ManufacturerClient.class})
public class ExtApiConfig {
}
