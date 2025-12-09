package com.syskan.currencyconverter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.syskan.currencyconverter.dto.ConversionRequest;
import com.syskan.currencyconverter.dto.ConversionResponse;
import com.syskan.currencyconverter.dto.ExchangeRateResponse;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);
        currencyService = new CurrencyService(webClientBuilder);
        
        ReflectionTestUtils.setField(currencyService, "baseUrl", 
            "https://api.exchangerate-api.com/v4/latest");
        ReflectionTestUtils.setField(currencyService, "timeout", 5000);
    }

    @Test
    void testConvertCurrency_Success() {
        // Arrange
        ConversionRequest request = ConversionRequest.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(new BigDecimal("100.00"))
            .build();

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.73"));

        ExchangeRateResponse mockResponse = ExchangeRateResponse.builder()
            .base("USD")
            .date("2024-12-09")
            .rates(rates)
            .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponse.class))
            .thenReturn(Mono.just(mockResponse));

        // Act
        ConversionResponse response = currencyService.convertCurrency(request);

        // Assert
        assertNotNull(response);
        assertEquals("USD", response.getFromCurrency());
        assertEquals("EUR", response.getToCurrency());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals(new BigDecimal("85.00"), response.getConvertedAmount());
        assertEquals(new BigDecimal("0.85"), response.getExchangeRate());
        assertEquals("Conversion successful", response.getMessage());
        assertNotNull(response.getTimestamp());

        verify(webClient, times(1)).get();
    }

    @Test
    void testConvertCurrency_InvalidCurrency() {
        // Arrange
        ConversionRequest request = ConversionRequest.builder()
            .fromCurrency("USD")
            .toCurrency("XYZ")
            .amount(new BigDecimal("100.00"))
            .build();

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));

        ExchangeRateResponse mockResponse = ExchangeRateResponse.builder()
            .base("USD")
            .rates(rates)
            .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponse.class))
            .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> currencyService.convertCurrency(request));
        
        assertTrue(exception.getMessage().contains("Failed to convert currency"));
    }

    @Test
    void testGetAllRates_Success() {
        // Arrange
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.73"));
        rates.put("JPY", new BigDecimal("110.50"));

        ExchangeRateResponse mockResponse = ExchangeRateResponse.builder()
            .base("USD")
            .rates(rates)
            .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponse.class))
            .thenReturn(Mono.just(mockResponse));

        // Act
        Map<String, BigDecimal> result = currencyService.getAllRates("USD");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("0.85"), result.get("EUR"));
        assertEquals(new BigDecimal("0.73"), result.get("GBP"));
        assertEquals(new BigDecimal("110.50"), result.get("JPY"));
    }

    @Test
    void testIsCurrencySupported_True() {
        // Arrange
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));

        ExchangeRateResponse mockResponse = ExchangeRateResponse.builder()
            .base("USD")
            .rates(rates)
            .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponse.class))
            .thenReturn(Mono.just(mockResponse));

        // Act
        boolean supported = currencyService.isCurrencySupported("EUR");

        // Assert
        assertTrue(supported);
    }

    @Test
    void testIsCurrencySupported_False() {
        // Arrange
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));

        ExchangeRateResponse mockResponse = ExchangeRateResponse.builder()
            .base("USD")
            .rates(rates)
            .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateResponse.class))
            .thenReturn(Mono.just(mockResponse));

        // Act
        boolean supported = currencyService.isCurrencySupported("XYZ");

        // Assert
        assertFalse(supported);
    }
}
