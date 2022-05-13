package ru.gb.gbshopmart.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.web.dto.mapper.CategoryMapper;


@Component
@RequiredArgsConstructor
public class CategoryConverter implements Converter<String, CategoryDto> {

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto convert(String source) {
        return categoryMapper.toCategoryDto(categoryDao.findById(Long.valueOf(source)).orElse(null));
    }
}
