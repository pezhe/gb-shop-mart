package ru.gb.gbshopmart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.gb.gbshopmart.dao.ProductImageDao;
import ru.gb.gbshopmart.exceptions.StorageException;
import ru.gb.gbshopmart.exceptions.StorageFileNotFoundException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageDao productImageDao;

    private static final String UPLOADED_FOLDER = "/static/images/";
    private static final String path = "products";

    @Value("${storage.location}")
    private String storagePath;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(storagePath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.error("Ошибка при создании хранилища файлов "  + rootLocation.toAbsolutePath());
            throw new StorageException("Error while creating file storage " + rootLocation.toAbsolutePath(), e);
        }
    }


    public BufferedImage loadProductImageAsResource(Long id) throws IOException {
        try {
            String imageName = productImageDao.findImageNameByProductId(id);
            Resource resource = loadAsResource(path, imageName);
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

    private Resource loadAsResource(String path, String filename) {
        Path file = rootLocation.resolve(path).resolve(filename);
        try {
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Inside directory {} file with name {} not found", path, filename);
                throw new StorageFileNotFoundException("Inside directory " + path + " file with name " + filename + " not found");
            }
        } catch (MalformedURLException e) {
            log.error("Inside directory {} file with name {} not found", path, filename);
            throw new StorageFileNotFoundException("Inside directory " + path + " file with name " + filename + " not found", e);
        }
    }

    public String save(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        return save(filename, file);
    }

    public String save(String filename, MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("File " + filename + " is empty");
        }

        if (filename.contains("..")) {
            log.error("Trying to save file outside current directory " + filename);
            throw new StorageException("Incorrect filename " + filename);
        }

        try {
            Files.createDirectories(rootLocation.resolve(path));
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(rootLocation.resolve(path))) {
                for (Path child : dirStream) {
                    if (child.getFileName().toString().equals(filename)) {
                        throw new StorageException("File with nsuch name already exists " + rootLocation.resolve(path));
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error while create file storage" + rootLocation.toAbsolutePath());
            throw new StorageException("Error while create file storage", e);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(path).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error while saving storage" + filename);
            throw new StorageException("Error while saving storage" + filename, e);
        }
        return filename;
    }

}