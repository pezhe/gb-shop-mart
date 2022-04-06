package ru.gb.gbexternalapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.gb.gbapi.product.api.ProductGateway;

@FeignClient(url = "http://localhost:8080/internal/api/v1/product", name = "ProductGateway")
public interface ProductClient extends ProductGateway {
}
