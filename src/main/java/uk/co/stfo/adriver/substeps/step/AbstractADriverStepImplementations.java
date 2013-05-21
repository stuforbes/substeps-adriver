package uk.co.stfo.adriver.substeps.step;

import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.runner.DefaultExecutionSetupTearDown;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;

public abstract class AbstractADriverStepImplementations {

    private final Supplier<TestRun> testRunSupplier;


    public AbstractADriverStepImplementations() {
        this(DefaultExecutionSetupTearDown.currentTestRun());
    }


    public AbstractADriverStepImplementations(final Supplier<TestRun> testRunSupplier) {
        this.testRunSupplier = testRunSupplier;
    }


    protected Supplier<TestRun> testRunSupplier() {
        return testRunSupplier;
    }


    protected Driver webDriver() {
        return testRunSupplier.get().webDriver();
    }


    protected Element currentElement() {
        return testRunSupplier.get().currentElement();
    }


    protected void currentElement(final Element currentElement) {
        testRunSupplier.get().currentElement(currentElement);
    }
}