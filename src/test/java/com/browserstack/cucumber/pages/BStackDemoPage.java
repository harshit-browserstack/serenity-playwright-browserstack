package com.browserstack.cucumber.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

public class BStackDemoPage {

    private final Page page;
    private String selectedProductName = "";

    public BStackDemoPage(Page page) {
        this.page = page;
    }

    public void open(String url) {
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        firstProductName().waitFor();
    }

    public void selectFirstProductName() {
        selectedProductName = firstProductName().textContent();
    }

    public void clickAddToCartButton() {
        page.locator("xpath=//*[@id=\"1\"]/div[4]").click();
    }

    public void waitForCartToOpen() {
        page.locator(".float-cart__content").waitFor();
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public String getProductCartText() {
        return page.locator("xpath=//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]").textContent();
    }

    private Locator firstProductName() {
        return page.locator("xpath=//*[@id=\"1\"]/p");
    }
}
