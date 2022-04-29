package ru.gb.gbshopmart.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbshopmart.service.ManufacturerService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manufacturer")
@Slf4j
public class ManufacturerRestController {
    
    private final ManufacturerService manufacturerService;

    @GetMapping
    public List<ManufacturerDto> getManufacturerList() {
        return manufacturerService.findAll();
    }

    @GetMapping("/{manufacturerId}")
    public ResponseEntity<?> getManufacturer(@PathVariable("manufacturerId") Long id) {
        ManufacturerDto manufacturer;
        if (id != null) {
            manufacturer = manufacturerService.findById(id);
            if (manufacturer != null) {
                return new ResponseEntity<>(manufacturer, HttpStatus.OK);
            }
        }
        log.error("Retryer");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    public ResponseEntity<?> handlePost(@Validated @RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto savedManufacturer = manufacturerService.save(manufacturerDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/manufacturer/" + savedManufacturer.getManufacturerId()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{manufacturerId}")
    public ResponseEntity<?> handleUpdate(@PathVariable("manufacturerId") Long id, @Validated @RequestBody ManufacturerDto manufacturerDto) {
        manufacturerDto.setManufacturerId(id);
        manufacturerService.save(manufacturerDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{manufacturerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("manufacturerId") Long id) {
        manufacturerService.deleteById(id);
    }
}
