package uk.co.stuforbes.windowlicker.substeps.step;

import uk.co.stuforbes.windowlicker.substeps.runner.DefaultExecutionSetupTearDown;
import uk.co.stuforbes.windowlicker.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.objogate.wl.web.AsyncElementDriver;
import com.objogate.wl.web.AsyncWebDriver;

public abstract class AbstractWebLickerStepImplementations {

	private Supplier<TestRun> testRunSupplier;
	
	public AbstractWebLickerStepImplementations(){
		this(DefaultExecutionSetupTearDown.currentTestRun());
	}
	
	public AbstractWebLickerStepImplementations(Supplier<TestRun> testRunSupplier) {
		this.testRunSupplier = testRunSupplier;
	}
	
	protected Supplier<TestRun> testRunSupplier(){
		return testRunSupplier;
	}

	protected AsyncWebDriver webDriver(){
		return testRunSupplier.get().webDriver();
	}
	
	protected AsyncElementDriver currentElement(){
		return testRunSupplier.get().currentElement();
	}
	
	protected void currentElement(AsyncElementDriver currentElement){
		testRunSupplier.get().currentElement(currentElement);
	}
}