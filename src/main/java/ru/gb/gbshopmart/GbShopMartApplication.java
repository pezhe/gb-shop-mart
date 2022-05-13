package ru.gb.gbshopmart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbapi.common.enums.Status;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.entity.Product;
import ru.gb.gbshopmart.service.CategoryService;
import ru.gb.gbshopmart.service.ManufacturerService;
import ru.gb.gbshopmart.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

@SpringBootApplication
public class GbShopMartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbShopMartApplication.class, args);

    }

    @Bean
    public CommandLineRunner boot(CategoryService categoryService,
                                  ManufacturerService manufacturerService,
                                  ProductService productService) {
        return (args -> {
//            categoryService.save(CategoryDto.builder().title("Напитки").build());
//            manufacturerService.save(ManufacturerDto.builder().name("Pepsi Co").build());
//            productService.save(ProductDto.builder()
//                            .categories(new HashSet<>(categoryService.findAll()))
//                            .manufacturer(manufacturerService.findById(1L).getName())
//                            .title("Fanta")
//                            .cost(new BigDecimal("100.00"))
//                            .manufactureDate(LocalDate.now())
//                            .status(Status.ACTIVE)
//                    .build());
        });
    }

}
