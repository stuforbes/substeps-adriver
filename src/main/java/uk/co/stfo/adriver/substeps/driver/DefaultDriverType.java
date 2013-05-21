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
package uk.co.stfo.adriver.substeps.driver;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.WebdriverSubstepsConfiguration;
import uk.co.stfo.adriver.substeps.driver.phantomjs.WorkingPhantomJSDriverServiceFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public enum DefaultDriverType implements DriverType {
	
	FIREFOX(true) {
		public WebDriver createWebDriverFrom() {
			return new FirefoxDriver();
		}
	},
	// If using PhantomJS, make sure you set the webdriver.shutdown property to 'false'. Otherwise, the external
	// PhantomJS process gets killed after every scenario
	PHANTOMJS(false) {
		public WebDriver createWebDriverFrom() {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setJavascriptEnabled(true);
			
			PhantomJSDriverService driverService = WorkingPhantomJSDriverServiceFactory.createDefaultService(capabilities);
			
			return new PhantomJSDriver(driverService, capabilities);
		}
	},
	HTMLUNIT(false) {
		public WebDriver createWebDriverFrom() {
			final HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_10);
            htmlUnitDriver.setJavascriptEnabled(!WebdriverSubstepsConfiguration.isJavascriptDisabledWithHTMLUnit());

            // Run via a proxy - HTML unit only for timebeing
            final String proxyHost = WebdriverSubstepsConfiguration.getHtmlUnitProxyHost();
            if (!StringUtils.isEmpty(proxyHost)) {
                final int proxyPort = WebdriverSubstepsConfiguration.getHtmlUnitProxyPort();
                htmlUnitDriver.setProxy(proxyHost, proxyPort);
            }

            setDriverLocale(htmlUnitDriver);

            return htmlUnitDriver;
		}
	},
	CHROME(true) {
		public WebDriver createWebDriverFrom() {
			return new ChromeDriver();
		}
	},
	IE(true) {
		public WebDriver createWebDriverFrom() {
			// apparently this is required to get around some IE security
            // restriction.

            final DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

            LOG.warn("Using IE Webdriver with IGNORING SECURITY DOMAIN");

            return new InternetExplorerDriver(ieCapabilities);
		}
	};
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverType.class);

	private DefaultDriverType(final boolean visual) {
		this.visual = visual;
	}

	private boolean visual;

	public boolean isVisual() {
		return visual;
	}
	
	private static void setDriverLocale(final WebDriver driver) {

        try {
            final Field field = driver.getClass().getDeclaredField("webClient");
            if (field != null) {
                final boolean original = field.isAccessible();
                field.setAccessible(true);

                final WebClient webClient = (WebClient) field.get(driver);
                if (webClient != null) {
                    webClient.addRequestHeader("Accept-Language", "en-gb");
                }
                field.setAccessible(original);
            } else {
                Assert.fail("Failed to get webclient field to set accept language");
            }
        } catch (final IllegalAccessException ex) {

            LOG.warn(ex.getMessage());

        } catch (final SecurityException e) {

            LOG.warn(e.getMessage());
        } catch (final NoSuchFieldException e) {

            LOG.warn(e.getMessage());
        }
    }
}
