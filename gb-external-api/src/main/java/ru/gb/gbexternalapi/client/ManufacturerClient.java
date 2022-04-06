package ru.gb.gbexternalapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.gb.gbapi.manufacturer.api.ManufacturerGateway;

@FeignClient(url = "http://localhost:8080/internal/api/v1/manufacturer", name = "ManufacturerGateway")
public interface ManufacturerClient extends ManufacturerGateway {
}
