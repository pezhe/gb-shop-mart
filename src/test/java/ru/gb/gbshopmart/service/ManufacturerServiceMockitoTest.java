package ru.gb.gbshopmart.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.gb.gbapi.manufacturer.dto.ManufacturerDto;
import ru.gb.gbshopmart.dao.ManufacturerDao;
import ru.gb.gbshopmart.entity.Manufacturer;
import ru.gb.gbshopmart.web.dto.mapper.ManufacturerMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceMockitoTest {

    @Mock
    ManufacturerDao manufacturerDao;

    @Mock
    ManufacturerMapper manufacturerMapper;

    @InjectMocks
    ManufacturerService manufacturerService;

    @Test
    void findAllManufacturersTest() {
        // given
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturers.add(Manufacturer.builder()
                .id(1L)
                .name("Apple")
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .lastModifiedBy("user1")
                .lastModifiedDate(LocalDateTime.now())
                .version(1)
                .build());
        manufacturers.add(Manufacturer.builder()
                .id(2L)
                .name("Microsoft")
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .lastModifiedBy("user1")
                .lastModifiedDate(LocalDateTime.now())
                .version(1)
                .build());
        given(manufacturerDao.findAll()).willReturn(manufacturers);

        // when
        List<ManufacturerDto> manufacturerDtos = manufacturerService.findAll();

        // then
        then(manufacturerDao).should().findAll();
        assertEquals(manufacturers.size(), manufacturerDtos.size());
    }

    @Test
    void saveManufacturerTest() {
        // given
        Manufacturer manufacturerFromDao = Manufacturer.builder()
                .id(1L)
                .name("Apple")
                .createdBy("user1")
                .createdDate(LocalDateTime.now())
                .lastModifiedBy("user1")
                .lastModifiedDate(LocalDateTime.now())
                .version(1)
                .build();
        ManufacturerDto apple = new ManufacturerDto(null, "Apple");
        given(manufacturerDao.save(any(Manufacturer.class))).willReturn(manufacturerFromDao);
        given(manufacturerMapper.toManufacturer(any())).will(new ToManufacturer());
        given(manufacturerMapper.toManufacturerDto(any())).will(new ToManufacturerDto());

        // when
        ManufacturerDto returnedManufacturerDto = manufacturerService.save(apple);

        // then
        then(manufacturerDao).should().save(any(Manufacturer.class));
        assertEquals(1L, returnedManufacturerDto.getManufacturerId());
    }

}

class ToManufacturer implements Answer<Manufacturer> {

    @Override
    public Manufacturer answer(InvocationOnMock invocation) throws Throwable {
        ManufacturerDto manufacturerDto = (ManufacturerDto) invocation.getArgument(0);
        if ( manufacturerDto == null ) {
            return null;
        }

        Manufacturer.ManufacturerBuilder manufacturer = Manufacturer.builder();

        manufacturer.id( manufacturerDto.getManufacturerId() );
        manufacturer.name( manufacturerDto.getName() );

        return manufacturer.build();
    }
}

class ToManufacturerDto implements Answer<ManufacturerDto> {

    @Override
    public ManufacturerDto answer(InvocationOnMock invocation) throws Throwable {
        Manufacturer manufacturer = (Manufacturer) invocation.getArgument(0);
        if ( manufacturer == null ) {
            return null;
        }

        ManufacturerDto.ManufacturerDtoBuilder manufacturerDto = ManufacturerDto.builder();

        manufacturerDto.manufacturerId( manufacturer.getId() );
        manufacturerDto.name( manufacturer.getName() );

        return manufacturerDto.build();
    }
}