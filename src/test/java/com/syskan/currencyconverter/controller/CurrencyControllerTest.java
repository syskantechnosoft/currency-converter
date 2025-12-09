package com.syskan.currencyconverter.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.syskan.currencyconverter.dto.ConversionRequest;
import com.syskan.currencyconverter.dto.ConversionResponse;
import com.syskan.currencyconverter.service.CurrencyService;

import tools.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    void testConvertCurrency_Success() throws Exception {
        // Arrange
        ConversionRequest request = ConversionRequest.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(new BigDecimal("100.00"))
            .build();

        ConversionResponse response = ConversionResponse.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(new BigDecimal("100.00"))
            .convertedAmount(new BigDecimal("85.00"))
            .exchangeRate(new BigDecimal("0.85"))
            .timestamp(LocalDateTime.now())
            .message("Conversion successful")
            .build();

        when(currencyService.convertCurrency(any(ConversionRequest.class)))
            .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/currency/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fromCurrency").value("USD"))
            .andExpect(jsonPath("$.toCurrency").value("EUR"))
            .andExpect(jsonPath("$.amount").value(100.00))
            .andExpect(jsonPath("$.convertedAmount").value(85.00))
            .andExpect(jsonPath("$.exchangeRate").value(0.85))
            .andExpect(jsonPath("$.message").value("Conversion successful"));
    }

    @Test
    void testConvertCurrency_ValidationError() throws Exception {
        // Arrange - Invalid request with null values
        ConversionRequest request = ConversionRequest.builder().build();

        // Act & Assert
        mockMvc.perform(post("/api/currency/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testConvertCurrency_NegativeAmount() throws Exception {
        // Arrange
        ConversionRequest request = ConversionRequest.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(new BigDecimal("-100.00"))
            .build();

        // Act & Assert
        mockMvc.perform(post("/api/currency/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetExchangeRates_Success() throws Exception {
        // Arrange
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.73"));
        rates.put("JPY", new BigDecimal("110.50"));

        when(currencyService.getAllRates("USD")).thenReturn(rates);

        // Act & Assert
        mockMvc.perform(get("/api/currency/rates/USD"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.EUR").value(0.85))
            .andExpect(jsonPath("$.GBP").value(0.73))
            .andExpect(jsonPath("$.JPY").value(110.50));
    }

    @Test
    void testCheckCurrencySupport_Supported() throws Exception {
        // Arrange
        when(currencyService.isCurrencySupported("EUR")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/currency/supported/EUR"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supported").value(true));
    }

    @Test
    void testCheckCurrencySupport_NotSupported() throws Exception {
        // Arrange
        when(currencyService.isCurrencySupported("XYZ")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/currency/supported/XYZ"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.supported").value(false));
    }

    @Test
    void testHealthCheck() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/currency/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("Currency Converter API"));
    }
}
