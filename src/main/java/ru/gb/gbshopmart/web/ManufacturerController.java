package ru.gb.gbshopmart.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbshopmart.service.ManufacturerService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manufacturer")
public class ManufacturerController {
    
    private final ManufacturerService manufacturerService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('manufacturer.read')")
    public String getManufacturerList(Model model) {
        List<ManufacturerDto> all = manufacturerService.findAll();
        model.addAttribute("manufacturers", manufacturerService.findAll());
        return "manufacturer/manufacturer-list";
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('manufacturer.create', 'manufacturer.update')")
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        ManufacturerDto manufacturerDto;
        if (id != null) {
            manufacturerDto = manufacturerService.findById(id);
        } else {
            manufacturerDto = new ManufacturerDto();
        }
        model.addAttribute("manufacturer", manufacturerDto);
        return "manufacturer/manufacturer-form";
    }

    @GetMapping("/{manufacturerId}")
    @PreAuthorize("hasAnyAuthority('manufacturer.read')")
    public String info(Model model, @PathVariable(name = "manufacturerId") Long id) {
        ManufacturerDto manufacturerDto;
        if (id != null) {
            manufacturerDto = manufacturerService.findById(id);
        } else {
            return "redirect:/manufacturer/all";
        }
        model.addAttribute(manufacturerDto);
        return "manufacturer/manufacturer-info";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('manufacturer.create', 'manufacturer.update')")
    public String saveManufacturer(ManufacturerDto manufacturer) {
        manufacturerService.save(manufacturer);
        return "redirect:/manufacturer/all";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('manufacturer.delete')")
    public String deleteById(@RequestParam(name = "id") Long id) {
        manufacturerService.deleteById(id);
        return "redirect:/manufacturer/all";
    }
}