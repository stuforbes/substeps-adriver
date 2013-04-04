package uk.co.stuforbes.windowlicker.substeps.step;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stuforbes.windowlicker.substeps.common.WebDriverSubstepsBy;
import uk.co.stuforbes.windowlicker.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.objogate.wl.web.AsyncElementDriver;
import com.technophobia.substeps.model.SubSteps.Step;

public class FinderWebLickerStepImplementations extends AbstractWebLickerStepImplementations{

	private static final Logger logger = LoggerFactory.getLogger(FinderWebLickerStepImplementations.class);


    public FinderWebLickerStepImplementations() {
        super();
    }


    public FinderWebLickerStepImplementations(Supplier<TestRun> testRunSupplier) {
    	super(testRunSupplier);
	}


	/**
     * Find an element by it's ID
     * 
     * @example FindById username
     * @section Location
     * 
     * @param id
     *            the id
     * @return the web element
     */
    @Step("FindById ([^\"]*)")
    public AsyncElementDriver findById(final String id) {

        logger.debug("Looking for item with id " + id);
        return storeCurrentElementBy(By.id(id));
    }


    /**
     * Find an id by xpath
     * 
     * @example FindByXpath
     * @section Location
     * 
     * @param xpath
     *            the xpath
     * @return 
     */
    @Step("FindByXpath ([^\"]*)")
    public AsyncElementDriver findByXpath(final String xpath) {
        logger.debug("Looking for item with xpath " + xpath);
        
        return storeCurrentElementBy(By.xpath(xpath));
    }


	/**
     * Find an element using the name attribute of the element
     * 
     * @example FindByName "named field"
     * @section Location
     * 
     * @param name
     *            the name
     * @return the web element
     */
    @Step("FindByName \"?([^\"]*)\"?")
    public AsyncElementDriver findByName(final String name) {
        logger.debug("Looking for item with name " + name);
        return storeCurrentElementBy(By.name(name));
    }

    
    public AsyncElementDriver findByLinkText(String linkText){
    	logger.debug("Looking for item with link text " + linkText);
        return storeCurrentElementBy(By.linkText(linkText));
    }
    
    public AsyncElementDriver findByTagAndAttribute(String tag, String attributeName, String attributeValue){
    	logger.debug("Looking for item with tag " + tag + " and attribute "+ attributeName+"="+attributeValue);
    	return storeCurrentElementBy(WebDriverSubstepsBy.ByTagAndAttribute(tag, attributeName, attributeValue));
    }

    /**
     * Finds an element on the page with the specified tag and text
     * 
     * @example FindTagElementContainingText tag="ul" text="list item itext"
     * @section Location
     * @param tag
     *            the tag
     * @param text
     *            the text
     * @return 
     */
    @Step("FindTagElementContainingText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public AsyncElementDriver findTagElementContainingText(final String tag, final String text) {
        logger.debug("Finding tag element " + tag + "and asserting has text " + text);
        
        return storeCurrentElementBy(WebDriverSubstepsBy.ByTagAndWithText(tag, text, true));
    }


    /**
     * Finds an element that is a child of the current element using the name
     * attribute, another Find method should be used first
     * 
     * @example FindChild ByName name="child name"
     * @section Location
     * 
     * @param name
     *            the name
     * @return the web element
     */
    @Step("FindChild ByName name=\"?([^\"]*)\"?")
    public AsyncElementDriver findChildByName(final String name) {
        logger.debug("Looking for child with name " + name);
        
        AsyncElementDriver child = currentElement().element(By.name(name));
		currentElement(child);
		
		return child;
    }


    /**
     * Finds an element that is a child of the current element using the tag
     * name and specified attributes, another Find method should be used first
     * 
     * @example FindChild ByTagAndAttributes tag="input"
     *          attributes=[type="submit",value="Search"]
     * @section Location
     * 
     * @param tag
     *            the tag
     * @param attributeString
     *            the attribute string
     * @return the web element
     */
    @Step("FindChild ByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public AsyncElementDriver findChildByTagAndAttributes(final String tag, final String attributeString) {
        logger.debug("Looking for child with tag " + tag + " and attributes " + attributeString);
        
        AsyncElementDriver child = currentElement().element(WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString));
        
        currentElement(child);


        return child;
    }


    /**
     * Finds a checkbox that is a child of the specified tag, that contains the
     * specified text; eg.
     * 
     * <pre>
     * <label>
     *  <input type="checkbox" name="checkbox_name" value="yeah"/>a checkbox <span>label</span>
     * </label>
     * </pre>
     * 
     * @example FindCheckbox inside tag="label" with label="a checkbox label>"
     * 
     * @section Location
     * 
     * @param tag
     *            the tag
     * @param label
     *            the checkbox label
     * @return the web element
     */
    @Step("FindCheckbox inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public AsyncElementDriver findCheckBox(final String tag, final String label) {

    	return findInputInsideTag(tag, label, "checkbox");

    }

    // todo variant that also has attributes for the tag

    /**
     * Finds a radiobutton that is a child of the specified tag, that contains
     * the specified text; eg.
     * 
     * <pre>
     * <label>
     *  <input type="radio" name="radio_name" value="yeah"/>a radio <span>label</span>
     * </label>
     * </pre>
     * 
     * @example FindRadioButton inside tag="label" with label="a radio label>"
     * 
     * @section Location
     * 
     * @param tag
     *            the tag
     * @param label
     *            the radio button label
     * @return the web element
     */
    @Step("FindRadioButton inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public AsyncElementDriver findRadioButton(final String tag, final String label) {

        return findInputInsideTag(tag, label, "radio");
    }



    /**
     * Find an element by tag name and a set of attributes and corresponding
     * values
     * 
     * @param tag
     *            the tag
     * @param attributeString
     *            the attribute string
     * @return the web element
     * @example FindByTagAndAttributes tag="input"
     *          attributes=[type="submit",value="Search"]
     * @section Location
     */
    @Step("FindByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public AsyncElementDriver findByTagAndAttributes(final String tag, final String attributeString) {
        logger.debug("Looking for item with tag " + tag + " and attributes " + attributeString);
        
        AsyncElementDriver element = webDriver().element(WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString));

        currentElement(element);

        return element;
    }

    
    private AsyncElementDriver storeCurrentElementBy(By by) {
    	AsyncElementDriver element = webDriver().element(by);
        currentElement(element);

        return element;
	}
    
    private AsyncElementDriver findInputInsideTag(final String tag,
			final String label, final String inputType) {
		AsyncElementDriver element = webDriver().element(new ByChained(By.tagName(tag), WebDriverSubstepsBy.ByText(label))).element(WebDriverSubstepsBy.ByTagAndAttribute("input", "type", inputType));
    	
    	currentElement(element);

        return element;
	}
}
