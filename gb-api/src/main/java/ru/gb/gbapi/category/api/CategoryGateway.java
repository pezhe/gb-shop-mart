package ru.gb.gbapi.category.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.category.dto.CategoryDto;

import java.util.List;

public interface CategoryGateway {
    
    @GetMapping
    List<CategoryDto> getCategoryList();

    @GetMapping("/{id}")
    ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<CategoryDto> handlePost(@Validated @RequestBody CategoryDto categoryDto);

    @PutMapping("/{id}")
    ResponseEntity<CategoryDto> handleUpdate(@PathVariable("id") Long id,
                                            @Validated @RequestBody CategoryDto categoryDto);

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable("id") Long id);
}
