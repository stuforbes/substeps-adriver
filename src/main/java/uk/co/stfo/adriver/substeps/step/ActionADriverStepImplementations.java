package uk.co.stfo.adriver.substeps.step;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DriverInitialisation.class)
public class ActionADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(ActionADriverStepImplementations.class);

    private final FinderADriverStepImplementations locator;


    public ActionADriverStepImplementations() {
        this.locator = new FinderADriverStepImplementations();
    }


    public ActionADriverStepImplementations(final Supplier<TestRun> testRunSupplier,
            final Supplier<ADriverConfiguration> configurationSupplier) {
        super(testRunSupplier, configurationSupplier);
        this.locator = new FinderADriverStepImplementations(testRunSupplier, configurationSupplier);
    }


    /**
     * Navigates to the specified url. If the url is absolute (is prefixed with
     * file or http), the browser is redirected to that url. If not, the
     * configured base url (see base.url in default.properties) is prefixed.
     * 
     * @example NavigateTo /login
     * @section Navigation
     * 
     * @param url
     *            The url to navigate to
     */
    @Step("NavigateTo ([^\"]*)")
    public void navigateTo(final String url) {
        LOG.debug("NavigateTo {}", url);

        if (url.startsWith("file") || url.startsWith("http")) {
            driver().navigateTo(url);
        } else {
            driver().navigateTo(normaliseURL(url));
        }
    }


    /**
     * Clicks on an Html element with the specified id attribute
     * 
     * @example ClickById my-link
     * @section Clickable
     * 
     * @param id
     *            The id of the element to be clicked
     */
    @Step("ClickById ([^\"]*)")
    public void clickById(final String id) {
        LOG.debug("clickById {}", id);
        this.locator.findById(id);
        click();
    }


    /**
     * Clicks on the currently selected element (Assumes that an element has
     * been located and saved in the {@link ExecutionContext}
     * 
     * @example Click
     * @section Clickable
     * 
     */
    @Step("Click")
    public void click() {
        LOG.debug("Click");
        currentElement().perform().click();
    }


    /**
     * Clicks on a link that has the specified linkText
     * 
     * @example ClickLink "Logout"
     * @section Clickable
     * 
     * @param linkText
     *            The text contained in the link
     */
    @Step("ClickLink \"([^\"]*)\"")
    public void clickLink(final String linkText) {
        LOG.debug("ClickLink \"{}\"", linkText);
        locator.findByLinkText(linkText).perform().click();
    }


    /**
     * Click on a button containing the text buttonText
     * 
     * @example ClickButton Register
     * @section Clickable
     * 
     * @param buttonText
     *            The text on the button to be clicked
     */
    @Step("ClickButton ([^\"]*)")
    public void clickButton(final String buttonText) {
        LOG.debug("ClickButton {}", buttonText);

        locator.findTagElementContainingText("button", buttonText.trim()).perform().click();
    }


    /**
     * Click on the submit button containing the text buttonText
     * 
     * @example ClickSubmitButton Submit
     * @section Clickable
     * 
     * @param buttonText
     *            The text on the submit button to be clicked
     */
    @Step("ClickSubmitButton \"([^\"]*)\"")
    public void clickInput(final String buttonText) {
        LOG.debug("ClickSubmitButton \"{}\"", buttonText);

        locator.findByTagAndAttribute("input", "value", buttonText).perform().click();
    }


    /**
     * Performs a double click on the current element (Assumes that an element
     * has been located and saved in the {@link ExecutionContext}.
     * 
     * @example PerformDoubleClick
     * @section Clickable
     * 
     */
    @Step("PerformDoubleClick")
    public void doDoubleClick() {
        LOG.debug("PerformDoubleClick");
        currentElement().perform().doubleClick();
    }


    /**
     * Performs a context click on the current element (Assumes that an element
     * has been located and saved in the {@link ExecutionContext}.
     * 
     * @example PerformContextClick
     * @section Clickable
     * 
     */
    @Step("PerformContextClick")
    public void performContextClick() {
        LOG.debug("PerformContextClick");
        currentElement().perform().rightClick();
    }


    private String normaliseURL(final String relativeURL) {
        return normalise(configuration().getBaseUrl() + relativeURL);
    }


    private String normalise(final String urlToNormalise) {
        try {
            return new URI(urlToNormalise).toString();
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException("The url " + urlToNormalise + " is invalid.", ex);
        }
    }
}
