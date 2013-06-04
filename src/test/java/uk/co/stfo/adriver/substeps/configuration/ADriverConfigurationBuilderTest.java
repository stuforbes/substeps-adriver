package uk.co.stfo.adriver.substeps.configuration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.co.stfo.adriver.substeps.driver.DriverType;

public class ADriverConfigurationBuilderTest {

    @Test
    public void notAddingDefaultPropertiesDoesntInclude() {
        final ADriverConfiguration configuration = new ADriverConfigurationBuilder().build();

        assertThat(configuration.getBaseUrl(), is(nullValue()));
        assertThat(configuration.getDriverType(), is(nullValue()));
    }


    @Test
    public void addingDefaultPropertiesFetchesFromDefault() {
        final ADriverConfiguration configuration = new ADriverConfigurationBuilder().addDefaultProperties().build();

        assertThat(configuration.getBaseUrl(), is("file://"));
        assertThat(configuration.getDriverType(), is(DriverType.HTMLUNIT));
    }


    @Test
    public void addingCustomPropertiesAppendsAndOverrides() {
        final ADriverConfiguration configuration = new ADriverConfigurationBuilder()
                .addCustomProperties("builder-test.properties").addDefaultProperties().build();

        assertThat(configuration.getDriverType(), is(DriverType.FIREFOX));
        assertThat(configuration.getProperty("additional-property1"), is("value1"));
        assertThat(configuration.getProperty("additional-property2"), is("value2"));
    }
}
