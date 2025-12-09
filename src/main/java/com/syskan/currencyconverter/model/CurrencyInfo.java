package com.syskan.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Currency Info
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyInfo {
 private String code;
 private String name;
 private String symbol;
}
