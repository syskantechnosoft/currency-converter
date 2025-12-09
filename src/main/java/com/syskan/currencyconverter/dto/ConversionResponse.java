package com.syskan.currencyconverter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionResponse {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;
    private LocalDateTime timestamp;
    private String message;
}
