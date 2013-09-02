package uk.co.stfo.adriver.substeps.runner;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.substeps.runner.INotificationDistributor;
import com.technophobia.substeps.runner.INotifier;
import com.technophobia.substeps.runner.setupteardown.Annotations.AfterAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryScenario;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfigurationBuilder;
import uk.co.stfo.adriver.substeps.driver.ADriverFactory;
import uk.co.stfo.adriver.substeps.driver.ConfiguredADriverFactory;

import java.util.List;

public class DriverInitialisation {

    private static final Logger LOG = LoggerFactory.getLogger(DriverInitialisation.class);


    @BeforeAllFeatures
    public void notificationInitialisation() {
        LOG.info("Updating notification distributor");


        final INotificationDistributor notificationDistributor = (INotificationDistributor) ExecutionContext.get(Scope.SUITE,
                INotificationDistributor.NOTIFIER_DISTRIBUTOR_KEY);

        List<String> notifiers = ExecutionState.configuration().get().getNotifiers();
        for (String notifierClassName : notifiers){
            INotifier notifier = instantiateNotifier(notifierClassName.trim());
            if(notifier != null){
                notificationDistributor.addListener(notifier);
            }
        }

//        notifier.addListener(new UpdateTestRunOnFailureNotifier(currentTestRun()));
//        notifier.addListener(new DumpPageSourceOnFailureListener(configuration(), currentTestRun()));
//        notifier.addListener(new TakeScreenshotOnFailureListener(configuration(), currentTestRun()));

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
        ExecutionContext.put(Scope.SUITE, ExecutionState.CONFIGURATION_EXECUTION_CONTEXT_KEY, configuration);

        final ADriverFactory driverFactory = createDriverFactory(configuration);
        final TestRun testRun = new TestRun(driverFactory.createDriver());
        ExecutionContext.put(Scope.SUITE, ExecutionState.TEST_RUN_EXECUTION_CONTEXT_KEY, testRun);
    }


    // @BeforeEveryScenario
    // public void basePreScenarioSetup() {
    // final ADriverFactory factory = (ADriverFactory)
    // ExecutionContext.get(Scope.SUITE,
    // A_DRIVER_FACTORY_EXECUTION_CONTEXT_KEY);
    // ExecutionContext.put(Scope.SCENARIO, TEST_RUN_EXECUTION_CONTEXT_KEY, new
    // TestRun(factory.createDriver()));
    // }

    @AfterEveryScenario
    public void basePostScenariotearDown() {

        final TestRun testRun = ExecutionState.currentTestRun().get();
        if (testRun != null) {
            testRun.clearSession();
        }
    }


    @AfterAllFeatures
    public void finaliseWebDriver() {
        final TestRun testRun = ExecutionState.currentTestRun().get();
        if (testRun != null) {
            testRun.finaliseWebDriver((ADriverConfiguration) ExecutionContext.get(Scope.SUITE,
                    ExecutionState.CONFIGURATION_EXECUTION_CONTEXT_KEY));
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

    private INotifier instantiateNotifier(String notifierClassName) {
        try {
            Class<? extends INotifier> notifierClass = (Class<? extends INotifier>) Class.forName(notifierClassName);
            return notifierClass.newInstance();
        } catch (ClassNotFoundException e) {
            LOG.warn("Could not find class "+notifierClassName);
        } catch (InstantiationException e) {
            LOG.warn("Could not instantiate class "+notifierClassName);
        } catch (IllegalAccessException e) {
            LOG.warn("Could not access class "+notifierClassName);
        }
        return null;
    }
}
