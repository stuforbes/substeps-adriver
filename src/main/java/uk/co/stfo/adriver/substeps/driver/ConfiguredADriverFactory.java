package uk.co.stfo.adriver.substeps.driver;

import org.openqa.selenium.WebDriver;

import uk.co.stfo.adriver.driver.AsyncDriver;
import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.driver.webdriver.WebDriverFactories;

public class ConfiguredADriverFactory implements ADriverFactory {

    private final ADriverConfiguration configuration;


    public ConfiguredADriverFactory(final ADriverConfiguration configuration) {
        this.configuration = configuration;
    }


    public Driver createDriver() {
        final WebDriver webDriver = WebDriverFactories.createFor(configuration.getDriverType(), configuration);

        return AsyncDriver
                .createAsynDriver(webDriver, configuration.getPollTimeout(), configuration.getPollFrequency());
    }

}
