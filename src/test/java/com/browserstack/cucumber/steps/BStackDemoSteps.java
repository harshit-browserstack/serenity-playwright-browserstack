package com.browserstack.cucumber.steps;

import com.browserstack.cucumber.hooks.PlaywrightHooks;
import com.browserstack.cucumber.pages.BStackDemoPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BStackDemoSteps {

    private BStackDemoPage demoPage;

    @Given("^I am on the website '(.+)'$")
    public void I_am_on_the_website(String url) {
        demoPage = new BStackDemoPage(PlaywrightHooks.page());
        demoPage.open(url);
    }

    @When("^I select a product and click on 'Add to cart' button")
    public void I_select_a_product_and_add_to_cart() {
        demoPage.selectFirstProductName();
        demoPage.clickAddToCartButton();
    }

    @Then("the product should be added to cart")
    public void product_should_be_added_to_cart() {
        demoPage.waitForCartToOpen();
        assertEquals(demoPage.getSelectedProductName(), demoPage.getProductCartText());
    }
}
