@screenplay
Feature: BrowserStack Demo with Serenity Screenplay

  Scenario: BStack Screenplay Test - Product is listed
    Given Shopper opens the website 'https://www.bstackdemo.com'
    Then Shopper should see product "iPhone 12" listed
