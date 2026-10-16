# serenity-playwright-browserstack

[Serenity BDD](https://serenity-bdd.info/) with Cucumber on JUnit 6 and Playwright, running on BrowserStack with the BrowserStack Java SDK.

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

## How this sample is set up

| Part | Role |
|---|---|
| JUnit Platform 6 | Starts the run through the `@Suite` classes `SampleTest`, `LocalTest` and `ScreenplayTest` |
| Cucumber 7.34 | Runs the scenarios in `src/test/resources/features` (`@IncludeEngines("cucumber")`) |
| Serenity 5.3 | Cucumber plugin `SerenityReporterParallel` (HTML report), plus Screenplay in `ScreenplayTest` |
| Playwright 1.60 | Drives the browser. `PlaywrightHooks` opens one browser per scenario; in the Screenplay scenario Serenity opens it (`BrowseTheWebWithPlaywright`) |
| BrowserStack SDK | Runs every Playwright `launch()` on BrowserStack, names and marks each session, and sends each scenario to Test Reporting & Analytics |

No JUnit 4 or JUnit Vintage is needed.

## Prerequisites

- Java 17 or newer
- Maven 3.6.3 or newer
- `browserstack-java-sdk` 1.77.0 or newer

## Using Maven

### Setup
* Clone the repo
* Replace `YOUR_USERNAME` and `YOUR_ACCESS_KEY` with your BrowserStack access credentials in `browserstack.yml`, or set the environment variables (see Notes)
* Install dependencies `mvn compile`

### Running your tests
- To run a sample test, run `mvn verify -P sample-test`
- To run local tests, run `mvn verify -P sample-local-test`
- To run the Serenity Screenplay test, run `mvn verify -P sample-screenplay-test`

Each scenario runs once per platform in `browserstack.yml`. On BrowserStack you get:
- **Automate:** one session per scenario, named `<Feature> - <Scenario>` and marked passed or failed with the reason.
- **Test Reporting & Analytics:** one test per scenario, with steps, hooks and tags, linked to its session.

The Serenity report is written to `target/site/serenity/index.html`.

Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

### Integrate your test suite

* Set the framework in `browserstack.yml`:
  ```yaml
  framework: cucumber-junit6
  ```
* Keep `parallelsPerPlatform` in `browserstack.yml` equal to `cucumber.execution.parallel.config.fixed.parallelism` in `src/test/resources/junit-platform.properties`.
* Run Cucumber through a JUnit Platform `@Suite` with the Serenity plugin:
  ```java
  @Suite
  @IncludeEngines("cucumber")
  @SelectClasspathResource("features")
  @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "<your step definitions package>")
  @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel,pretty")
  public class CucumberTestSuite {
  }
  ```
* Add the Maven dependency of `browserstack-java-sdk`:
  ```xml
  <dependency>
      <groupId>com.browserstack</groupId>
      <artifactId>browserstack-java-sdk</artifactId>
      <version>1.77.0</version>
      <scope>compile</scope>
  </dependency>
  ```
* Add `maven-dependency-plugin` (goal `properties`) and the argLine `-javaagent:${com.browserstack:browserstack-java-sdk:jar}` to the plugin that runs your tests:
  ```xml
  <plugin>
      <artifactId>maven-dependency-plugin</artifactId>
      <executions>
          <execution>
              <id>getClasspathFilenames</id>
              <goals>
                  <goal>properties</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
  <plugin>
      <artifactId>maven-failsafe-plugin</artifactId>
      <configuration>
          <includes>
              <include>your_suite_class.java</include>
          </includes>
          <argLine>-javaagent:${com.browserstack:browserstack-java-sdk:jar}</argLine>
      </configuration>
      <executions>
          <execution>
              <goals>
                  <goal>integration-test</goal>
                  <goal>verify</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
  ```
* Keep your Playwright code as it is: `launch()` and `connect()` both run on BrowserStack, including browsers opened by Serenity Screenplay.
* Install dependencies `mvn compile`

## Notes
* `framework: cucumber-junit6` runs only through the BrowserStack CLI, which the SDK downloads at startup. If the CLI can't start, the run stops before any test and prints the reason (for example, a firewall blocking the download).
* For exact session names and statuses, use one browser per scenario. When several scenarios share one browser, each test is still linked to its session, but the session's name and status come from the last scenario that used it.
* With `connect()`, the capabilities come from `browserstack.yml`; a name or build set in your own connect URL is not used.
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* You can export the environment variables for the Username and Access Key of your BrowserStack account

  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
