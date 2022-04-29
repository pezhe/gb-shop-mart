package ru.gb.gbshopmart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.gb.gbshopmart.dao.ProductImageDao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageDao productImageDao;

    private String getImageForSpecificProduct(Long id) {
        return productImageDao.findImageNameByProductId(id);
    }

    public BufferedImage loadFileAsResource(Long id) throws IOException {
        try {
            String imageName = getImageForSpecificProduct(id);
            Resource resource = new ClassPathResource("/static/images/" + imageName);
            if (resource.exists()) {
                return ImageIO.read(resource.getFile());
            } else {
                log.error("Image not found!");
                throw new FileNotFoundException("File " + imageName + " not found!");
            }
        } catch (MalformedInputException | FileNotFoundException ex) {
            return null;
        }
    }

}