package uk.co.stfo.adriver.substeps.runner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import uk.co.stfo.adriver.substeps.configuration.WebdriverSubstepsConfiguration;

public class WebdriverSubstepsConfigurationTest {

    @Test
    public void checkOverrides() {

        System.setProperty("environment", "localhost");

        Assert.assertFalse(WebdriverSubstepsConfiguration.closeVisualWebDriveronFail());

    }


    @Test
    public void testRelativeURLResolvesToFileProtocol() throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        final Method determineBaseURLMethod = WebdriverSubstepsConfiguration.class.getDeclaredMethod(
                "determineBaseURL", String.class);

        determineBaseURLMethod.setAccessible(true);

        final String baseUrl = (String) determineBaseURLMethod.invoke(WebdriverSubstepsConfiguration.class, "src/web");

        Assert.assertThat(baseUrl, startsWith("file:/"));

        final String baseUrl2 = (String) determineBaseURLMethod.invoke(WebdriverSubstepsConfiguration.class,
                "./src/web");

        final File current = new File(".");

        Assert.assertThat(baseUrl2, is(current.toURI().toString() + "src/web"));

        final String baseUrl3 = (String) determineBaseURLMethod.invoke(WebdriverSubstepsConfiguration.class,
                "http://blah-blah.com/src/web");

        Assert.assertThat(baseUrl3, startsWith("http://"));

        final String baseUrl4 = (String) determineBaseURLMethod.invoke(WebdriverSubstepsConfiguration.class,
                "file://some-path/whatever");

        Assert.assertThat(baseUrl4, is("file://some-path/whatever"));

    }
}
