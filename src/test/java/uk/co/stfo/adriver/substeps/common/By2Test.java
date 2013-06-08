package uk.co.stfo.adriver.substeps.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

public class By2Test {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();


    @Test
    public void byCombinedThrowsExceptionIfBysAreInvalid() {
        checkCombinedBysAreInvalid(null);
        checkCombinedBysAreInvalid(new By[0]);
    }


    @Test
    public void byCombinedFindsElementInDocumentIfBothBysMatch() {

        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element = context.mock(WebElement.class);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementsByXPath("*[@id = 'an-id']");
                will(returnValue(Collections.singletonList(element)));

                oneOf(searchContext).findElementsByXPath(
                        ".//*[contains(concat(' ',normalize-space(@class),' '),' a-class ')]");
                will(returnValue(Collections.singletonList(element)));
            }
        });

        assertThat(By2.combined(By.id("an-id"), By.className("a-class")).findElement(searchContext), is(element));
    }


    @Test(expected = NoSuchElementException.class)
    public void byCombinedFindsNothingInDocumentIfOnly1ByMatches() {

        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element = context.mock(WebElement.class);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementsByXPath("*[@id = 'an-id']");
                will(returnValue(Collections.singletonList(element)));

                oneOf(searchContext).findElementsByXPath(
                        ".//*[contains(concat(' ',normalize-space(@class),' '),' a-class ')]");
                will(returnValue(Collections.emptyList()));
            }
        });

        By2.combined(By.id("an-id"), By.className("a-class")).findElement(searchContext);
    }


    @Test
    public void byTextThrowsExceptionIfTextIsInvalid() {
        checkTextIsInvalid(null);
        checkTextIsInvalid("");
        checkTextIsInvalid(" ");
        checkTextIsInvalid("\n\t ");
    }


    @Test
    public void byTextFindsElementInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element = context.mock(WebElement.class);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementByXPath(
                        "self::node()[text()='some element text'] | .//*[text()='some element text']");
                will(returnValue(element));
            }
        });

        assertThat(By2.text("some element text").findElement(searchContext), is(element));
    }


    @Test
    public void byTextFindsElementsInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element1 = context.mock(WebElement.class, "element1");
        final WebElement element2 = context.mock(WebElement.class, "element2");
        final WebElement element3 = context.mock(WebElement.class, "element3");
        final List<WebElement> elements = Arrays.asList(element1, element2, element3);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementsByXPath(
                        "self::node()[text()='some element text'] | .//*[text()='some element text']");
                will(returnValue(elements));
            }
        });

        assertThat(By2.text("some element text").findElements(searchContext), is(elements));
    }


    @Test
    public void byTextFragmentThrowsExceptionIfTextIsInvalid() {
        checkTextFragmentIsInvalid(null);
        checkTextFragmentIsInvalid("");
        checkTextFragmentIsInvalid(" ");
        checkTextFragmentIsInvalid("\n\t ");
    }


    @Test
    public void byTextFragmentFindsElementInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element = context.mock(WebElement.class);

        context.checking(new Expectations() {
            {
                oneOf(searchContext)
                        .findElementByXPath(
                                "self::node()[contains(text(), 'some element text')] | .//*[contains(text(), 'some element text')]");
                will(returnValue(element));
            }
        });

        assertThat(By2.textFragment("some element text").findElement(searchContext), is(element));
    }


    @Test
    public void byTextFragmentFindsElementsInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element1 = context.mock(WebElement.class, "element1");
        final WebElement element2 = context.mock(WebElement.class, "element2");
        final WebElement element3 = context.mock(WebElement.class, "element3");
        final List<WebElement> elements = Arrays.asList(element1, element2, element3);

        context.checking(new Expectations() {
            {
                oneOf(searchContext)
                        .findElementsByXPath(
                                "self::node()[contains(text(), 'some element text')] | .//*[contains(text(), 'some element text')]");
                will(returnValue(elements));
            }
        });

        assertThat(By2.textFragment("some element text").findElements(searchContext), is(elements));
    }


    @Test
    public void byAttributeThrowsExceptionIfNameIsInvalid() {
        checkAttributeIsInvalid(null);
        checkAttributeIsInvalid("");
        checkAttributeIsInvalid(" ");
        checkAttributeIsInvalid("\n\t ");
    }


    @Test
    public void byAttributeFindsElementInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element = context.mock(WebElement.class);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementByXPath(".//*[@attribute-name='attribute-value']");
                will(returnValue(element));
            }
        });

        assertThat(By2.attribute("attribute-name", "attribute-value").findElement(searchContext), is(element));
    }


    @Test
    public void byAttributeFindsElementsInDocument() {
        final SearchContextFindsByXPath searchContext = context.mock(SearchContextFindsByXPath.class);
        final WebElement element1 = context.mock(WebElement.class, "element1");
        final WebElement element2 = context.mock(WebElement.class, "element2");
        final WebElement element3 = context.mock(WebElement.class, "element3");
        final List<WebElement> elements = Arrays.asList(element1, element2, element3);

        context.checking(new Expectations() {
            {
                oneOf(searchContext).findElementsByXPath(".//*[@attribute-name='attribute-value']");
                will(returnValue(elements));
            }
        });

        assertThat(By2.attribute("attribute-name", "attribute-value").findElements(searchContext), is(elements));
    }


    private void checkCombinedBysAreInvalid(final By[] bys) {
        try {
            By2.combined(bys);
            fail("Expecting an " + IllegalArgumentException.class.getName() + " to be thrown");
        } catch (final IllegalArgumentException ex) {
            // No-op
        }
    }


    private void checkTextIsInvalid(final String text) {
        try {
            By2.text(text);
            fail("Expecting an " + IllegalArgumentException.class.getName() + " to be thrown");
        } catch (final IllegalArgumentException ex) {
            // No-op
        }
    }


    private void checkTextFragmentIsInvalid(final String text) {
        try {
            By2.textFragment(text);
            fail("Expecting an " + IllegalArgumentException.class.getName() + " to be thrown");
        } catch (final IllegalArgumentException ex) {
            // No-op
        }
    }


    private void checkAttributeIsInvalid(final String name) {
        try {
            By2.attribute(name, "value");
            fail("Expecting an " + IllegalArgumentException.class.getName() + " to be thrown");
        } catch (final IllegalArgumentException ex) {
            // No-op
        }
    }

    private interface SearchContextFindsByXPath extends SearchContext, FindsByXPath {
        // No-op
    }
}
