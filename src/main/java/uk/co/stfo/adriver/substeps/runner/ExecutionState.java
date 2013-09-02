package uk.co.stfo.adriver.substeps.runner;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;

public class ExecutionState {
    public static final String TEST_RUN_EXECUTION_CONTEXT_KEY = "__TEST_RUN__";
    public static final String CONFIGURATION_EXECUTION_CONTEXT_KEY = "__CONFIGURATION__";

    public static Supplier<TestRun> currentTestRun() {
        return new Supplier<TestRun>() {
            @Override
            public TestRun get() {
                return (TestRun) ExecutionContext.get(Scope.SUITE, TEST_RUN_EXECUTION_CONTEXT_KEY);
            }
        };
    }


    public static Supplier<ADriverConfiguration> configuration() {
        return new Supplier<ADriverConfiguration>() {
            @Override
            public ADriverConfiguration get() {
                return (ADriverConfiguration) ExecutionContext.get(Scope.SUITE, CONFIGURATION_EXECUTION_CONTEXT_KEY);
            }
        };
    }
}
