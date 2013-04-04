package uk.co.stuforbes.windowlicker.substeps.step;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stuforbes.windowlicker.substeps.configuration.WebdriverSubstepsConfiguration;
import uk.co.stuforbes.windowlicker.substeps.driver.ActionableAsyncWebDriver;
import uk.co.stuforbes.windowlicker.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.SubSteps.Step;

public class ActionWebLickerStepImplementations extends
		AbstractWebLickerStepImplementations {

	private static final Logger logger = LoggerFactory.getLogger(ActionWebLickerStepImplementations.class);

    private final FinderWebLickerStepImplementations locator;


    public ActionWebLickerStepImplementations() {
    	super();
    	this.locator = new FinderWebLickerStepImplementations(testRunSupplier());
    }


    public ActionWebLickerStepImplementations(Supplier<TestRun> testRunSupplier) {
    	super(testRunSupplier);
        this.locator = new FinderWebLickerStepImplementations(testRunSupplier);
	}


	/**
     * Navigate to a url, if the url begins with http or file, the url will be
     * used as is, if a relative url is specified then it will be prepended with
     * the base url property
     * 
     * @example NavigateTo /myApp (will navigate to http://localhost/myApp if
     *          base.url is set to http://localhost)
     * @section Location
     * 
     * @param url
     *            the url
     */
    @Step("NavigateTo ([^\"]*)")
    public void navigateTo(final String url) {
        logger.debug("About to navigate to " + url);

        if (url.startsWith("file") || url.startsWith("http")) {
            webDriver().navigate().to(url);
        } else {
            webDriver().navigate().to(normaliseURL(url));
        }
    }


    /**
     * Find an element by id, then click it.
     * 
     * @example ClickById login
     * @section Clicks
     * 
     * @param id
     *            the id
     */
    @Step("ClickById ([^\"]*)")
    public void clickById(final String id) {
        logger.debug("About to click item with id " + id);
        this.locator.findById(id);
        click();
    }


    /**
     * Click (the current element)
     * 
     * @example Click
     * @section Clicks
     */
    @Step("Click")
    public void click() {
        logger.debug("About to click on current element");
        currentElement().click();
    }


    /**
     * Click the link "(....)" as it appears on the page
     * 
     * @example ClickLink "Contracts"
     * @section Clicks
     * @param linkText
     *            the link text
     */
    @Step("ClickLink \"([^\"]*)\"")
    public void clickLink(final String linkText) {
        logger.debug("About to click link with text " + linkText);
        locator.findByLinkText(linkText).click();
    }


    /**
     * Click a button that has the text...
     * 
     * @example ClickButton submit
     * @section Clicks
     * @param buttonText
     *            the button text
     */
    @Step("ClickButton ([^\"]*)")
    public void clickButton(final String buttonText) {
        logger.debug("About to click button with text " + buttonText);
        
        locator.findTagElementContainingText("button", buttonText.trim()).click();
    }


    @Step("ClickSubmitButton \"([^\"]*)\"")
    public void clickInput(final String buttonText) {
        logger.debug("About to click submit button with text " + buttonText);
        
        locator.findByTagAndAttribute("input", "value", buttonText).click();
    }


    private String normaliseURL(final String relativeURL) {
        return normalise(WebdriverSubstepsConfiguration.baseURL() + relativeURL);
    }


    private String normalise(final String urlToNormalise) {
        try {
            return new URI(urlToNormalise).toString();
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException("The url " + urlToNormalise + " is invalid.", ex);
        }
    }


    /**
     * Performs a double click on the current element (set with a previous Find
     * method).
     * 
     * @example PerformDoubleClick
     * @section Clicks
     * 
     */
    @Step("PerformDoubleClick")
    public void doDoubleClick() {

    	((ActionableAsyncWebDriver)webDriver()).doubleClick(currentElement());
    }


    /**
     * Performs a context click (typically right click, unless this has been
     * changed by the user) on the current element.
     * 
     * @example PerformContextClick
     * @section Clicks
     * 
     */
    @Step("PerformContextClick")
    public void performContextClick() {

        //DM: removed - this this isn't true?! - plus even if it is, doesn't the HtmlUnitWebDriver tell you this?
//        if (webDriverContext().getDriverType() == DriverType.HTMLUNIT) {
//            throw new WebDriverSubstepsException("PerformContextClick not supported in HTMLUnit");
//        }

    	((ActionableAsyncWebDriver)webDriver()).contextClick(currentElement());
    }
}
