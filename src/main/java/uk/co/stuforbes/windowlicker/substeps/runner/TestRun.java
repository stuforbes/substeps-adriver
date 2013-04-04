package uk.co.stuforbes.windowlicker.substeps.runner;

import com.objogate.wl.web.AsyncElementDriver;
import com.objogate.wl.web.AsyncWebDriver;

public class TestRun {

	private AsyncWebDriver webDriver;
	private AsyncElementDriver currentElement;
	private boolean success;
	
	public TestRun(AsyncWebDriver webDriver){
		this.webDriver = webDriver;
		this.currentElement = null;
		this.success = true;
	}
	
	public AsyncWebDriver webDriver(){
		return webDriver;
	}
	
	public AsyncElementDriver currentElement() {
		return currentElement;
	}
	
	public void currentElement(AsyncElementDriver currentElement) {
		this.currentElement = currentElement;
	}
	
	public boolean hasPassed(){
		return success;
	}
	
	public void failed(){
		this.success = false;
	}
	
	public void finaliseWebDriver(){
		if (this.webDriver != null) {
			this.currentElement = null;
            this.webDriver.quit();
            this.webDriver = null;
        }
	}
}
