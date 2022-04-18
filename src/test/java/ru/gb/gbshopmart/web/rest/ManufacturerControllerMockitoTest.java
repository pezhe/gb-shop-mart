package ru.gb.gbshopmart.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbshopmart.service.ManufacturerService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// BDD (Behavior Driven Development)
@ExtendWith(MockitoExtension.class)
class ManufacturerControllerMockitoTest {

    @Mock
    ManufacturerService manufacturerService;

    @InjectMocks
    ManufacturerController manufacturerController;

    List<ManufacturerDto> manufacturers = new ArrayList<>();

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        manufacturers.add(ManufacturerDto.builder().manufacturerId(1L).name("Apple").build());
        manufacturers.add(ManufacturerDto.builder().manufacturerId(2L).name("Microsoft").build());

        mockMvc = MockMvcBuilders.standaloneSetup(manufacturerController).build();
    }

    @Test
    void mockMvcGetManufacturerListTest() throws Exception {

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
                        .content("{\"name\": \"Tesla\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getManufacturerListTest() {
        // given
        given(manufacturerService.findAll()).willReturn(manufacturers);

        // when
        List<ManufacturerDto> manufacturerList = manufacturerController.getManufacturerList();

        // then
        then(manufacturerService).should().findAll();

        assertAll(
                () -> assertEquals(2, manufacturerList.size(), "Size must be equals 2"),
                () -> assertEquals("Apple", manufacturerList.get(0).getName())
        );
    }
}