package uk.co.stfo.adriver.substeps.step;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.element.Element;
import uk.co.stfo.adriver.substeps.common.By2;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DriverInitialisation.class)
public class FinderADriverStepImplementations extends AbstractADriverStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(FinderADriverStepImplementations.class);


    public FinderADriverStepImplementations() {
        super();
    }


    public FinderADriverStepImplementations(final Supplier<TestRun> testRunSupplier,
            final Supplier<ADriverConfiguration> configurationSupplier) {
        super(testRunSupplier, configurationSupplier);
    }


    /**
     * Find an html element by its id attribute, and store as the current
     * element
     * 
     * @example FindById login-button
     * @section Locatable
     * 
     * @param id
     *            The id of the html element
     * @return The {@link Element} placeholder with this id
     */
    @Step("FindById ([^\"]*)")
    public Element findById(final String id) {
        LOG.debug("FindById {}", id);
        return storeCurrentElementBy(By.id(id));
    }


    /**
     * Find an html element by applying the xpath, and store as the current
     * element
     * 
     * @example FindByXpath //div[@class='panel']
     * @section Locatable
     * 
     * @param xpath
     *            The xpath to locate the html element
     * @return The {@link Element} placeholder with this xpath
     */
    @Step("FindByXpath ([^\"]*)")
    public Element findByXpath(final String xpath) {
        LOG.debug("FindByXpath {}", xpath);

        return storeCurrentElementBy(By.xpath(xpath));
    }


    /**
     * Find an html element by its name attribute, and store as the current
     * element
     * 
     * @example FindByName surname-field
     * @section Locatable
     * 
     * @param name
     *            The name of the html element
     * @return The {@link Element} placeholder with this name
     */
    @Step("FindByName \"?([^\"]*)\"?")
    public Element findByName(final String name) {
        LOG.debug("FindByName {}", name);
        return storeCurrentElementBy(By.name(name));
    }


    /**
     * Find an html element by its link text attribute, and store as the current
     * element
     * 
     * @example FindByLinkText logout
     * @section Locatable
     * 
     * @param linkText
     *            The link text of the html element
     * @return The {@link Element} placeholder with this link text
     */
    @Step("FindByLinkText ([^\"]*)")
    public Element findByLinkText(final String linkText) {
        LOG.debug("FindByLinkText {}", linkText);
        return storeCurrentElementBy(By.linkText(linkText));
    }


    /**
     * Find an html element by its tag name, and a specified attribute name and
     * value
     * 
     * @example FindByTagAndAttribute tag=input attributeName=type
     *          attributeValue=text
     * @section Locatable
     * 
     * @param tag
     *            The tag name of the html element
     * @param attributeName
     *            The name of the attribute
     * @param attributeValue
     *            The value of the attribute
     * @return The {@link Element} placeholder with this tag, and attribute
     *         name/value
     */
    @Step("FindByTagAndAttribute tag=([^\"]*) attributeName=([^\"]*) attributeValue=([^\"]*)")
    public Element findByTagAndAttribute(final String tag, final String attributeName, final String attributeValue) {
        LOG.debug("FindByTagAndAttribute tag={} attributeName={} attributeValue={}", new Object[] { tag, attributeName,
                attributeValue });
        return storeCurrentElementBy(By2.combined(By.tagName(tag), By2.attribute(attributeName, attributeValue)));
    }


    /**
     * Find an html element by its tag name, and containing the text
     * 
     * @example FindTagElementContainingText tag="p" text="Some paragraph text"
     * @section Locatable
     * 
     * @param tag
     *            The tag name of the html element
     * @param text
     *            The text that the element must contain
     * @return The {@link Element} placeholder with this tag and text
     */
    @Step("FindTagElementContainingText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public Element findTagElementContainingText(final String tag, final String text) {
        LOG.debug("FindTagElementContainingText tag={} text={}", tag, text);

        return storeCurrentElementBy(By2.combined(By.tagName(tag), By2.text(text)));
    }


    /**
     * Find a child of the current element with the specified name
     * 
     * @example FindChild ByName name="child-name"
     * @section Locatable
     * 
     * @param name
     *            The name of the child element
     * @return The {@link Element} placeholder with this id
     */
    @Step("FindChild ByName name=\"?([^\"]*)\"?")
    public Element findChildByName(final String name) {
        LOG.debug("FindChild ByName name {}", name);

        final Element child = currentElement().child(By.name(name));
        currentElement(child);

        return child;
    }


    /**
     * Find a checkbox element inside a tag with the specified tag name, that
     * has inner text matching label
     * 
     * @example FindCheckbox inside tag="form" with label="This is checkbox 1"
     * @section Locatable
     * 
     * @param tag
     *            The tag name of the container element
     * @param label
     *            The text inside the checkbox element
     * @return The {@link Element} placeholder with this tag and label
     */
    @Step("FindCheckbox inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public Element findCheckBox(final String tag, final String label) {
        LOG.debug("FindCheckbox inside tag={} with label={}", tag, label);
        return findInputInsideTag(tag, label, "checkbox");

    }


    /**
     * Find a radio element inside a tag with the specified tag name, that has
     * inner text matching label
     * 
     * @example FindRadioButton inside tag="form" with label="This is radio 2"
     * @section Locatable
     * 
     * @param tag
     *            The tag name of the container element
     * @param label
     *            The text inside the radio element
     * @return The {@link Element} placeholder with this tag and label
     */
    @Step("FindRadioButton inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public Element findRadioButton(final String tag, final String label) {
        LOG.debug("FindRadioButton inside tag={} with label={}", tag, label);
        return findInputInsideTag(tag, label, "radio");
    }


    private Element storeCurrentElementBy(final By by) {
        final Element element = driver().child(by);
        currentElement(element);

        return element;
    }


    private Element findInputInsideTag(final String tag, final String label, final String inputType) {
        final Element parentElement = driver().child(By2.combined(By.tagName(tag), By2.text(label)));
        final Element element = parentElement
                .child(By2.combined(By.tagName("input"), By2.attribute("type", inputType)));

        currentElement(element);

        return element;
    }
}
