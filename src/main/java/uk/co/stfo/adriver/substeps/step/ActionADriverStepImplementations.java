package uk.co.stfo.adriver.substeps.step;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.WebdriverSubstepsConfiguration;
import uk.co.stfo.adriver.substeps.runner.DefaultExecutionSetupTearDown;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class ActionADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(ActionADriverStepImplementations.class);

    private final FinderADriverStepImplementations locator;


    public ActionADriverStepImplementations() {
        super();
        this.locator = new FinderADriverStepImplementations(testRunSupplier());
    }


    public ActionADriverStepImplementations(final Supplier<TestRun> testRunSupplier) {
        super(testRunSupplier);
        this.locator = new FinderADriverStepImplementations(testRunSupplier);
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
            webDriver().navigateTo(url);
        } else {
            webDriver().navigateTo(normaliseURL(url));
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
        currentElement().perform().click();
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
        locator.findByLinkText(linkText).perform().click();
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

        locator.findTagElementContainingText("button", buttonText.trim()).perform().click();
    }


    @Step("ClickSubmitButton \"([^\"]*)\"")
    public void clickInput(final String buttonText) {
        logger.debug("About to click submit button with text " + buttonText);

        locator.findByTagAndAttribute("input", "value", buttonText).perform().click();
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
        currentElement().perform().doubleClick();
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
        currentElement().perform().rightClick();
    }
}
