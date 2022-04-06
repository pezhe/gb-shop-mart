package ru.gb.gbexternalapi.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbexternalapi.client.ProductClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ProductClient productClient;

    @GetMapping
    public List<ProductDto> getProductList() {
        return productClient.getProductList();
    }

}
