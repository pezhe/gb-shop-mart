package ru.gb.gbshopmart.web.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.entity.Category;
import ru.gb.gbshopmart.entity.Manufacturer;
import ru.gb.gbshopmart.entity.Product;

import java.util.NoSuchElementException;
import java.util.Set;

@Mapper(uses = {ManufacturerMapper.class, CategoryMapper.class})
public interface ProductMapper {
    Product toProduct(ProductDto productDto,
                      @Context ManufacturerDao manufacturerDao,
                      @Context CategoryDao categoryDao);

    ProductDto toProductDto(Product product);

    default Manufacturer getManufacturer(String manufacturer,
                                         @Context ManufacturerDao manufacturerDao) {
        return manufacturerDao.findByName(manufacturer).orElseThrow(NoSuchElementException::new);
    }

    default String getManufacturer(Manufacturer manufacturer) {
        return manufacturer.getName();
    }

    Set<String> categorySetToStringSet(Set<Category> categories);

    Set<Category> stringSetToCategorySet(Set<String> categories, @Context CategoryDao categoryDao);

    default String categoryToString(Category category) {
        return category.getTitle();
    }

    default Category stringToCategory(String category, @Context CategoryDao categoryDao) {
        return categoryDao.findByTitle(category).orElseThrow(NoSuchElementException::new);
    }

}
