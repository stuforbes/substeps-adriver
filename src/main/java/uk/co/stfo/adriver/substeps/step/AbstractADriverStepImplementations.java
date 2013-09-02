package uk.co.stfo.adriver.substeps.step;

import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;
import uk.co.stfo.adriver.substeps.runner.ExecutionState;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;

public abstract class AbstractADriverStepImplementations {

    private final Supplier<TestRun> testRunSupplier;
    private final Supplier<ADriverConfiguration> configurationSupplier;


    public AbstractADriverStepImplementations() {
        this(ExecutionState.currentTestRun(), ExecutionState.configuration());
    }


    public AbstractADriverStepImplementations(final Supplier<TestRun> testRunSupplier,
            final Supplier<ADriverConfiguration> configurationSupplier) {
        this.testRunSupplier = testRunSupplier;
        this.configurationSupplier = configurationSupplier;
    }


    protected Supplier<TestRun> testRunSupplier() {
        return testRunSupplier;
    }


    protected Driver driver() {
        return testRunSupplier.get().webDriver();
    }


    protected Element currentElement() {
        return testRunSupplier.get().currentElement();
    }


    protected void currentElement(final Element currentElement) {
        testRunSupplier.get().currentElement(currentElement);
    }


    protected ADriverConfiguration configuration() {
        return configurationSupplier.get();
    }
}