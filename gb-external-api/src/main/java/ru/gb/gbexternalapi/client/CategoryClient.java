package ru.gb.gbexternalapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.gb.gbapi.category.api.CategoryGateway;

@FeignClient(url = "http://localhost:8080/internal/api/v1/category", name = "CategoryGateway")
public interface CategoryClient extends CategoryGateway {

}
