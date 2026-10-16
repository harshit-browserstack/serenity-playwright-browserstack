package com.browserstack.cucumber.hooks;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * One browser per scenario, on the scenario's own thread. The BrowserStack SDK runs launch() on
 * BrowserStack with the capabilities from browserstack.yml. Screenplay scenarios get their browser
 * from Serenity instead (see ScreenplaySteps).
 */
public class PlaywrightHooks {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    public static Page page() {
        return PAGE.get();
    }

    @Before("not @screenplay")
    public void openBrowser() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();
        PLAYWRIGHT.set(playwright);
        BROWSER.set(browser);
        PAGE.set(browser.newContext().newPage());
    }

    @After("not @screenplay")
    public void closeBrowser() {
        try {
            if (PAGE.get() != null) {
                PAGE.get().context().close();
            }
            if (BROWSER.get() != null) {
                BROWSER.get().close();
            }
        } finally {
            if (PLAYWRIGHT.get() != null) {
                PLAYWRIGHT.get().close();
            }
            PAGE.remove();
            BROWSER.remove();
            PLAYWRIGHT.remove();
        }
    }
}
