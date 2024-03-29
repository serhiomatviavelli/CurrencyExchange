package ru.skillbox.currency.exchange.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.xml.ValCurs;
import ru.skillbox.currency.exchange.xml.Valute;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CbrClient {

    @Value("${bank.website.path}")
    private String path;
    private final CurrencyRepository repository;

    public CbrClient(CurrencyRepository repository) {
        this.repository = repository;
    }

    public ValCurs getCurrenciesFromCbr() throws JAXBException, MalformedURLException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);
        URL url = new URL(path);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ValCurs) Objects.requireNonNull(unmarshaller).unmarshal(url);
    }

    public void checkCurrency(Valute valute) {
        Currency currencyFromRepo = repository.findByIsoNumCode(valute.getNumCode());
        double currencyValue = Double.parseDouble(valute.getValue().replace(",", "."));
        if (currencyFromRepo != null) {
            currencyFromRepo.setNominal(valute.getNominal());
            currencyFromRepo.setValue(currencyValue);
            currencyFromRepo.setIsoLetterCode(valute.getCharCode());
            repository.save(currencyFromRepo);
            log.info("Updated currency: " + valute.getName() + ". New value = " + valute.getValue());
        } else {
            Currency currency = new Currency();
            currency.setName(valute.getName());
            currency.setNominal(valute.getNominal());
            currency.setValue(currencyValue);
            currency.setIsoNumCode(valute.getNumCode());
            currency.setIsoLetterCode(valute.getCharCode());
            repository.save(currency);
            log.info("Saved new currency: " + valute.getName() + " with value = " + valute.getValue());
        }
    }

    @Scheduled(fixedDelayString = "PT1H")
    public void refreshTables() {
        ValCurs currenciesList = null;
        try {
            currenciesList = getCurrenciesFromCbr();
        } catch (JAXBException e) {
            log.error("Error parsing xml-file: " + e);
        } catch (MalformedURLException e) {
            log.error("Connecting error: " + e);
        }
        List<Valute> currencies = Objects.requireNonNull(currenciesList).getValuteList();
        currencies.forEach(this::checkCurrency);
    }
}
