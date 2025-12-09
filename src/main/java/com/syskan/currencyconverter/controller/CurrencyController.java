package com.syskan.currencyconverter.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syskan.currencyconverter.dto.ConversionRequest;
import com.syskan.currencyconverter.dto.ConversionResponse;
import com.syskan.currencyconverter.service.CurrencyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CurrencyController {

	private final CurrencyService currencyService;

	@PostMapping("/convert")
	public ResponseEntity<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {

		log.info("Received conversion request: {}", request);

		try {
			ConversionResponse response = currencyService.convertCurrency(request);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.error("Invalid request: {}", e.getMessage());
			return ResponseEntity.badRequest()
					.body(ConversionResponse.builder().message("Error: " + e.getMessage()).build());
		} catch (Exception e) {
			log.error("Conversion failed", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ConversionResponse.builder().message("Conversion failed: " + e.getMessage()).build());
		}
	}

	@GetMapping("/rates/{baseCurrency}")
	public ResponseEntity<Map<String, BigDecimal>> getExchangeRates(@PathVariable String baseCurrency) {

		log.info("Fetching rates for: {}", baseCurrency);

		try {
			Map<String, BigDecimal> rates = currencyService.getAllRates(baseCurrency);
			return ResponseEntity.ok(rates);
		} catch (Exception e) {
			log.error("Failed to fetch rates", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/supported/{currencyCode}")
	public ResponseEntity<Map<String, Boolean>> checkCurrencySupport(@PathVariable String currencyCode) {

		boolean supported = currencyService.isCurrencySupported(currencyCode);
		return ResponseEntity.ok(Map.of("supported", supported));
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> health() {
		return ResponseEntity.ok(Map.of("status", "UP", "service", "Currency Converter API"));
	}
}
