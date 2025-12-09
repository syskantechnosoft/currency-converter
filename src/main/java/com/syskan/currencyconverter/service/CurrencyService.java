package com.syskan.currencyconverter.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.syskan.currencyconverter.dto.ConversionRequest;
import com.syskan.currencyconverter.dto.ConversionResponse;
import com.syskan.currencyconverter.dto.ExchangeRateResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrencyService {

    private final WebClient webClient;
    
    @Value("${currency.api.base-url}")
    private String baseUrl;
    
    @Value("${currency.api.timeout}")
    private int timeout;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        log.info("Converting {} {} to {}", 
                request.getAmount(), request.getFromCurrency(), request.getToCurrency());

        try {
            ExchangeRateResponse rateResponse = fetchExchangeRates(request.getFromCurrency());
            
            BigDecimal rate = rateResponse.getRates().get(request.getToCurrency());
            if (rate == null) {
                throw new IllegalArgumentException(
                    "Exchange rate not found for currency: " + request.getToCurrency());
            }

            BigDecimal convertedAmount = request.getAmount()
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);

            log.info("Conversion successful: {} {} = {} {}", 
                    request.getAmount(), request.getFromCurrency(),
                    convertedAmount, request.getToCurrency());

            return ConversionResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .exchangeRate(rate)
                .timestamp(LocalDateTime.now())
                .message("Conversion successful")
                .build();

        } catch (Exception e) {
            log.error("Error converting currency", e);
            throw new RuntimeException("Failed to convert currency: " + e.getMessage(), e);
        }
    }

    private ExchangeRateResponse fetchExchangeRates(String baseCurrency) {
        String url = baseUrl + "/" + baseCurrency;
        
        log.debug("Fetching exchange rates from: {}", url);
        
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(ExchangeRateResponse.class)
            .timeout(Duration.ofMillis(timeout))
            .block();
    }

    public Map<String, BigDecimal> getAllRates(String baseCurrency) {
        ExchangeRateResponse response = fetchExchangeRates(baseCurrency);
        return response.getRates();
    }

    public boolean isCurrencySupported(String currencyCode) {
        try {
            Map<String, BigDecimal> rates = getAllRates("USD");
            return rates.containsKey(currencyCode) || currencyCode.equals("USD");
        } catch (Exception e) {
            log.error("Error checking currency support", e);
            return false;
        }
    }
}
