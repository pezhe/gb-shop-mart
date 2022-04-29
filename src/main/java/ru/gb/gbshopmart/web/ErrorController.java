package ru.gb.gbshopmart.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gb.gbshopmart.exceptions.ProductNotFoundException;

@Controller
@RequestMapping("/errors")
@Slf4j
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(final ProductNotFoundException ex) {
        log.error("Product not found thrown", ex);
        return "not-found";
    }
}
