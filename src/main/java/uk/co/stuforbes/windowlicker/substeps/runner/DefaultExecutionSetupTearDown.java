package uk.co.stuforbes.windowlicker.substeps.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stuforbes.windowlicker.substeps.common.MutableSupplier;
import uk.co.stuforbes.windowlicker.substeps.configuration.WebdriverSubstepsConfiguration;
import uk.co.stuforbes.windowlicker.substeps.driver.WebDriverFactory;
import uk.co.stuforbes.windowlicker.substeps.execution.ExecutionContextSupplier;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.substeps.runner.INotificationDistributor;
import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryScenario;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeEveryScenario;

public class DefaultExecutionSetupTearDown {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExecutionSetupTearDown.class);
    private long startTimeMillis;

    private static final MutableSupplier<TestRun> testRunSupplier = new ExecutionContextSupplier<TestRun>(
            Scope.SUITE, "__TEST_RUN__");


    public static Supplier<TestRun> currentTestRun() {
        return testRunSupplier;
    }


    @BeforeAllFeatures
    public final void beforeAllFeaturesSetup() {

        final INotificationDistributor notifier = (INotificationDistributor) ExecutionContext.get(
                Scope.SUITE, INotificationDistributor.NOTIFIER_DISTRIBUTOR_KEY);

        notifier.addListener(new TestFailureListener(testRunSupplier));

        logger.info("beforeAllTestsSetup");

        String env = System.getProperty("environment");

        if (env == null) {
            logger.warn("\n\n\n****** NO ENVIRONMENT SET DEFAULTING TO LOCALHOST ADD -Denvironment=mycomputer\" TO OVERRIDE******* \n\n\n\n");
            env = "localhost";
            System.setProperty("environment", env);
        }

        logger.info("env prop: " + env);


        WebDriverFactory factory = createWebDriverFactory();
        
        ExecutionContext.put(Scope.SUITE, WebDriverFactory.WEB_DRIVER_FACTORY_KEY, factory);
    }


    @BeforeEveryScenario
    public final void basePreScenarioSetup() {
        startTimeMillis = System.currentTimeMillis();

        WebDriverFactory factory = (WebDriverFactory) ExecutionContext.get(Scope.SUITE, WebDriverFactory.WEB_DRIVER_FACTORY_KEY);
        testRunSupplier.set(new TestRun(factory.createWebDriver()));
    }


    @AfterEveryScenario
    public final void basePostScenariotearDown() {


        boolean doShutdown = true;
        // reasons *NOT* to shutdown

        final TestRun testRun = testRunSupplier.get();

        if (!WebdriverSubstepsConfiguration.shutDownWebdriver()) {
            // this overrides everything else
            System.out.println("global don't shutdown");
            doShutdown = false;
        } else if (testRun != null
                && !testRun.hasPassed()
                && (!WebdriverSubstepsConfiguration.closeVisualWebDriveronFail() && WebdriverSubstepsConfiguration.driverType().isVisual())) {

            System.out.println("failed and visual shutdown");
            doShutdown = false;
        }

        if (doShutdown) {

            if (testRun != null) {
                System.out.println("testRunSupplier.get().hasPassed(): "
                        + testRun.hasPassed());
            }

            System.out.println("WebdriverSubstepsConfiguration.closeVisualWebDriveronFail(): "
                    + WebdriverSubstepsConfiguration.closeVisualWebDriveronFail());

            System.out.println("driverType().isVisual(): " + WebdriverSubstepsConfiguration.driverType().isVisual());

            System.out.println("doing shutdown");

            if (testRun != null) {
                testRun.finaliseWebDriver();
            }
        }

        // TODO put long test threshold in config

        final long ticks = (System.currentTimeMillis() - startTimeMillis) / 1000;

        if (ticks > 30) {
            logger.warn(String.format("Test scenario took %s seconds", ticks));
        } else {
            logger.info(String.format("Test scenario took %s seconds", ticks));
        }
    }

    private WebDriverFactory createWebDriverFactory() {
        Class<? extends WebDriverFactory> webDriverFactoryClass = WebdriverSubstepsConfiguration.getWebDriverFactoryClass();

        logger.debug("Creating WebDriverFactory of type [{}]", webDriverFactoryClass.getName());

        try {
            return webDriverFactoryClass.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException(String.format("Failed to create WebDriverFactory %s.", webDriverFactoryClass.getName()), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(String.format("Failed to create WebDriverFactory %s.", webDriverFactoryClass.getName()), ex);
        }
    }

}
