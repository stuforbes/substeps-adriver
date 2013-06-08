package uk.co.stfo.adriver.substeps.step;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.action.ElementAction;
import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.common.By2;
import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;

import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DriverInitialisation.class)
public class FormADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(FormADriverStepImplementations.class);

    private final FinderADriverStepImplementations locator;


    //

    public FormADriverStepImplementations() {
        this.locator = new FinderADriverStepImplementations();
    }


    /**
     * Submit the form that the current element belongs to
     * 
     * @example Submit
     * @section Form
     */
    @Step("Submit")
    public void submit() {
        LOG.debug("Submit");
        currentElement().perform().submit();
    }


    /**
     * Type the text defined in value into the current element
     * 
     * @example SendKeys "Hello!"
     * @section Form
     * 
     * @param value
     *            The text to be typed
     */
    @Step("SendKeys \"([^\"]*)\"")
    public void sendKeys(final String value) {
        LOG.debug("SendKeys \"{}\"", value);
        currentElement().perform().type(value);
    }


    /**
     * Find the element with the id, and clear the existing text of it, before
     * replacing with the contents of value
     * 
     * @example ClearAndSendKeys "Hello!" to id welcome-field
     * @section Form
     * 
     * @param id
     *            the id of the element to be located
     * @param value
     *            the text to be entered into the element
     */
    @Step("ClearAndSendKeys \"([^\"]*)\" to id ([^\"]*)")
    public void sendKeysById(final String value, final String id) {
        LOG.debug("ClearAndSendKeys \"{}\" to id {}", value, id);
        locator.findById(id);
        clearAndSendKeys(value);
    }


    /**
     * Clear the existing text of the current element, before replacing with the
     * contents of value
     * 
     * @example ClearAndSendKeys "Hello!"
     * @section Form
     * 
     * @param value
     *            the text to be entered into the element
     */
    @Step("ClearAndSendKeys \"([^\"]*)\"")
    public void clearAndSendKeys(final String value) {
        LOG.debug("ClearAndSendKeys \"{}\"", value);
        currentElement().perform().clear();
        currentElement().perform().type(value);
    }


    /**
     * Find the select element with the id specified in the id field, and choose
     * the option with the text defined in value
     * 
     * @example ChooseOption "Option 1"
     * @section Form
     * 
     * @param text
     *            the option text of the option to be selected
     * @param id
     *            the id of the select box
     */
    @Step("ChooseOption \"([^\"]*)\" in id ([^\"]*)")
    public void selectOptionInId(final String text, final String id) {
        LOG.debug("ChooseOption \"{}\" in id {}", text, id);

        final Element element = locator.findById(id);

        element.perform().select(text);

    }


    /**
     * Select an option in the current element (assumed to be a Select box),
     * that has an option with text defined in the text field
     * 
     * @example ChooseOption "Option 3" in current element
     * @section Form
     * 
     * @param text
     *            the text of the option element under the select
     */
    @Step("ChooseOption \"([^\"]*)\" in current element")
    public void selectValueInCurrentElement(final String text) {
        LOG.debug("ChooseOption \"{}\" in current element", text);

        currentElement().perform().select(text);
    }


    /**
     * Assert that the select box with the id specified, has an option with text
     * defined in the value attribute, and that option is currently selected
     * 
     * @example AssertSelect id="select-box" text="Option 4" is currently
     *          selected
     * @section Form
     * 
     * @param id
     *            the id of the select box
     * @param text
     *            the text of the option element
     */
    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is currently selected")
    public void assertOptionIsSelected(final String id, final String text) {
        LOG.debug("AssertSelect id=\"{}\" text=\"{}\" is currently selected", id, text);
        final Element optionElement = locator.findById(id).child(By2.combined(By.tagName("option"), By2.text(text)));

        optionElement.assertThat().matches(new BaseMatcher<WebElement>() {

            @Override
            public boolean matches(final Object item) {
                return ((WebElement) item).isSelected();
            }


            @Override
            public void describeTo(final Description description) {
                description.appendText("is selected");
            }
        });
    }


    /**
     * Assert that the select box with the id specified, has an option with text
     * defined in the value attribute, and that option is currently not selected
     * 
     * @example AssertSelect id="select-box" text="Option 4" is not currently
     *          selected
     * @section Form
     * 
     * @param id
     *            the id of the select box
     * @param value
     *            the text of the option element
     */
    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is not currently selected")
    public void assertOptionIsNotSelected(final String id, final String value) {
        LOG.debug("AssertSelect id=\"{}\" text=\"{}\" is not currently selected", id, value);
        final Element optionElement = locator.findById(id).child(By2.combined(By.tagName("option"), By2.text(value)));

        optionElement.assertThat().matches(new BaseMatcher<WebElement>() {

            @Override
            public boolean matches(final Object item) {
                return !((WebElement) item).isSelected();
            }


            @Override
            public void describeTo(final Description description) {
                description.appendText("is not selected");
            }
        });
    }


    /**
     * Sets the checked state of the current element, which is assumed to be a
     * radio button
     * 
     * @example SetRadioButton checked=true
     * @section Form
     * 
     * @param checked
     *            the checked state of the radio button. Will either be true or
     *            false
     */
    @Step("SetRadioButton checked=([^\"]*)")
    public void setRadioButtonChecked(final String checked) {
        LOG.debug("SetRadioButton checked={}", checked);

        // assumes current element is not null and a radio button
        final Element currentElem = currentElement();

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElem, val);
    }


    /**
     * Sets the checked state of the current element, which is assumed to be a
     * checkbox
     * 
     * @example SetCheckedBox checked=true
     * @section Form
     * 
     * @param checked
     *            the checked state of the checkbox. Will either be true or
     *            false
     */
    @Step("SetCheckedBox checked=([^\"]*)")
    public void setSetCheckedBoxChecked(final String checked) {
        LOG.debug("SetCheckedBox checked={}", checked);

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElement(), val);
    }


    protected void setCheckboxValue(final Element checkboxField, final boolean value) {
        LOG.debug("setCheckboxValue {} to {}", checkboxField, (value ? "checked" : "not checked"));

        checkboxField.perform().perform(new ElementAction() {
            @Override
            public void doActionOn(final WebElement element) {
                if (element.isSelected() && !value) {
                    element.click();
                } else if (!element.isSelected() && value) {
                    element.click();
                }
            }
        });
    }
}
