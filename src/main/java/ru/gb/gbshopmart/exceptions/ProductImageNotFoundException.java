package ru.gb.gbshopmart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductImageNotFoundException extends RuntimeException {

    public ProductImageNotFoundException(String message) {
        super(message);
    }

    public ProductImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}