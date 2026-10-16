package com.browserstack.cucumber.steps;

import com.browserstack.cucumber.hooks.PlaywrightHooks;
import com.browserstack.cucumber.pages.LocalPage;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalCheckSteps {

    @Then("^Title should contain \"([^\"]*)\"$")
    public void matchTitle(String matchTitle) {
        LocalPage localPage = new LocalPage(PlaywrightHooks.page());
        localPage.open();
        String title = localPage.getTitle();
        assertTrue(title.toLowerCase().contains(matchTitle.toLowerCase()),
                "expected title to contain '" + matchTitle + "' but was '" + title + "'");
    }
}
