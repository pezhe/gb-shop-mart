package ru.gb.gbapi.product.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.product.dto.ProductDto;

import java.util.List;

public interface ProductGateway {

    @GetMapping
    List<ProductDto> getProductList();

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<ProductDto> handlePost(@Validated @RequestBody ProductDto productDto);

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> handleUpdate(@PathVariable("id") Long id,
                                                 @Validated @RequestBody ProductDto productDto);

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable("id") Long id);
}
