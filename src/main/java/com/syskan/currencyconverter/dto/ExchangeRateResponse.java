package com.syskan.currencyconverter.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateResponse {
	private String base;
	private String date;
	private Map<String, BigDecimal> rates;
}
