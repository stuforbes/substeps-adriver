package uk.co.stuforbes.windowlicker.substeps.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.objogate.wl.Prober;
import com.objogate.wl.web.AsyncElementDriver;
import com.objogate.wl.web.AsyncWebDriver;
import com.objogate.wl.web.ElementAction;

public class ActionableAsyncWebDriver extends AsyncWebDriver {

	private WebDriver webDriver;

	public ActionableAsyncWebDriver(Prober prober, WebDriver webDriver) {
		super(prober, webDriver);
		this.webDriver = webDriver;
	}

	public void doubleClick(AsyncElementDriver element){
		element.apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				actions().doubleClick(element).perform();
			}
		});
	}

	public void contextClick(AsyncElementDriver currentElement) {
		currentElement.apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				actions().contextClick(element).perform();
			}
		});		
	}
	
	protected Actions actions() {
		return new Actions(webDriver);
	}
}
