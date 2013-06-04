package uk.co.stfo.adriver.substeps.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import uk.co.stfo.adriver.util.ByUtils;

import com.google.common.collect.Lists;

/**
 * Additional criteria to find elements in a document. Extension of {@link By}
 * 
 * @author sforbes
 * 
 */
public class By2 {

    /**
     * Performs all by operations and returns the intersection of returned
     * elements
     * 
     * @param bys
     *            All the bys that are to be applied to the search context
     * @return A {@link By} that locates all elements that all bys match
     */
    public static By combined(final By... bys) {
        if (bys == null || bys.length == 0) {
            throw new IllegalArgumentException("Cannot find elements if no bys are defined");
        }
        return new ByCombined(bys);
    }


    /**
     * Find an element/elements that contain the specified inner text
     * 
     * @param text
     * @return A {@link By} that has inner text that contains the contents of
     *         the "text" attribute
     */
    public static By text(final String text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Cannot find elements with null or empty text.");
        }
        return new ByText(text);
    }


    public static By attribute(final String name, final String value) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Cannot find elements with null or empty attribute.");
        }
        return new ByAttribute(name, value);
    }

    static class ByCombined extends By {

        private final By[] bys;


        public ByCombined(final By... bys) {
            this.bys = bys;
        }


        @Override
        public List<WebElement> findElements(final SearchContext context) {
            final List<WebElement> matches = Lists.newArrayList();

            for (final By by : bys) {
                final List<WebElement> elements = by.findElements(context);
                if (matches.isEmpty()) {
                    matches.addAll(elements);
                } else {
                    matches.retainAll(elements);
                }
            }
            return matches;
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("By.a combination of (");
            for (int i = 0; i < bys.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(ByUtils.asString(bys[i]));
            }
            sb.append(")");
            return sb.toString();
        }
    }

    static class ByText extends By {

        private static final String XPATH_PATTERN = "self::node()[contains(text(), '%s')] | .//*[contains(text(), '%s')]";

        private final String text;


        public ByText(final String text) {
            this.text = text;
        }


        @Override
        public List<WebElement> findElements(final SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(String.format(XPATH_PATTERN, text, text));
        }


        @Override
        public WebElement findElement(final SearchContext context) {
            return ((FindsByXPath) context).findElementByXPath(String.format(XPATH_PATTERN, text, text));
        }


        @Override
        public String toString() {
            return "By.text: " + text;
        }
    }

    static class ByAttribute extends By {
        private final String name;
        private final String value;


        public ByAttribute(final String name, final String value) {
            this.name = name;
            this.value = value;
        }


        @Override
        public List<WebElement> findElements(final SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(".//*[@" + name + "='" + value + "']");
        }


        @Override
        public WebElement findElement(final SearchContext context) {
            return ((FindsByXPath) context).findElementByXPath(".//*[@" + name + "='" + value + "']");
        }


        @Override
        public String toString() {
            return "By.attribute: " + name + "='" + value + "'";
        }
    }
}
