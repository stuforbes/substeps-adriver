package uk.co.stfo.adriver.substeps.configuration;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.apache.commons.configuration.Configuration;
import uk.co.stfo.adriver.substeps.driver.DriverType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ADriverConfiguration {

    private final Configuration config;


    public static ADriverConfiguration configureWith(final Configuration config) {
        return new ADriverConfiguration(config);
    }


    private ADriverConfiguration(final Configuration config) {
        this.config = config;
    }


    public String getBaseUrl() {
        String baseUrl = config.getString("base.url");
        if (baseUrl != null) {
            if (!isFullyQualified(baseUrl)) {
                baseUrl = new File(baseUrl).toURI().toString();
            }
            if (baseUrl.endsWith("/")) {
                return baseUrl.substring(0, baseUrl.length() - 1);
            }
        }
        return baseUrl;
    }


    public DriverType getDriverType() {
        final String value = config.getString("driver.type");
        return value != null ? DriverType.valueOf(value) : null;
    }


    public long getPollTimeout() {
        return config.getLong("poll.timeout");
    }


    public long getPollFrequency() {
        return config.getLong("poll.frequency");
    }


    public boolean dumpHtmlOnFailure() {
        return config.getBoolean("dump.html.on.fail");
    }


    public String getDumpHtmlFolder() {
        return config.getString("dump.html.folder");
    }


    public boolean takeScreenshotOnFailure() {
        return config.getBoolean("take.screenshot.on.fail");
    }


    public String getScreenshotFolder() {
        return config.getString("screenshot.folder");
    }

    public List<String> getProfilingTags(){
        return toStringList(config.getList("profiling.tags"));
    }

    public String getProfilingOutputFolder() {
        return config.getString("profiling.output.folder");
    }


    public CloseWebDriverStrategy getCloseWebDriverStrategy() {
        final String value = config.getString("close.web.driver.strategy");
        return value != null ? CloseWebDriverStrategy.valueOf(value) : null;
    }

    public List<String> getNotifiers(){
        return toStringList(config.getList("test.notifiers"));
    }

    public String getProperty(final String name) {
        return config.getString(name);
    }


    private boolean isFullyQualified(final String url) {
        final String lowerCase = url.toLowerCase();
        return lowerCase.startsWith("http:") || lowerCase.startsWith("file:");
    }

    private List<String> toStringList(List<Object> configList) {
        return new ArrayList<String>(Collections2.transform(configList, new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return input.toString();
            }
        }));
    }
}
