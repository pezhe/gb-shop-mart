package ru.gb.gbshopmart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.gb.gbapi.common.enums.Status;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.dao.ProductDao;
import ru.gb.gbshopmart.entity.Product;
import ru.gb.gbshopmart.entity.ProductImage;
import ru.gb.gbshopmart.web.dto.mapper.ProductMapper;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductDao productDao;
    private final ManufacturerDao manufacturerDao;
    private final ProductMapper productMapper;
    private final CategoryDao categoryDao;
    private final ProductImageService productImageService;

    @Transactional(propagation = Propagation.NEVER, isolation = Isolation.DEFAULT)
    public long count() {
        System.out.println(productDao.count());
        return productDao.count();
    }

    @Transactional
    public ProductDto save(final ProductDto productDto, MultipartFile file) {
        Product product = productMapper.toProduct(productDto, manufacturerDao, categoryDao);

        if (file !=null && !file.isEmpty()) {
            String pathToSavedImage = productImageService.save(file);
            ProductImage productImage = ProductImage.builder()
                    .path(pathToSavedImage)
                    .product(product)
                    .build();
            product.addImage(productImage);
        }

        if (product.getId() != null) {
            productDao.findById(productDto.getId()).ifPresent(
                    (p) -> product.setVersion(p.getVersion())
            );
        }
        if (product.getManufactureDate() == null) {
            product.setManufactureDate(LocalDate.now());
        }
        return productMapper.toProductDto(productDao.save(product));
    }


    @Transactional
    public ProductDto save(final ProductDto productDto) {
        return save(productDto, null);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findByTitle(String title) {
        return productDao.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return productMapper.toProductDto(productDao.findById(id).orElse(null));
    }

    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }


    public List<ProductDto> findAll() {
        return productDao.findAll().stream().map(productMapper::toProductDto).collect(Collectors.toList());
    }

    public List<Product> findAllActive() {
        return productDao.findAllByStatus(Status.ACTIVE);
    }

    public void deleteById(Long id) {
        try {
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void disable(Long id) {
        Optional<Product> product = productDao.findById(id);
        product.ifPresent(p -> {
            p.setStatus(Status.DISABLED);
            productDao.save(p);
        });
    }

    public List<Product> findAll(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
    }

    public List<Product> findAllSortedById() {
        return productDao.findAllByStatus(Status.ACTIVE, Sort.by("id"));
    }

    public List<Product> findAllSortedById(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size, Sort.by("id")));
    }

}
