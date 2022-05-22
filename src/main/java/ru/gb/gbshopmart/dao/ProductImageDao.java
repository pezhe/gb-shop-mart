package ru.gb.gbshopmart.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gb.gbshopmart.entity.ProductImage;

public interface ProductImageDao extends JpaRepository<ProductImage, Long> {

    @Query(value = "SELECT product_image.path FROM product_image WHERE product_image.product_id = :id ORDER BY " +
            "product_image.id LIMIT 1", nativeQuery = true)
    String findImageNameByProductId(@Param("id") Long id);

    @Query(value = "SELECT COUNT(product_image.product_id) FROM product_image WHERE product_image.product_id = :id",
            nativeQuery = true)
    Integer countImages(Long id);

    @Query(value = "SELECT product_image.path FROM product_image WHERE product_image.product_id = :id ORDER BY " +
            "product_image.id LIMIT 1 OFFSET :count", nativeQuery = true)
    String findImageNameByProductIdAndCount(@Param("id") Long id, @Param("count") Integer count);
}
