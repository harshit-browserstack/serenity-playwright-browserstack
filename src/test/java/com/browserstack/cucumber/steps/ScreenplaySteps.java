package com.browserstack.cucumber.steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Serenity Screenplay opens the Playwright browser (BrowseTheWebWithPlaywright, configured in serenity.conf);
 * the BrowserStack SDK runs it on BrowserStack like any other launch().
 */
public class ScreenplaySteps {

    private static final String PRODUCT_TITLE = ".shelf-item__title";

    private Actor actor;

    @Before("@screenplay")
    public void setTheStage() {
        OnStage.setTheStage(Cast.ofStandardActors());
    }

    @Given("^(\\w+) opens the website '(.+)'$")
    public void opensTheWebsite(String name, String url) {
        actor = OnStage.theActorCalled(name).whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());
        actor.attemptsTo(Task.where("{0} opens " + url,
                a -> BrowseTheWebWithPlaywright.as(a).getCurrentPage()
                        .navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED))));
        page().locator(PRODUCT_TITLE).first().waitFor();
    }

    @Then("^(\\w+) should see product \"([^\"]*)\" listed$")
    public void shouldSeeProductListed(String name, String product) {
        int matches = page().locator(PRODUCT_TITLE).filter(new Locator.FilterOptions().setHasText(product)).count();
        assertTrue(matches > 0, "expected product to be listed: " + product);
    }

    @After("@screenplay")
    public void drawTheCurtain() {
        OnStage.drawTheCurtain();
    }

    private Page page() {
        return BrowseTheWebWithPlaywright.as(actor).getCurrentPage();
    }
}
