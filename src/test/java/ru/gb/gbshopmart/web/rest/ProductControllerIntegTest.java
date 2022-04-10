package ru.gb.gbshopmart.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbapi.common.enums.Status;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.entity.Category;
import ru.gb.gbshopmart.entity.Manufacturer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerIntegTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    void handlePostTest() throws Exception  {
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(Category.builder()
                                        .title("Food")
                                        .build())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/manufacturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(Manufacturer.builder()
                                        .name("Nestle")
                                        .build())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(ProductDto.builder()
                                        .title("Nestea")
                                        .cost(BigDecimal.valueOf(105.00))
                                        .manufactureDate(LocalDate.of(2022, 3, 8))
                                        .status(Status.ACTIVE)
                                        .manufacturer("Nestle")
                                        .categories(Collections.singleton(new CategoryDto(1L, "Food")))
                                        .build())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(ProductDto.builder()
                                        .title("Nesquik")
                                        .cost(BigDecimal.valueOf(99.00))
                                        .manufactureDate(LocalDate.of(2022, 4, 1))
                                        .status(Status.ACTIVE)
                                        .manufacturer("Nestle")
                                        .categories(Collections.singleton(new CategoryDto(2L, "Drinks")))
                                        .build())))
                .andExpect(status().isConflict());

    }

    @Test
    @Order(2)
    void getProductListTest() throws Exception  {
        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].title").value("Nestea"));
    }

    @Test
    @Order(3)
    void handleUpdateTest() throws Exception  {
        mockMvc.perform(put("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(ProductDto.builder()
                                        .title("Nesquik")
                                        .cost(BigDecimal.valueOf(99.00))
                                        .manufactureDate(LocalDate.of(2022, 4, 1))
                                        .status(Status.ACTIVE)
                                        .manufacturer("Nestle")
                                        .categories(Collections.singleton(new CategoryDto(1L, "Food")))
                                        .build())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void getProductTest() throws Exception  {
        mockMvc.perform(get("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Nesquik"))
                .andExpect(jsonPath("$.cost").value("99.0"))
                .andExpect(jsonPath("$.manufactureDate").value("01.04.2022"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.manufacturer").value("Nestle"))
                .andExpect(jsonPath("$.categories.[0].id").value("1"))
                .andExpect(jsonPath("$.categories.[0].title").value("Food"));
    }

    @Test
    @Order(5)
    void deleteByIdTest() throws Exception  {
        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}