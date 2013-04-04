package uk.co.stuforbes.windowlicker.substeps.step;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stuforbes.windowlicker.substeps.common.WebDriverSubstepsBy;
import uk.co.stuforbes.windowlicker.substeps.runner.DefaultExecutionSetupTearDown;

import com.google.common.base.Function;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.objogate.wl.web.AsyncElementDriver;
import com.objogate.wl.web.ElementAction;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.runner.ExecutionContext;

@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class AssertionWebLickerStepImplementations extends AbstractWebLickerStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWebLickerStepImplementations.class);


    public AssertionWebLickerStepImplementations() {
        super();
    }


    /**
     * Check that the element with id has the text ....
     * 
     * @example AssertValue id msg_id text = "Hello World"
     * @section Assertions
     * @param id
     *            the id
     * @param expected
     *            the expected
     */
    @Step("AssertValue id ([^\"]*) text = \"([^\"]*)\"")
    public void assertElementText(final String id, final String expected) {
        logger.debug("Asserting element with id " + id + " has the text " + expected);

            currentElement(null);

            final By byIdAndText = WebDriverSubstepsBy.ByIdAndText(id, expected);

            final AsyncElementDriver elem = webDriver().element(byIdAndText);

            elem.assertExists();
            
            currentElement(elem);
    }


    /**
     * From the current element, apply the xpath and check to see if any of the
     * children have the text ...
     * 
     * @example AssertChildElementsContainText xpath="li//a" text = "Log Out"
     * @section Assertions
     * @param xpath
     *            the xpath
     * @param text
     *            the text
     */
    @Step("AssertChildElementsContainText xpath=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void assertChildElementsContainText(final String xpath, final String text) {
        logger.debug("Asserting chile element with xpath " + xpath + " has the text " + text);
        
        currentElement().element(new ByChained(By.xpath(xpath), WebDriverSubstepsBy.ByText(text))).assertExists();
    }


    /**
     * Check that the current element has the expected text value
     * 
     * @example AssertCurrentElement text="Hello World!"
     * @section Assertions
     * @param expected
     *            the expected text
     */
    @Step("AssertCurrentElement text=\"([^\"]*)\"")
    public void assertTextInCurrentElement(final String expected) {
        logger.debug("Asserting the current element has the text " + expected);
        
        currentElement().assertText(is(expected));
    }


    /**
     * Check that the current element contains the specified text
     * 
     * @example AssertCurrentElement text contains "Hello world"
     * @section Assertions
     * @param expected
     *            the expected text
     */
    @Step("AssertCurrentElement text contains \"([^\"]*)\"")
    public void assertTextInCurrentElementContains(final String expected) {
        logger.debug("Asserting current element contains the text " + expected);
        currentElement().assertText(containsString(expected));
    }


    /**
     * Check that the current element has the specified attribute and value
     * 
     * @example AssertCurrentElement attribute="class" value="icon32x32"
     * @section Assertions
     * 
     * @param attribute
     *            the attribute name
     * @param expected
     *            the expected value of the attribute
     */
    @Step("AssertCurrentElement attribute=\"([^\"]*)\" value=\"([^\"]*)\"")
    public void assertAttributeInCurrentElement(final String attribute, final String expected) {
        logger.debug("Asserting current element has the attribute " + attribute + "with value " + expected);

        currentElement().apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				final String attributeValue = element.getAttribute(attribute);
		        Assert.assertNotNull("Expecting to find attribute " + attribute + " on current element", attributeValue);
		        Assert.assertThat(attributeValue, is(expected));
			}
		});
        
    }


    /**
     * Check that any of the html tags have the specified text
     * 
     * @example AssertTagElementContainsText tag="ul" text="list item itext"
     * @section Assertions
     * @param tag
     *            the tag
     * @param text
     *            the text
     */
    @Step("AssertTagElementContainsText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void assertTagElementContainsText(final String tag, final String text) {
        logger.debug("Asserting tag element " + tag + " has text " + text);
        
        webDriver().element(new ByChained(By.tagName(tag), WebDriverSubstepsBy.ByText(text)));
    }


    /**
     * Check that any of the html tags have the specified attribute name and
     * value
     * 
     * @example AssertTagElementContainsText tag="ul" attributeName="class"
     *          attributeValue="a_list"
     * @section Assertions
     * @param tag
     *            the tag
     * @param attributeName
     *            the attribute name
     * @param attributeValue
     *            the attribute value
     */
    @Step("AssertTagElementContainsAttribute tag=\"([^\"]*)\" attributeName=\"([^\"]*)\" attributeValue=\"([^\"]*)\"")
    public void assertTagElementContainsAttribute(final String tag, final String attributeName,
            final String attributeValue) {
        logger.debug("Asserting tag element " + tag + " has attribute " + attributeName + " with value "
                + attributeValue);
        
        currentElement().element(WebDriverSubstepsBy.ByTagAndAttribute(tag, attributeName, attributeValue));
        webDriver().element(WebDriverSubstepsBy.ByTagAndAttribute(tag, attributeName, attributeValue)).assertExists();
    }


    /**
     * Check that the page title is ....
     * 
     * @example AssertPageTitle is "My Home Page"
     * @section Assertions
     * @param expectedTitle
     *            the expected title
     */
    @Step("AssertPageTitle is \"([^\"]*)\"")
    public void assertPageTitle(final String expectedTitle) {
        logger.debug("Asserting the page title is " + expectedTitle);
        webDriver().assertTitle(is(expectedTitle));
    }


    /**
     * Simple text search on page source
     * 
     * @example AssertPageSourceContains "foobar"
     * 
     * @param expected
     *            Some text you expect to appear on the page
     */
    @Step("AssertPageSourceContains \"([^\"]*)\"")
    public void pageSourceContains(final String expected) {
        logger.debug("Checking page source for expeted content [" + expected + "]");

        webDriver().assertPageSource(containsString(expected));
    }


    /**
     * Check that the current element, a checkbox is checked or not
     * 
     * @example AssertCheckBox checked=true/false
     * @section Assertions
     * @param checkedString
     *            whether the radio button is checked or not
     */
    @Step("AssertCheckBox checked=\"?([^\"]*)\"?")
    public void assertCheckBoxIsChecked(final String checkedString) {

        // check that the current element is not null and is a radio btn
        currentElement().apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				assertElementIs(element, "input", "checkbox");
				
				// check the state
		        final boolean checked = Boolean.parseBoolean(checkedString.trim());
		        if (checked) {
		            Assert.assertTrue("expecting checkbox to be checked", element.isSelected());
		        } else {
		            Assert.assertFalse("expecting checkbox not to be checked", element.isSelected());
		        }
			}
		});
    }


    /**
     * Check that the current element, a radio button, is checked or not
     * 
     * @example AssertRadioButton checked=true/false
     * @section Assertions
     * @param checkedString
     *            whether the radio button is checked or not
     */
    @Step("AssertRadioButton checked=\"?([^\"]*)\"?")
    public void assertRadioButtonIsChecked(final String checkedString) {

        currentElement().apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				assertElementIs(element, "input", "radio");

		        // check the state
		        final boolean checked = Boolean.parseBoolean(checkedString.trim());
		        if (checked) {
		            Assert.assertTrue("expecting radio button to be checked", element.isSelected());
		        } else {
		            Assert.assertFalse("expecting radio button not to be checked", element.isSelected());
		        }				
			}
		});
    }


    /**
     * Check that the current element has the specified attributes
     * 
     * @example AssertCurrentElement has
     *          attributes=[type="submit",value="Search"]
     * @section Assertions
     * @param attributeString
     *            comma separated list of attributes and quoted values
     */

    @Step("AssertCurrentElement has attributes=\\[(.*)\\]")
    public void assertCurrentElementHasAttributes(final String attributeString) {

        currentElement().apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				final Map<String, String> expectedAttributes = WebDriverSubstepsBy.convertToMap(attributeString);

		        Assert.assertTrue("element doesn't have expected attributes: " + attributeString,
		                elementHasExpectedAttributes(element, expectedAttributes));				
			}
		});
    }


    /**
     * Utility method to check that an element is of a particular tag and type
     * 
     * @param elem
     * @param tag
     * @param type
     */
    public static void assertElementIs(final WebElement elem, final String tag, final String type) {

        Assert.assertNotNull("expecting an element", elem);
        Assert.assertTrue("unexpected tag", elem.getTagName() != null
                && elem.getTagName().compareToIgnoreCase(tag) == 0);

        if (type != null) {
            Assert.assertTrue("unexpected type", elem.getAttribute("type") != null
                    && elem.getAttribute("type").compareToIgnoreCase(type) == 0);
        }
    }


    /**
     * Utility method to check that an element is of a particular tag
     * 
     * @param elem
     * @param tag
     * @param type
     */
    public static void assertElementIs(final WebElement elem, final String tag) {
        assertElementIs(elem, tag, null);
    }



    /**
     * Asserts that an element (identified by ID) eventually gets some text
     * inserted into it (by JavaScript, probably)
     * 
     * @example AssertEventuallyNotEmpty mySpan
     * @param elementId
     *            HTML ID of element
     */
    @Step("AssertEventuallyNotEmpty id=\"([^\"]*)\"")
    public void assertEventuallyNotEmpty(final String elementId) {
    	webDriver().element(By.id(elementId)).assertText(is(not("")));
    }


    /**
     * Asserts that an element (identified by ID) eventually gets some specific
     * text inserted into it (by JavaScript, probably)
     * 
     * @example AssertEventuallyContains mySpanId "text I eventually expect"
     * @section Assertions
     * @param elementId
     *            HTML ID of element
     * @param text
     *            the expected text
     */
    @Step("AssertEventuallyContains ([^\"]*) \"([^\"]*)\"")
    public void assertEventuallyContains(final String elementId, final String text) {

        webDriver().element(By.id(elementId)).assertText(is(text));

    }


    /**
     * Assert that the specified text is not found within the page
     * 
     * @example AssertNotPresent text="undesirable text"
     * @section Assertions
     * @param text
     */
    @Step("AssertNotPresent text=\"([^\"]*)\"")
    public void assertNotPresent(final String text) {
        webDriver().assertPageSource(not(containsString(text)));
    }


    
    protected boolean elementHasExpectedAttributes(final WebElement e, final Map<String, String> expectedAttributes) {
        final Map<String, String> actualValues = new HashMap<String, String>();

        for (final String key : expectedAttributes.keySet()) {
            final String elementVal = e.getAttribute(key);

            // if no attribute will this throw an exception or just return
            // null ??
            actualValues.put(key, elementVal);

        }

        final MapDifference<String, String> difference = Maps.difference(expectedAttributes, actualValues);
        return difference.areEqual();
    }
}
