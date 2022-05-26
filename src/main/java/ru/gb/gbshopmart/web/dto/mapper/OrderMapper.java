package ru.gb.gbshopmart.web.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.gb.gbapi.order.dto.OrderDto;
import ru.gb.gbshopmart.dao.CategoryDao;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.entity.Address;
import ru.gb.gbshopmart.entity.Order;

@Mapper(uses = {ProductMapper.class, ManufacturerMapper.class})
public interface OrderMapper {

    Order toOrder(OrderDto orderDto, @Context ManufacturerDao manufacturerDao, @Context CategoryDao categoryDao);

    OrderDto toOrderDto(Order order);

    default String addressToString(Address address) {
        return address.getAddress();
    }

    default Address stringToAddress(String s) {
        return Address.builder().address(s).build();
    }
}
