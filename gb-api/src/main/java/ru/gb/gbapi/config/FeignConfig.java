package ru.gb.gbapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.gb.gbapi.category.api.CategoryGateway;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbapi.manufacturer.api.ManufacturerGateway;
import ru.gb.gbapi.product.api.ProductGateway;

@Configuration
@EnableFeignClients(clients = {CategoryGateway.class,
        ManufacturerGateway.class,
        ProductGateway.class})
public class FeignConfig {
}
