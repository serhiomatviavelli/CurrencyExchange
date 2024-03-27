package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nominal", ignore = true)
    @Mapping(target = "isoNumCode", ignore = true)
    @Mapping(target = "isoLetterCode", ignore = true)
    CurrencyDto convertToDtoForGetAll(Currency currency);

}
