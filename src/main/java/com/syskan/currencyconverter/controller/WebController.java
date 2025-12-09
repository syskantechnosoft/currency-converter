package com.syskan.currencyconverter.controller;


import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.syskan.currencyconverter.dto.ConversionRequest;
import com.syskan.currencyconverter.dto.ConversionResponse;
import com.syskan.currencyconverter.service.CurrencyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final CurrencyService currencyService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("conversionRequest", new ConversionRequest());
        addCurrencies(model);
        return "index";
    }

    @PostMapping("/convert")
    public String convert(
            @Valid @ModelAttribute("conversionRequest") ConversionRequest request,
            BindingResult result,
            Model model) {

        addCurrencies(model);

        if (result.hasErrors()) {
            return "index";
        }

        try {
            ConversionResponse response = currencyService.convertCurrency(request);
            model.addAttribute("response", response);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", "Conversion failed: " + e.getMessage());
        }

        return "index";
    }

    private void addCurrencies(Model model) {
        // Popular currencies
        Map<String, String> currencies = Map.of(
            "USD", "US Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "INR", "Indian Rupee",
            "AUD", "Australian Dollar",
            "CAD", "Canadian Dollar",
            "CHF", "Swiss Franc"
        );
        model.addAttribute("currencies", currencies);
    }
}
