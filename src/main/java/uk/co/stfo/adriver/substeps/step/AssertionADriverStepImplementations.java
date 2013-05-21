package uk.co.stfo.adriver.substeps.step;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.common.WebDriverSubstepsBy;
import uk.co.stfo.adriver.substeps.runner.DefaultExecutionSetupTearDown;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class AssertionADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(AbstractADriverStepImplementations.class);


    public AssertionADriverStepImplementations() {
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

        final Element elem = webDriver().child(byIdAndText);

        elem.assertThat().doesExist();

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

        currentElement().child(new ByChained(By.xpath(xpath), WebDriverSubstepsBy.ByText(text))).assertThat()
                .doesExist();
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

        currentElement().assertThat().hasText(is(expected));
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
        currentElement().assertThat().hasText(containsString(expected));
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

        currentElement().assertThat().hasAttribute(attribute, is(expected));
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

        webDriver().child(new ByChained(By.tagName(tag), WebDriverSubstepsBy.ByText(text))).assertThat().doesExist();
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

        webDriver().child(WebDriverSubstepsBy.ByTagAndAttribute(tag, attributeName, attributeValue)).assertThat()
                .doesExist();
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
        webDriver().assertThat().title(is(expectedTitle));
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

        webDriver().assertThat().pageSource(containsString(expected));
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
        currentElement().assertThat().matches(new BaseMatcher<WebElement>() {

            public boolean matches(final Object obj) {
                final WebElement element = (WebElement) obj;

                if (checkElementIs(element, "input", "checkbox")) {
                    // check the state
                    final boolean checked = Boolean.parseBoolean(checkedString.trim());
                    final boolean result = checked == element.isSelected();

                    return result;
                }
                return false;
            }


            public void describeTo(final Description description) {
                description.appendText("A Checkbox element with a checked status of ");
                description.appendText(checkedString);
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

        // check that the current element is not null and is a radio btn
        currentElement().assertThat().matches(new BaseMatcher<WebElement>() {

            public boolean matches(final Object obj) {
                final WebElement element = (WebElement) obj;

                if (checkElementIs(element, "input", "radio")) {
                    // check the state
                    final boolean checked = Boolean.parseBoolean(checkedString.trim());
                    final boolean result = checked == element.isSelected();

                    return result;
                }
                return false;
            }


            public void describeTo(final Description description) {
                description.appendText("A Radio button element with a selected status of ");
                description.appendText(checkedString);
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

        currentElement().assertThat().matches(new BaseMatcher<WebElement>() {

            public boolean matches(final Object obj) {
                final WebElement element = (WebElement) obj;
                final Map<String, String> expectedAttributes = WebDriverSubstepsBy.convertToMap(attributeString);

                return elementHasExpectedAttributes(element, expectedAttributes);
            }


            public void describeTo(final Description description) {
                description.appendText("Element has attributes ");
                description.appendText(attributeString);
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
    protected boolean checkElementIs(final WebElement elem, final String tag, final String type) {

        if (elem != null) {
            if (elem.getTagName() != null && elem.getTagName().compareToIgnoreCase(tag) == 0) {

                if (elem.getAttribute("type") != null && elem.getAttribute("type").compareToIgnoreCase(type) == 0) {
                    return true;
                } else {
                    logger.debug("Unexpected type attribute. Expected {}, but got {}", type, elem.getAttribute("type"));
                }
            } else {
                logger.debug("Unexpected tag. Expected {}, but got {}", tag, elem.getTagName());
            }
        } else {
            logger.debug("Element was null");
        }
        return false;
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
        webDriver().child(By.id(elementId)).assertThat().hasText(is(not("")));
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

        webDriver().child(By.id(elementId)).assertThat().hasText(is(text));

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
        webDriver().assertThat().pageSource(not(containsString(text)));
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
