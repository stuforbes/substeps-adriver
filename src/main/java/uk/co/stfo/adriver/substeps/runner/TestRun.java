package uk.co.stfo.adriver.substeps.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;

public class TestRun {

    private static final Logger LOG = LoggerFactory.getLogger(TestRun.class);

    private Driver driver;
    private Element currentElement;
    private boolean success;


    public TestRun(final Driver driver) {
        this.driver = driver;
        this.currentElement = null;
        this.success = true;
    }


    public Driver webDriver() {
        return driver;
    }


    public Element currentElement() {
        return currentElement;
    }


    public void currentElement(final Element currentElement) {
        this.currentElement = currentElement;
    }


    public boolean hasPassed() {
        return success;
    }


    public void failed() {
        this.success = false;
    }


    public void clearSession() {
        if (this.driver != null) {
            LOG.debug("Clearing session data on web driver");
            this.driver.closeSession();
        }
    }


    public void finaliseWebDriver(final ADriverConfiguration configuration) {
        if (this.driver != null && isQuitWebDriver(configuration)) {
            LOG.debug("Closing web driver");
            this.driver.quit();
        }
        this.currentElement = null;
        this.driver = null;
    }


    private boolean isQuitWebDriver(final ADriverConfiguration configuration) {
        if (configuration.getDriverType().isVisual()) {
            return configuration.getCloseWebDriverStrategy().shouldClose(hasPassed());
        }

        LOG.debug("Web driver is not visual, it is safe to quit");
        return true;
    }
}
