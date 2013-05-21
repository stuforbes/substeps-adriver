package uk.co.stfo.adriver.substeps.runner;

import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.element.Element;

public class TestRun {

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


    public void finaliseWebDriver() {
        if (this.driver != null) {
            this.currentElement = null;
            this.driver.quit();
            this.driver = null;
        }
    }
}
