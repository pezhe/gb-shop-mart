package ru.gb.gbshopmart.web.dto.mapper;

import org.mapstruct.Mapper;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbshopmart.entity.Category;

@Mapper
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

}
