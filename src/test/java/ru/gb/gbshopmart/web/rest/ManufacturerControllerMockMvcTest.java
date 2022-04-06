package ru.gb.gbshopmart.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbshopmart.entity.Manufacturer;
import ru.gb.gbshopmart.service.ManufacturerService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ManufacturerController.class)
class ManufacturerControllerMockMvcTest {

    @MockBean
    ManufacturerService manufacturerService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void findAllTest() throws Exception {
        List<ManufacturerDto> manufacturers = new ArrayList<>();

        manufacturers.add(ManufacturerDto.builder().manufacturerId(1L).name("Apple").build());
        manufacturers.add(ManufacturerDto.builder().manufacturerId(2L).name("Microsoft").build());

        given(manufacturerService.findAll()).willReturn(manufacturers);

        mockMvc.perform(get("/api/v1/manufacturer"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].name").value("Apple"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }

    @Test
    void testSaveManufacturerTest() throws Exception {

        given(manufacturerService.save(any())).willReturn(new ManufacturerDto(3L, "Tesla"));

        mockMvc.perform(post("/api/v1/manufacturer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(Manufacturer.builder()
                                .name("Tesla")
                                .build())))
                .andExpect(status().isCreated());
    }
}