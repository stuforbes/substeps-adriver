package uk.co.stuforbes.windowlicker.substeps.step;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stuforbes.windowlicker.substeps.common.WebDriverSubstepsBy;

import com.objogate.wl.web.AsyncElementDriver;
import com.objogate.wl.web.ElementAction;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import com.technophobia.substeps.model.parameter.BooleanConverter;

public class FormWebLickerStepImplementations extends
		AbstractWebLickerStepImplementations {

	private static final Logger logger = LoggerFactory
            .getLogger(FormWebLickerStepImplementations.class);

    private final FinderWebLickerStepImplementations locator;
    //
    private final ActionWebLickerStepImplementations actions;


    public FormWebLickerStepImplementations() {
        super();
        this.locator = new FinderWebLickerStepImplementations(testRunSupplier());
        this.actions = new ActionWebLickerStepImplementations(testRunSupplier());
    }


    /**
     * Submit the form of the current element. NB using click is preferable as
     * javascript may be executed on click, which this method would bypass
     * 
     * @example Submit
     * @section Clicks
     */
    @Step("Submit")
    public void submit() {
        logger.debug("About to submit the form");
        currentElement().submit();
    }


    // TODO quote this, or just send the remainder of the line
    /**
     * Enters text to the current element, without clearing any current content
     * first
     * 
     * @example SendKeys hello
     * @section Forms
     * @param value
     *            the value
     */
    @Step("SendKeys \"([^\"]*)\"")
    public void sendKeys(final String value) {
        logger.debug("About to send keys " + value + " to the current element");
        currentElement().type(value);
    }


    /**
     * Find an element by id, clear any text from the element, and enter text
     * 
     * @example ClearAndSendKeys "fred" to id username
     * @section Forms
     * @param id
     *            the id
     * @param value
     *            the value
     */
    @Step("ClearAndSendKeys \"([^\"]*)\" to id ([^\"]*)")
    public void sendKeysById(final String value, final String id) {
        logger.debug("About to send keys" + value + " to item with id " + id);
        locator.findById(id);
        clearAndSendKeys(value);
    }


    /**
     * Clear any text from the element, and enter text (to the current element)
     * 
     * @example ClearAndSendKeys "hello"
     * @section Forms
     * @param value
     *            the value
     */
    @Step("ClearAndSendKeys \"([^\"]*)\"")
    public void clearAndSendKeys(final String value) {
        logger.debug("About to clear the current element and send the keys "
                + value);
        currentElement().clear();
        currentElement().type(value);
    }


    /**
     * Select a value in the option list that has the id
     * 
     * @example ChooseOption "fred" in id usersList
     * @section Forms
     * @param value
     *            the value
     * @param id
     *            the id
     */
    @Step("ChooseOption \"([^\"]*)\" in id ([^\"]*)")
    public void selectValueInId(final String value, final String id) {
        logger.debug("About to choose option " + value
                + " in select box with id " + id);

        AsyncElementDriver element = locator.findById(id);

        chooseOptionByTextInSelect(value, element);

    }


    /**
     * @param value
     * @param selectElement
     */
    public void chooseOptionByTextInSelect(final String value,
            final AsyncElementDriver element) {
    	element.apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				final Select select = new Select(element);
		        select.selectByVisibleText(value);
		        Assert.assertTrue("expected value is not selected", select
		                .getFirstSelectedOption().isSelected());
		        Assert.assertThat("expected value is not selected", value, is(select
		                .getFirstSelectedOption().getText()));				
			}
		});        
    }


    /**
     * Select a value in the option list in the current element, a Find
     * operation is required immediatebly before
     * 
     * @example ChooseOption "fred" in current element
     * @section Forms
     * @param value
     *            the value
     * @param id
     *            the id
     */
    @Step("ChooseOption \"([^\"]*)\" in current element")
    public void selectValueInCurrentElement(final String value) {

        this.actions.click();
        
        currentElement().apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				boolean found = false;

		        final List<WebElement> options = element
		                .findElements(By.tagName("option"));
		        for (final WebElement e : options) {
		            if (e.getText().equalsIgnoreCase(value)) {
		                e.click();
		                found = true;
		                break;
		            }
		        }

		        if (!found) {
		            throw new IllegalStateException("failed to locate option in select");
		        }				
			}
		});
    }


    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is currently selected")
    public void assertOptionIsSelected(final String id, final String value) {
        logger.debug("Asserting select box with id " + id + " has option "
                + value + " selected");
        locator.findById(id).element(WebDriverSubstepsBy.ByTagAndWithText("option", value, true)).assertIsSelected();
    }


    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is not currently selected")
    public void assertOptionIsNotSelected(final String id, final String value) {
        logger.debug("Asserting select box with id " + id + " has option "
                + value + " not selected");
        locator.findById(id).element(WebDriverSubstepsBy.ByTagAndWithText("option", value, true)).assertIsNotSelected();;
    }


    /**
     * Use: FindRadioButton inside tag="label" with label="<radio_button_text>"
     * + SetRadioButton checked=<true> in preference as this will locate the
     * radio button by visible text rather than the underlying value.
     * 
     * Locates a radio button with a specific value and checks the radio button.
     * 
     * @example SetRadioButton name=opt_in, value=OFF, checked=true
     * @section Forms
     * @param name
     *            the name
     * @param value
     *            the value
     * @param checked
     *            the checked
     * @deprecated use "SetRadioButton checked=.." instead
     */
    @Deprecated
    @Step("SetRadioButton name =([^\"]*), value =([^\"]*), checked =([^\"]*)")
    public void setRadioButtonValue(final String name, final String value,
            final String checked) {

        throw new IllegalStateException(
                "change code to use SetRadioButton checked= true/false instead");
        // logger.debug("Setting radio button with name " + name + " to value "
        // + value + " with checked status " + checked);
        // webDriverContext().setCurrentElement(null);
        // final WebElement elem = this.locator
        // .findElementByPredicate(new RadioButtonPredicate(name.trim(),
        // value.trim()));
        //
        // Assert.assertNotNull("expecting a radio buttin element with value"
        // + value, elem);
        //
        // webDriverContext().setCurrentElement(elem);
        //
        // final boolean val = Boolean.parseBoolean(checked.trim());
        // setCheckboxValue(elem, val);
    }


    /**
     * Sets the value of the current element, assumed to be a radio button to...
     * 
     * @example SetRadioButton checked=true
     * @section Forms
     * @param checked
     *            the checked
     */
    @Step("SetRadioButton checked=([^\"]*)")
    public void setRadioButtonChecked(final String checked) {

        // assumes current element is not null and a radio button
        AsyncElementDriver currentElem = currentElement();

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElem, val);
    }


    /**
     * Sets the value of the current element, assumed to be a checkbox to...
     * 
     * @example SetCheckedBox checked=true
     * @section Forms
     * @param checked
     *            the checked
     */
    @Step("SetCheckedBox checked=([^\"]*)")
    public void setSetCheckedBoxChecked(final String checked) {

        // assumes current element is not null and a radio button

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElement(), val);
    }


    /**
     * Sets the value of a radio button
     * 
     * @example SetRadioButton name="opt_in", text="radio button text"
     * @section Forms
     * @param name
     *            the name
     * @param text
     *            text value
     * @deprecated use SetRadioButton checked=true / false instead with an
     *             apporpriate finder method
     */
    @Deprecated
    @Step("SetRadioButton name=\"([^\"]*)\", text=\"([^\"]*)\"")
    public void setRadioButton(final String name, final String text) {

        throw new IllegalStateException(
                "change code to use SetRadioButton checked=true / false instead");

        // logger.debug("About to set radio button with name " + name
        // + " to text " + text);
        // final WebElement elem = getRadioButtonByNameAndText(name, text);
        //
        // elem.click();
    }


    /**
     * Asserts a value of a radio button
     * 
     * @example AssertRadioButton name="radio_btn_name", text="text",
     *          checked="true"
     * @section Forms
     * @param name
     *            the name
     * @param text
     *            text value
     * @param checked
     *            true or false to indicate wether the checkbox is checked or
     *            not
     * @deprecated use AssertRadioButton checked=true
     */
    @Deprecated
    @Step("AssertRadioButton name=\"([^\"]*)\", text=\"([^\"]*)\", checked=\"([^\"]*)\"")
    public void assertRadioButton(
            final String name,
            final String text,
            @StepParameter(converter = BooleanConverter.class) final Boolean checked) {

        throw new IllegalStateException(
                "change code to use AssertRadioButton checked= true/false instead");
        //
        // logger.debug("Asserting radio button " + name + ", option " + text
        // + " is " + (checked ? "" : "not") + "checked");
        // final WebElement elem = getRadioButtonByNameAndText(name, text);
        //
        // final String checkedAttr = elem.getAttribute("checked");
        //
        // Assert.assertNotNull(checkedAttr);
        //
        // Assert.assertThat(checked, is(Boolean.parseBoolean(checkedAttr)));

    }


    // /**
    // * @param name
    // * @param text
    // * @return
    // */
    // public WebElement getRadioButtonByNameAndText(final String name,
    // final String text) {
    //
    //
    // logger.debug("About to get radio button with name " + name
    // + " and text " + text);
    // webDriverContext().setCurrentElement(null);
    //
    // final RadioButtonPredicate predicate = new RadioButtonPredicate();
    // predicate.setText(text.trim());
    // predicate.setName(name.trim());
    //
    // final WebElement elem = this.locator.findElementByPredicate(predicate);
    //
    // Assert.assertNotNull("expecting a radio buttin element with name:text "
    // + name + ":" + text, elem);
    //
    // webDriverContext().setCurrentElement(elem);
    // return elem;
    // }

    /**
     * Sets a check box value; deprecated use
     * 
     * @example SetCheckBox name="accept", checked=true
     * @section Forms
     * @param name
     *            the name
     * @param checked
     *            the checked
     * @deprecated use SetCheckedBox checked= true/false instead
     */
    @Deprecated
    @Step("SetCheckBox name=\"([^\"]*)\", checked=([^\"]*)")
    public void setSetCheckBox(final String name, final String checked) {
        throw new IllegalStateException(
                "change code to use SetCheckedBox checked= true/false instead");
        // logger.debug("About to set checkbox " + name + "to " + checked);
        // webDriverContext().setCurrentElement(null);
        // final WebElement elem = locator
        // .findElementByPredicate(new CheckBoxPredicate(name.trim()));
        //
        // Assert.assertNotNull("expecting a radio buttin element with value"
        // + name);
        // webDriverContext().setCurrentElement(elem);
        // final boolean val = Boolean.parseBoolean(checked.trim());
        // setCheckboxValue(elem, val);
    }


    /**
     * Sets the checkbox value.
     * 
     * @example
     * @param checkboxField
     *            the checkbox field
     * @param value
     *            the value
     */
    protected void setCheckboxValue(final AsyncElementDriver checkboxField,
            final boolean value) {
        logger.debug("About to set checkbox " + checkboxField + "to "
                + (value ? "checked" : "not checked"));
        
        checkboxField.apply(new ElementAction() {
			
			public void performOn(WebElement element) {
				if (element.isSelected() && !value) {
		            element.click();
		        } else if (!element.isSelected() && value) {
		            element.click();
		        }				
			}
		});
    }
}
