package ru.gb.gbshopmart.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.gbshopmart.entity.Category;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerIntegTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    void handlePostTest() throws Exception {

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(Category.builder()
                                        .title("Food")
                                        .build())))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(Category.builder()
                                        .title("Drinks")
                                        .build())))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void getCategoryListTest() throws Exception {

        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("Food"))
                .andExpect(jsonPath("$.[1].title").value("Drinks"));

    }

    @Test
    @Order(3)
    void handleUpdateTest() throws Exception {

        mockMvc.perform(put("/api/v1/category/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(Category.builder()
                                        .title("Alcohol")
                                        .build())))
                .andExpect(status().isNoContent());

    }

    @Test
    @Order(4)
    void getCategoryTest() throws Exception {

        mockMvc.perform(get("/api/v1/category/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Drink"))))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Alcohol"));

    }

    @Test
    @Order(5)
    void deleteByIdTest() throws Exception {

        mockMvc.perform(delete("/api/v1/category/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Food"))))
                .andExpect(jsonPath("$", hasSize(1)));

    }

}