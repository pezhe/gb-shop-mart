package ru.gb.gbshopmart.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.service.CategoryService;
import ru.gb.gbshopmart.service.ManufacturerService;
import ru.gb.gbshopmart.service.ProductImageService;
import ru.gb.gbshopmart.service.ProductService;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;

    @GetMapping("/all")
    public String getProductList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/product-list";
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('product.create', 'product.update')")
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        ProductDto productDto;
        if (id != null) {
            productDto = productService.findById(id);
        } else {
            productDto = new ProductDto();
        }
        model.addAttribute("productDto", productDto); // вопрос
        model.addAttribute("categoryService", categoryService);
        model.addAttribute("manufacturers", manufacturerService.findAll());
        return "product/product-form";
    }

    @GetMapping("/{productId}")
    public String info(Model model, @PathVariable(name = "productId") Long id) {
        ProductDto productDto;
        if (id != null) {
            productDto = productService.findById(id);
        } else {
            return "redirect:/product/all";
        }
        model.addAttribute("product", productDto);
        return "product/product-info";
    }

    // @DateTimeFormat если будут проблемы с получением даты из шаблона подставитьт эту аннотацию
    @PostMapping
    @PreAuthorize("hasAnyAuthority('product.create', 'product.update')")
    public String saveProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("file") MultipartFile file) {
        if (productDto.getId() == null && productService.findByTitle(productDto.getTitle()).isPresent()) {
            bindingResult.addError(new ObjectError("productDto.title", "Товар с таким названием уже существует"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("productDto", productDto);
            model.addAttribute("categoryService", categoryService);
            model.addAttribute("manufacturers", manufacturerService.findAll());
            return "product/product-form";
        }
        productService.save(productDto, file);
        return "redirect:/product/all";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyAuthority('product.delete')")
    public String deleteById(@RequestParam(name = "id") Long id) {
        productService.deleteById(id);
        return "redirect:/product/all";
    }

    // todo ДЗ* - сделать поддержку множества картинок для для страницы подробной информации с продуктами
    @GetMapping(value = "images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable Long id) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(productImageService.loadProductImageAsResource(id), "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw e; // todo ДЗ - заменить на ProductImageNotFoundException
        }
    }

}
