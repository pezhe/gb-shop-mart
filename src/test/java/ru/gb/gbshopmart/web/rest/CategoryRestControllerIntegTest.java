package ru.gb.gbshopmart.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbshopmart.entity.Category;
import ru.gb.gbshopmart.entity.Manufacturer;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CategoryRestControllerIntegTest {

    private final static String CATEGORY_TITLE = "Fruit";
    private final static String UPDATED_CATEGORY_TITLE = "Vegetables";
    private final static String CATEGORY_URI = "/api/v1/category";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testSaveManufacturerTest() throws Exception {

        mockMvc.perform(post(CATEGORY_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(CategoryDto.builder()
                                        .title(CATEGORY_TITLE)
                                        .build())))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void findAllCategoryTest() throws Exception {

        mockMvc.perform(get(CATEGORY_URI))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value(CATEGORY_TITLE));
    }

    @Test
    @Order(3)
    public void getCategoryBeforeUpdateTest() throws Exception {
        mockMvc.perform(get(CATEGORY_URI + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(CATEGORY_TITLE));
    }

    @Test
    @Order(4)
    public void handleUpdateTest() throws Exception {
        mockMvc.perform(put(CATEGORY_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(CategoryDto.builder()
                                .title(UPDATED_CATEGORY_TITLE)
                                .build()
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    public void getCategoryAfterUpdateTest() throws Exception {
        mockMvc.perform(get(CATEGORY_URI + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(UPDATED_CATEGORY_TITLE));
    }

    @Test
    @Order(6)
    public void deleteCategoryTest() throws Exception {
        mockMvc.perform(delete(CATEGORY_URI + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(7)
    public void getZeroSizeCategoriesTest() throws Exception {
        mockMvc.perform(get(CATEGORY_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}