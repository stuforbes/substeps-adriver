package uk.co.stfo.adriver.substeps.driver.webdriver;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.driver.DriverType;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class WebDriverFactories {

    private static Map<DriverType, Function<ADriverConfiguration, WebDriver>> driverMap = initDriverMap();


    public static WebDriver createFor(final DriverType driverType, final ADriverConfiguration configuration) {

        if (driverMap.containsKey(driverType)) {
            final WebDriver driver = driverMap.get(driverType).apply(configuration);

            if (configuration.takeScreenshotOnFailure() && !(driver instanceof TakesScreenshot)) {
                throw new IllegalStateException(
                        "Substeps is configured to take screenshots, but the defined webdriver ("
                                + configuration.getDriverType().name() + ") does not support this");
            }

            return driver;
        }
        throw new IllegalArgumentException("Unknown WebDriver type: " + driverType);
    }


    private static Function<ADriverConfiguration, WebDriver> firefoxDriver() {
        return new Function<ADriverConfiguration, WebDriver>() {
            @Override
            public WebDriver apply(final ADriverConfiguration input) {
                return new FirefoxDriver();
            }
        };
    }


    private static Function<ADriverConfiguration, WebDriver> phantomJsDriver() {
        return new Function<ADriverConfiguration, WebDriver>() {
            @Override
            public WebDriver apply(final ADriverConfiguration input) {
                final DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setJavascriptEnabled(true);

                final PhantomJSDriverService driverService = PhantomJSDriverService.createDefaultService(capabilities);

                return new PhantomJSDriver(driverService, capabilities);
            }
        };
    }


    private static Function<ADriverConfiguration, WebDriver> htmlUnitDriver() {
        return new Function<ADriverConfiguration, WebDriver>() {
            @Override
            public WebDriver apply(final ADriverConfiguration input) {
                final HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.FIREFOX_10);
                htmlUnitDriver.setJavascriptEnabled(Boolean.valueOf(input.getProperty("javascript.enabled")));

                final String proxyHost = input.getProperty("proxy.host");
                if (!StringUtils.isEmpty(proxyHost)) {
                    final int proxyPort = Integer.valueOf(input.getProperty("proxy.port"));
                    htmlUnitDriver.setProxy(proxyHost, proxyPort);
                }

                return htmlUnitDriver;
            }
        };
    }


    private static Function<ADriverConfiguration, WebDriver> chromeDriver() {
        return new Function<ADriverConfiguration, WebDriver>() {
            @Override
            public WebDriver apply(final ADriverConfiguration input) {
                return new ChromeDriver();
            }
        };
    }


    private static Function<ADriverConfiguration, WebDriver> ieDriver() {
        return new Function<ADriverConfiguration, WebDriver>() {
            @Override
            public WebDriver apply(final ADriverConfiguration input) {
                final DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                        true);

                return new InternetExplorerDriver(ieCapabilities);
            }
        };
    }


    private static Map<DriverType, Function<ADriverConfiguration, WebDriver>> initDriverMap() {
        final Map<DriverType, Function<ADriverConfiguration, WebDriver>> map = Maps.newHashMap();
        map.put(DriverType.FIREFOX, firefoxDriver());
        map.put(DriverType.PHANTOMJS, phantomJsDriver());
        map.put(DriverType.HTMLUNIT, htmlUnitDriver());
        map.put(DriverType.CHROME, chromeDriver());
        map.put(DriverType.IE, ieDriver());
        return Collections.unmodifiableMap(map);
    }
}
