package com.syskan.currencyconverter.e2e;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyConverterE2ETest {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    private String baseUrl;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContextAndPage() {
        baseUrl = "http://localhost:" + port;
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void testPageLoads() {
        page.navigate(baseUrl);
        assertThat(page).hasTitle("Currency Converter");
        assertThat(page.locator("h1")).containsText("Currency Converter");
    }

    @Test
    void testFormElementsPresent() {
        page.navigate(baseUrl);

        // Check all form elements are present
        assertThat(page.locator("[data-testid='amount-input']")).isVisible();
        assertThat(page.locator("[data-testid='from-currency']")).isVisible();
        assertThat(page.locator("[data-testid='to-currency']")).isVisible();
        assertThat(page.locator("[data-testid='convert-button']")).isVisible();
    }

    @Test
    void testCurrencyConversion() {
        page.navigate(baseUrl);

        // Fill in the form
        page.locator("[data-testid='amount-input']").fill("100");
        page.locator("[data-testid='from-currency']").selectOption("USD");
        page.locator("[data-testid='to-currency']").selectOption("EUR");

        // Submit the form
        page.locator("[data-testid='convert-button']").click();

        // Wait for result and verify
        page.waitForSelector("[data-testid='result']", 
            new Page.WaitForSelectorOptions().setTimeout(10000));
        
        assertThat(page.locator("[data-testid='result']")).isVisible();
        assertThat(page.locator("[data-testid='converted-amount']")).isVisible();
    }

    @Test
    void testValidationErrors() {
        page.navigate(baseUrl);

        // Submit form without filling fields
        page.locator("[data-testid='convert-button']").click();

        // Page should still be on the same page (validation errors shown)
        assertThat(page).hasURL(baseUrl + "/convert");
    }

    @Test
    void testMultipleConversions() {
        page.navigate(baseUrl);

        // First conversion
        page.locator("[data-testid='amount-input']").fill("50");
        page.locator("[data-testid='from-currency']").selectOption("USD");
        page.locator("[data-testid='to-currency']").selectOption("GBP");
        page.locator("[data-testid='convert-button']").click();
        page.waitForSelector("[data-testid='result']");

        // Second conversion
        page.locator("[data-testid='amount-input']").fill("200");
        page.locator("[data-testid='from-currency']").selectOption("EUR");
        page.locator("[data-testid='to-currency']").selectOption("JPY");
        page.locator("[data-testid='convert-button']").click();
        
        page.waitForTimeout(2000); // Wait for result update
        assertThat(page.locator("[data-testid='result']")).isVisible();
    }

    @Test
    void testUIResponsiveness() {
        page.setViewportSize(375, 667); // iPhone size
        page.navigate(baseUrl);

        assertThat(page.locator("h1")).isVisible();
        assertThat(page.locator("[data-testid='amount-input']")).isVisible();
        assertThat(page.locator("[data-testid='convert-button']")).isVisible();
    }

    @Test
    void testFormFieldInteractions() {
        page.navigate(baseUrl);

        Locator amountInput = page.locator("[data-testid='amount-input']");
        Locator fromCurrency = page.locator("[data-testid='from-currency']");

        // Test amount input
        amountInput.fill("123.45");
        assertThat(amountInput).hasValue("123.45");

        // Test currency selection
        fromCurrency.selectOption("USD");
        assertThat(fromCurrency).hasValue("USD");
    }
}
