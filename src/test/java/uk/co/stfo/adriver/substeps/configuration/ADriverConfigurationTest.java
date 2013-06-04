package uk.co.stfo.adriver.substeps.configuration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.configuration.Configuration;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.stfo.adriver.substeps.driver.DriverType;

public class ADriverConfigurationTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Configuration configuration;

    private ADriverConfiguration driverConfiguration;


    @Before
    public void initialise() {
        this.configuration = context.mock(Configuration.class);

        this.driverConfiguration = ADriverConfiguration.configureWith(configuration);
    }


    @Test
    public void canGetBaseUrl() {
        final String baseUrl = "http://base.url.com";

        prepareConfigWithString("base.url", baseUrl);

        assertThat(driverConfiguration.getBaseUrl(), is(baseUrl));
    }


    @Test
    public void canGetDriverType() {

        prepareConfigWithString("driver.type", "FIREFOX");

        assertThat(driverConfiguration.getDriverType(), is(DriverType.FIREFOX));
    }


    @Test
    public void canGetPollTimeout() {
        final long timeout = 10000;

        prepareConfigWithLong("poll.timeout", timeout);

        assertThat(driverConfiguration.getPollTimeout(), is(timeout));
    }


    @Test
    public void canGetPollFrequency() {
        final long freq = 100;

        prepareConfigWithLong("poll.frequency", freq);

        assertThat(driverConfiguration.getPollFrequency(), is(freq));
    }


    @Test
    public void canGetCloseWebDriverStrategy() {

        prepareConfigWithString("close.web.driver.strategy", "ON_TEST_SUCCESS_ONLY");

        assertThat(driverConfiguration.getCloseWebDriverStrategy(), is(CloseWebDriverStrategy.ON_TEST_SUCCESS_ONLY));
    }


    @Test
    public void canGetGenericPropertyStrings() {

        prepareConfigWithString("a.property.key", "a.property.value");

        assertThat(driverConfiguration.getProperty("a.property.key"), is("a.property.value"));
    }


    private void prepareConfigWithString(final String key, final String value) {

        context.checking(new Expectations() {
            {
                oneOf(configuration).getString(key);
                will(returnValue(value));
            }
        });
    }


    private void prepareConfigWithLong(final String key, final long value) {

        context.checking(new Expectations() {
            {
                oneOf(configuration).getLong(key);
                will(returnValue(value));
            }
        });
    }
}
