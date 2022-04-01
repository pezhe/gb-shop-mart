package ru.gb.gbapi.config;

import feign.*;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import ru.gb.gbapi.category.api.CategoryGateway;
import ru.gb.gbapi.category.dto.CategoryDto;
import ru.gb.gbapi.manufacturer.api.ManufacturerGateway;
import ru.gb.gbapi.product.api.ProductGateway;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static feign.FeignException.errorStatus;
import static feign.Util.RETRY_AFTER;

@Configuration
@EnableFeignClients(clients = {CategoryGateway.class,
        ProductGateway.class})
@EnableConfigurationProperties(GbApiProperties.class)
@RequiredArgsConstructor
public class FeignConfig {

    private final GbApiProperties gbApiProperties;
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public ManufacturerGateway manufacturerGateway() {
        return Feign.builder()
                .encoder(new SpringEncoder(this.messageConverters))
                .decoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters))))
                .errorDecoder(errorDecoder())
                .options(new Request.Options(
                        gbApiProperties.getConnection().getConnectTimeout(),
                        TimeUnit.SECONDS,
                        gbApiProperties.getConnection().getReadTimeout(),
                        TimeUnit.SECONDS,
                        true
                ))
                .logger(new Slf4jLogger(ManufacturerGateway.class))
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default(
                        gbApiProperties.getConnection().getPeriod(),
                        gbApiProperties.getConnection().getMaxPeriod(),
                        gbApiProperties.getConnection().getMaxAttempts()
                ))
                .contract(new SpringMvcContract())
                .target(ManufacturerGateway.class, gbApiProperties.getEndpoint().getManufacturerUrl());
    }

    private ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            FeignException exception = errorStatus(methodKey, response);
            if (exception.status() == 500 || exception.status() == 503) {
                return new RetryableException(
                        response.status(),
                        exception.getMessage(),
                        response.request().httpMethod(),
                        exception,
                        null,
                        response.request());
            }
            return exception;
        };
    }
}
