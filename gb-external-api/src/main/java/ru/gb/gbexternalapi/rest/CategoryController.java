package ru.gb.gbexternalapi.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {


    private final CategoryGateway categoryGateway;


    @GetMapping
    public List<CategoryDto> getCategoryList() {
        return categoryGateway.getCategoryList();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") Long id) {
        return categoryGateway.getCategory(id);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> handlePost(@Validated @RequestBody CategoryDto categoryDto) {
        return categoryGateway.handlePost(categoryDto);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> handleUpdate(@PathVariable("categoryId") Long id, @Validated @RequestBody CategoryDto categoryDto) {
        return categoryGateway.handleUpdate(id, categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteById(@PathVariable("categoryId") Long id) {
        categoryGateway.deleteById(id);
    }

}
