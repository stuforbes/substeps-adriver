package uk.co.stfo.adriver.substeps.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfigurationBuilder;
import uk.co.stfo.adriver.substeps.driver.ADriverFactory;
import uk.co.stfo.adriver.substeps.driver.ConfiguredADriverFactory;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.substeps.runner.INotificationDistributor;
import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryScenario;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeEveryScenario;

public class DriverInitialisation {

    private static final String TEST_RUN_EXECUTION_CONTEXT_KEY = "__TEST_RUN__";
    private static final String A_DRIVER_FACTORY_EXECUTION_CONTEXT_KEY = "__A_DRIVER_FACTORY__";
    private static final String CONFIGURATION_EXECUTION_CONTEXT_KEY = "__CONFIGURATION__";

    private static final Logger LOG = LoggerFactory.getLogger(DriverInitialisation.class);


    public static Supplier<TestRun> currentTestRun() {
        return new Supplier<TestRun>() {
            public TestRun get() {
                return (TestRun) ExecutionContext.get(Scope.SCENARIO, TEST_RUN_EXECUTION_CONTEXT_KEY);
            }
        };
    }


    public static Supplier<ADriverConfiguration> configuration() {
        return new Supplier<ADriverConfiguration>() {
            public ADriverConfiguration get() {
                return (ADriverConfiguration) ExecutionContext.get(Scope.SUITE, CONFIGURATION_EXECUTION_CONTEXT_KEY);
            }
        };
    }


    @BeforeAllFeatures
    public void notificationInitialisation() {
        LOG.info("Updating notification distributor");

        final INotificationDistributor notifier = (INotificationDistributor) ExecutionContext.get(Scope.SUITE,
                INotificationDistributor.NOTIFIER_DISTRIBUTOR_KEY);

        notifier.addListener(new UpdateTestRunOnFailureNotifier(currentTestRun()));
    }


    @BeforeAllFeatures
    public void createADriver() {
        LOG.info("Fetching configuration parameters");

        final ADriverConfigurationBuilder configurationBuilder = new ADriverConfigurationBuilder()
                .addDefaultProperties();

        final String environment = getEnvironment();
        System.setProperty("environment", environment);
        configurationBuilder.addCustomProperties(environment + ".properties");

        final ADriverConfiguration configuration = configurationBuilder.build();
        ExecutionContext.put(Scope.SUITE, CONFIGURATION_EXECUTION_CONTEXT_KEY, configuration);

        final ADriverFactory driverFactory = createDriverFactory(configuration);
        ExecutionContext.put(Scope.SUITE, A_DRIVER_FACTORY_EXECUTION_CONTEXT_KEY, driverFactory);
    }


    @BeforeEveryScenario
    public void basePreScenarioSetup() {
        final ADriverFactory factory = (ADriverFactory) ExecutionContext.get(Scope.SUITE,
                A_DRIVER_FACTORY_EXECUTION_CONTEXT_KEY);
        ExecutionContext.put(Scope.SCENARIO, TEST_RUN_EXECUTION_CONTEXT_KEY, new TestRun(factory.createDriver()));
    }


    @AfterEveryScenario
    public void basePostScenariotearDown() {

        final TestRun testRun = currentTestRun().get();
        if (testRun != null) {
            testRun.finaliseWebDriver((ADriverConfiguration) ExecutionContext.get(Scope.SUITE,
                    CONFIGURATION_EXECUTION_CONTEXT_KEY));
        }
    }


    private String getEnvironment() {
        final String environment = System.getProperty("environment");

        if (environment == null) {
            LOG.warn("Using default environment of 'localhost'. To use a different environment, use the -Denvironment=<ENVIRONMENT> system property");
            return "localhost";
        }
        LOG.info("Using environment " + environment);
        return environment;
    }


    private ADriverFactory createDriverFactory(final ADriverConfiguration configuration) {
        return new ConfiguredADriverFactory(configuration);
    }

}
