package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyForGetAllDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValueToRubles(Long value, Long numCode) {
        log.info("CurrencyService method convertValueToRubles executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public Double convertValue(Long value, Long numCode, Long toNumCode) {
        log.info("CurrencyService method convertValue executed");
        Double valueInRubles = convertValueToRubles(value, numCode);
        Currency toCurrency = repository.findByIsoNumCode(toNumCode);
        return Precision.round(valueInRubles / toCurrency.getValue(), 2);
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return  mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public List<CurrencyForGetAllDto> getAll() {
        List<Currency> allCurrencies = repository.findAll();
        return allCurrencies.stream().map(mapper::convertToDtoForGetAll).collect(Collectors.toList());
    }
}
