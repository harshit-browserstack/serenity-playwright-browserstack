package com.browserstack.cucumber.pages;

import com.microsoft.playwright.Page;

public class LocalPage {

    private static final String URL = "http://bs-local.com:45454/";

    private final Page page;

    public LocalPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate(URL);
    }

    public String getTitle() {
        return page.title();
    }
}
