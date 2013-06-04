package uk.co.stfo.adriver.substeps.configuration;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class ADriverConfigurationBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ADriverConfigurationBuilder.class);

    private static final String DEFAULT_PROPERTY_NAME = "/default.properties";
    private final List<String> propertyFiles;
    private boolean includeDefaultProperties;


    public ADriverConfigurationBuilder() {
        this.propertyFiles = Lists.newArrayList();
        this.includeDefaultProperties = false;
    }


    public ADriverConfigurationBuilder addDefaultProperties() {
        includeDefaultProperties = true;
        return this;
    }


    public ADriverConfigurationBuilder addCustomProperties(final String resourceFilename) {
        propertyFiles.add(File.separator + resourceFilename);
        return this;
    }


    public ADriverConfiguration build() {
        final PropertiesConfiguration config = new PropertiesConfiguration();

        // Add the properties in reverse-order, as we want most recent to be
        // highest priority
        for (int i = propertyFiles.size() - 1; i >= 0; i--) {
            addFileToConfig(asUrl(propertyFiles.get(i)), config);
        }

        if (includeDefaultProperties) {
            addFileToConfig(asUrl(DEFAULT_PROPERTY_NAME), config);
        }

        return ADriverConfiguration.configureWith(config);
    }


    private void addFileToConfig(final URL url, final PropertiesConfiguration config) {
        try {
            LOG.debug("Loading properties: " + url);
            config.load(url);
        } catch (final ConfigurationException ex) {
            LOG.warn("Could not load properties: " + url, ex);
        }
    }


    private URL asUrl(final String path) {
        return ADriverConfigurationBuilder.class.getResource(path);
    }
}
