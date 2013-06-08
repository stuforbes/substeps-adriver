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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.common.By2;
import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DriverInitialisation.class)
public class AssertionADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractADriverStepImplementations.class);


    public AssertionADriverStepImplementations() {
        super();
    }


    /**
     * Find an element with the specified id that contains the expected text.
     * 
     * @example AssertValue id an-id text = "Some inner text"
     * @section Assertable
     * 
     * @param id
     *            The id of the container element
     * @param expected
     *            The inner text of the element
     */
    @Step("AssertValue id ([^\"]*) text = \"([^\"]*)\"")
    public void assertElementText(final String id, final String expected) {
        LOG.debug("AssertValue id {} text = \"{}\"", id, expected);

        currentElement(null);

        final Element elem = driver().child(By.id(id));

        elem.assertThat().hasText(containsString(expected));

        currentElement(elem);
    }


    /**
     * Find a child of the current element, by using the xpath, and assert that
     * it contains text
     * 
     * @example AssertChildElementsContainText xpath="//div[@class='main-div']"
     *          text="The main content"
     * @section Assertable
     * 
     * @param xpath
     *            The xpath to locate the child of the current element.
     * @param text
     *            The inner text of the element to be verified
     */
    @Step("AssertChildElementsContainText xpath=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void assertChildElementsContainText(final String xpath, final String text) {
        LOG.debug("AssertChildElementsContainText xpath=\"{}\" text=\"{}\"", xpath, text);

        currentElement().child(By2.combined(By.xpath(xpath), By2.text(text))).assertThat().doesExist();
    }


    /**
     * Ensure the text of the current element is exactly the same as the
     * expected text
     * 
     * @example AssertCurrentElement text="This is the text"
     * @section Assertable
     * 
     * @param expected
     *            The text that the elements inner text must equal
     */
    @Step("AssertCurrentElement text=\"([^\"]*)\"")
    public void assertTextInCurrentElement(final String expected) {
        LOG.debug("AssertCurrentElement text=\"{}\"", expected);

        currentElement().assertThat().hasText(is(expected));
    }


    /**
     * Ensure the text of the current element contains the expected text
     * 
     * @example AssertCurrentElement text contains "This is the text"
     * @section Assertable
     * 
     * @param expected
     *            The text that the elements inner text must contain
     */
    @Step("AssertCurrentElement text contains \"([^\"]*)\"")
    public void assertTextInCurrentElementContains(final String expected) {
        LOG.debug("AssertCurrentElement text contains \"{}\"", expected);
        currentElement().assertThat().hasText(containsString(expected));
    }


    /**
     * Assert that the current element contains the attribute identified by the
     * "attribute" variable, and that it equals the "expected" value
     * 
     * @example AssertCurrentElement attribute="id" value="the-element-id"
     * @section Assertable
     * 
     * @param attribute
     *            The name of the attribute
     * @param expected
     *            The value of the attribute
     */
    @Step("AssertCurrentElement attribute=\"([^\"]*)\" value=\"([^\"]*)\"")
    public void assertAttributeInCurrentElement(final String attribute, final String expected) {
        LOG.debug("AssertCurrentElement attribute=\"{}\" value=\"{}\"", attribute, expected);

        currentElement().assertThat().hasAttribute(attribute, is(expected));
    }


    /**
     * Assert that there is an element on the page that has a tag name
     * identified by "tag", and inner text matching "text"
     * 
     * @example AssertTagElementContainsText tag="p"
     *          text="some paragraph content"
     * @section Assertable
     * 
     * @param tag
     *            The tag name of the element
     * @param text
     *            The inner text of the element
     */
    @Step("AssertTagElementContainsText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void assertTagElementContainsText(final String tag, final String text) {
        LOG.debug("AssertTagElementContainsText tag=\"{}\" text=\"{}\"", tag, text);

        driver().child(By2.combined(By.tagName(tag), By2.textFragment(text))).assertThat().doesExist();
    }


    /**
     * Assert that there is an element on the page that has a tag name
     * identified by "tag", and an attribute with name "attributeName" that has
     * a value "attributeValue"
     * 
     * @example AssertTagElementContainsAttribute tag="img" attributeName="src"
     *          attributeValue="image1.jpg"
     * @section Assertable
     * 
     * @param tag
     *            The tag name of the element
     * @param attributeName
     *            The name of the attribute
     * @param attributeValue
     *            The value of the attribute
     */
    @Step("AssertTagElementContainsAttribute tag=\"([^\"]*)\" attributeName=\"([^\"]*)\" attributeValue=\"([^\"]*)\"")
    public void assertTagElementContainsAttribute(final String tag, final String attributeName,
            final String attributeValue) {
        LOG.debug("AssertTagElementContainsAttribute tag=\"{}\" attributeName=\"{}\" attributeValue=\"{}\"",
                new Object[] { tag, attributeName, attributeValue });

        driver().child(By2.combined(By.tagName(tag), By2.attribute(attributeName, attributeValue))).assertThat()
                .doesExist();
    }


    /**
     * Assert that the page title equals the expected title
     * 
     * @example AssertPageTitle is "About Page"
     * @param expectedTitle
     *            The expected page title
     */
    @Step("AssertPageTitle is \"([^\"]*)\"")
    public void assertPageTitle(final String expectedTitle) {
        LOG.debug("AssertPageTitle is \"{}\"", expectedTitle);
        driver().assertThat().title(is(expectedTitle));
    }


    /**
     * Assert that the raw page source contains the expected text
     * 
     * @example AssertPageSourceContains "some page content"
     * @param expected
     *            The text that is expected to be on the page
     */
    @Step("AssertPageSourceContains \"([^\"]*)\"")
    public void pageSourceContains(final String expected) {
        LOG.debug("AssertPageSourceContains \"{}\"", expected);

        driver().assertThat().pageSource(containsString(expected));
    }


    /**
     * Asserts that the current element is a checkbox, and that it's checked
     * state matches the "checkedString" variable, which will either be "true"
     * or "false"
     * 
     * @example AssertCheckBox checked="true"
     * @section Assertable
     * 
     * @param checkedString
     *            Whether the checkbox should be checked or not. Will either be
     *            "true" or "false"
     */
    @Step("AssertCheckBox checked=\"?([^\"]*)\"?")
    public void assertCheckBoxIsChecked(final String checkedString) {

        LOG.debug("AssertCheckBox checked=\"{}\"", checkedString);

        // check that the current element is not null and is a radio btn
        currentElement().assertThat().matches(new BaseMatcher<WebElement>() {

            @Override
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


            @Override
            public void describeTo(final Description description) {
                description.appendText("A Checkbox element with a checked status of ");
                description.appendText(checkedString);
            }
        });
    }


    /**
     * Asserts that the current element is a radio, and that it's checked state
     * matches the "checkedString" variable, which will either be "true" or
     * "false"
     * 
     * @example AssertRadioButtonIsChecked checked="true"
     * @section Assertable
     * 
     * @param checkedString
     *            Whether the radio should be checked or not. Will either be
     *            "true" or "false"
     */
    @Step("AssertRadioButton checked=\"?([^\"]*)\"?")
    public void assertRadioButtonIsChecked(final String checkedString) {
        LOG.debug("AssertRadioButton checked=\"{}\"", checkedString);

        // check that the current element is not null and is a radio btn
        currentElement().assertThat().matches(new BaseMatcher<WebElement>() {

            @Override
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


            @Override
            public void describeTo(final Description description) {
                description.appendText("A Radio button element with a selected status of ");
                description.appendText(checkedString);
            }
        });
    }


    /**
     * Asserts that the element identified by id is not empty (ie. contains some
     * text)
     * 
     * @example AssertNotEmpty id="email-address"
     * @section Assertable
     * 
     * @param elementId
     *            The id of the html element to be tested
     */
    @Step("AssertNotEmpty id=\"([^\"]*)\"")
    public void assertNotEmpty(final String elementId) {
        LOG.debug("AssertNotEmpty id=\"{}\"", elementId);
        driver().child(By.id(elementId)).assertThat().hasText(is(not("")));
    }


    /**
     * Asserts that the element identified by id contains the specified text
     * 
     * @example AssertContains email-address "email@example.com"
     * @section Assertable
     * 
     * @param elementId
     *            The id of the html element to be tested
     * @param text
     *            The text that the element has to contain
     */
    @Step("AssertContains ([^\"]*) \"([^\"]*)\"")
    public void assertContains(final String elementId, final String text) {
        LOG.debug("AssertContains {} \"{}\"", elementId, text);
        driver().child(By.id(elementId)).assertThat().hasText(is(text));

    }


    /**
     * Asserts that the text is not present in the page source
     * 
     * @example AssertNotPresent text="This should not be in the page source"
     * @section Assertable
     * 
     * @param text
     *            The text we do not want to appear in the page source
     */
    @Step("AssertNotPresent text=\"([^\"]*)\"")
    public void assertNotPresent(final String text) {
        LOG.debug("AssertNotPresent text=\"{}\"", text);
        driver().assertThat().pageSource(not(containsString(text)));
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
                    LOG.debug("Unexpected type attribute. Expected {}, but got {}", type, elem.getAttribute("type"));
                }
            } else {
                LOG.debug("Unexpected tag. Expected {}, but got {}", tag, elem.getTagName());
            }
        } else {
            LOG.debug("Element was null");
        }
        return false;
    }
}
