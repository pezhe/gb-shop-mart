package ru.gb.gbexternalapi.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.manufacturer.api.ManufacturerGateway;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manufacturer")
public class ManufacturerController {

    private final ManufacturerGateway manufacturerGateway;

    @GetMapping
    List<ManufacturerDto> getManufacturerList() {
        return manufacturerGateway.getManufacturerList();
    }

    @GetMapping({"/{manufacturerId}"})
    ResponseEntity<ManufacturerDto> getManufacturer(@PathVariable("manufacturerId") Long id) {
        return manufacturerGateway.getManufacturer(id);
    }

    @PostMapping
    ResponseEntity<ManufacturerDto> handlePost(@Validated @RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerGateway.handlePost(manufacturerDto);
    }

    @PutMapping({"/{manufacturerId}"})
    ResponseEntity<ManufacturerDto> handleUpdate(@PathVariable("manufacturerId") Long id,
                                                 @Validated @RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerGateway.handleUpdate(id, manufacturerDto);
    }

    @DeleteMapping({"/{manufacturerId}"})
    void deleteById(@PathVariable("manufacturerId") Long id) {
        manufacturerGateway.deleteById(id);
    }
}
