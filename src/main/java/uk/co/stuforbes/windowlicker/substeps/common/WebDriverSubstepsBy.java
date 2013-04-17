/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.stuforbes.windowlicker.substeps.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/**
 * 
 * @author imoore
 * 
 */
public abstract class WebDriverSubstepsBy {

	public static ByIdAndText ByIdAndText(final String id, final String text) {
		return new ByIdAndText(id, text);
	}

	public static ByIdAndText ByIdAndCaseSensitiveText(final String id,
			final String text) {
		return new ByIdAndText(id, text, true);
	}

	public static ByTagAndAttribute ByTagAndAttribute(final String tagName,
			final String attributeName, final String attributeValue) {
		return new ByTagAndAttribute(tagName, attributeName, attributeValue);
	}

	public static ByTagAndAttributes ByTagAndAttributes(final String tagName,
			final Map<String, String> requiredAttributes) {
		return new ByTagAndAttributes(tagName, requiredAttributes);
	}

	public static ByTagAndAttributes ByTagAndAttributes(final String tagName,
			final String attributeString) {

		final Map<String, String> expectedAttributes = convertToMap(attributeString);

		return new ByTagAndAttributes(tagName, expectedAttributes);
	}

	/**
	 * Convert a string of the form type="submit",value="Search" to a map.
	 * 
	 * @example
	 * @param attributes
	 *            the attributes string
	 * @return the map
	 */
	public static Map<String, String> convertToMap(final String attributes) {
		Map<String, String> attributeMap = null;

		// split the attributes up, will be received as a comma separated list
		// of name value pairs
		final String[] nvps = attributes.split(",");

		if (nvps != null) {
			for (final String nvp : nvps) {
				final String[] split = nvp.split("=");
				if (split != null && split.length == 2) {
					if (attributeMap == null) {
						attributeMap = new HashMap<String, String>();
					}
					attributeMap.put(split[0], split[1].replaceAll("\"", ""));
				}
			}
		}

		return attributeMap;
	}

	public static ByCurrentWebElement ByCurrentWebElement(final WebElement elem) {
		return new ByCurrentWebElement(elem);
	}

	public static ByText ByText(final String text) {
		return new ByText(text);
	}

	public static ByTagAndWithText ByTagAndWithText(final String tag,
			final String text, final boolean matchPartial) {
		return new ByTagAndWithText(tag, text, matchPartial);
	}

	public static ByIdContainingText ByIdContainingText(final String id,
			final String text) {
		return new ByIdContainingText(id, text);
	}

	protected static boolean elementHasExpectedAttributes(final WebElement e,
			final Map<String, String> expectedAttributes) {
		final Map<String, String> actualValues = new HashMap<String, String>();

		for (final String key : expectedAttributes.keySet()) {
			final String elementVal = e.getAttribute(key);

			// if no attribute will this throw an exception or just return
			// null ??
			actualValues.put(key, elementVal);

		}

		final MapDifference<String, String> difference = Maps.difference(
				expectedAttributes, actualValues);
		return difference.areEqual();
	}

	static abstract class BaseBy extends By {

		@Override
		public final List<WebElement> findElements(final SearchContext context) {
			List<WebElement> matchingElems = findElementsBy(context);

			if (matchingElems == null) {
				matchingElems = Collections.emptyList();
			}

			return matchingElems;
		}

		public abstract List<WebElement> findElementsBy(
				final SearchContext context);
	}

	static class ByTagAndAttribute extends BaseBy {

		private final String tagName;
		private final String attributeName;
		private final String attributeValue;

		public ByTagAndAttribute(String tagName, String attributeName,
				String attributeValue) {
			super();
			this.tagName = tagName;
			this.attributeName = attributeName;
			this.attributeValue = attributeValue;
		}

		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			List<WebElement> matchingElems = null;

			final List<WebElement> tagElements = context.findElements(By
					.tagName(this.tagName));

			for (final WebElement elem : tagElements) {
				final String itemAttributeValue = elem
						.getAttribute(attributeName);
				if (StringUtils.isNotBlank(itemAttributeValue)
						&& itemAttributeValue.contains(attributeValue)) {
					if (matchingElems == null) {
						matchingElems = new ArrayList<WebElement>();
					}
					matchingElems.add(elem);
				}
			}

			return matchingElems;
		}

		@Override
		public String toString() {
			return "By.tag: " + tagName + " and attribute: " + attributeName
					+ "=" + attributeValue;
		}
	}

	static class ByTagAndAttributes extends BaseBy {

		private final String tagName;
		private final Map<String, String> requiredAttributes;

		ByTagAndAttributes(final String tagName,
				final Map<String, String> requiredAttributes) {
			this.tagName = tagName;
			this.requiredAttributes = requiredAttributes;
		}

		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			List<WebElement> matchingElems = null;

			final List<WebElement> tagElements = context.findElements(By
					.tagName(this.tagName));

			for (final WebElement e : tagElements) {
				// does this WebElement have the attributes that we need!

				if (elementHasExpectedAttributes(e, this.requiredAttributes)) {

					if (matchingElems == null) {
						matchingElems = new ArrayList<WebElement>();
					}
					matchingElems.add(e);
				}
			}

			return matchingElems;
		}

		@Override
		public String toString() {
			return "By.tag: " + tagName + " and attributes: "
					+ requiredAttributes;
		}
	}

	/**
	 * A By for use with the current web element, to be chained with other Bys
	 * 
	 */
	static class ByCurrentWebElement extends BaseBy {

		private final WebElement currentElement;

		public ByCurrentWebElement(final WebElement elem) {
			this.currentElement = elem;
		}

		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			final List<WebElement> matchingElems = new ArrayList<WebElement>();
			matchingElems.add(this.currentElement);

			return matchingElems;
		}
		
		@Override
		public String toString() {
			return "By.currentWebElement: " + currentElement;
		}
	}

	public static class ByText extends BaseBy {

		private final String text;

		ByText(final String text) {

			this.text = text;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext
		 * )
		 */
		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {
			if (context instanceof WebElement) {
				WebElement element = (WebElement) context;
				if (element.getText().equals(text)) {
					return Collections.singletonList(element);
				}
			}
			return Collections.emptyList();
		}

		@Override
		public String toString() {
			return "By.text: " + text;
		}
	}

	public static class ByTagAndWithText extends BaseBy {

		private final String tag;
		private final String text;
		private boolean matchPartial;

		ByTagAndWithText(final String tag, final String text,
				boolean matchPartial) {

			this.tag = tag;
			this.text = text;
			this.matchPartial = matchPartial;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext
		 * )
		 */
		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			List<WebElement> matchingElems = null;

			final List<WebElement> elems = context.findElements(By
					.tagName(this.tag));
			if (elems != null) {
				for (final WebElement e : elems) {
					boolean match;
					if (matchPartial) {
						match = e.getText().toLowerCase()
								.contains(text.toLowerCase());
					} else {
						match = this.text.equalsIgnoreCase(e.getText());
					}

					if (match) {
						if (matchingElems == null) {
							matchingElems = new ArrayList<WebElement>();
						}
						matchingElems.add(e);
					}
				}
			}

			return matchingElems;
		}

		@Override
		public String toString() {
			return "By.tag: " + tag + " and with text " + text;
		}
	}

	public static class ByIdContainingText extends BaseBy {

		protected final String text;
		protected final String id;

		ByIdContainingText(final String id, final String text) {
			this.id = id;
			this.text = text;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext
		 * )
		 */
		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			List<WebElement> matchingElems = null;

			final List<WebElement> elems = context.findElements(By.id(this.id));
			if (elems != null) {
				for (final WebElement e : elems) {

					if (e.getText() != null && e.getText().contains(this.text)) {

						if (matchingElems == null) {
							matchingElems = new ArrayList<WebElement>();
						}
						matchingElems.add(e);
					}
				}
			}

			return matchingElems;
		}
		
		@Override
		public String toString() {
			return "By.id: " + id + " and containing text " + text;
		}
	}

	public static class ByIdAndText extends BaseBy {

		protected final String text;
		protected final String id;
		protected final boolean caseSensitive;

		ByIdAndText(final String id, final String text) {
			this(id, text, false);
		}

		ByIdAndText(final String id, final String text,
				final boolean caseSensitive) {
			this.id = id;
			this.text = text;
			this.caseSensitive = caseSensitive;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext
		 * )
		 */
		@Override
		public List<WebElement> findElementsBy(final SearchContext context) {

			List<WebElement> matchingElems = null;

			final List<WebElement> elems = context.findElements(By.id(this.id));
			if (elems != null) {
				for (final WebElement e : elems) {

					if ((this.caseSensitive && this.text.equals(e.getText()))
							|| (!this.caseSensitive && this.text
									.equalsIgnoreCase(e.getText()))) {

						if (matchingElems == null) {
							matchingElems = new ArrayList<WebElement>();
						}
						matchingElems.add(e);
					}
				}
			}

			return matchingElems;
		}
		
		@Override
		public String toString() {
			return "By.id: " + id + " and with text " + text;
		}
	}
}
