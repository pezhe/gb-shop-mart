package ru.gb.gbshopmart.web.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbapi.product.dto.ProductDto;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.entity.Category;
import ru.gb.gbshopmart.entity.Manufacturer;
import ru.gb.gbshopmart.entity.Product;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {ManufacturerMapper.class, CategoryMapper.class})
public interface ProductMapper {
    Product toProduct(ProductDto productDto, @Context ManufacturerDao manufacturerDao, @Context CategoryDao categoryDao);

    ProductDto toProductDto(Product product);

    default Manufacturer getManufacturer(String manufacturer, @Context ManufacturerDao manufacturerDao) {
        return manufacturerDao.findByName(manufacturer).orElseThrow(
                () -> new NoSuchElementException("There isn't manufacturer with name " + manufacturer));
    }

    default String getManufacturer(Manufacturer manufacturer) {
        return manufacturer.getName();
    }

    // todo ДЗ - если что поменять здесь маппинг на list of ids to set of category
    default Set<Category> categoryDtoSetToCategorySet(Set<CategoryDto> categories, @Context CategoryDao categoryDao) {
        return categories.stream().map(c -> categoryDao.findById(c.getId())
                        .orElseThrow(
                                () -> new NoSuchElementException("There isn't category with id + " + c.getId()))
                )
                .collect(Collectors.toSet());
    }

}
