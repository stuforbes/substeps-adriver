package uk.co.stfo.adriver.substeps.runner.notification;

import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;

public class UpdateTestRunOnFailureNotifier extends NotifierAdapter {

    private final Supplier<TestRun> testRunSupplier;


    public UpdateTestRunOnFailureNotifier(final Supplier<TestRun> testRunSupplier) {
        super();
        this.testRunSupplier = testRunSupplier;
    }


    @Override
    public void notifyNodeFailed(final IExecutionNode arg0, final Throwable arg1) {

        final TestRun testRun = testRunSupplier.get();
        // possible to have a failure before the testrun has been
        // initialised - missing ' default.webdriver.timeout.secs' property for
        // example
        if (testRun != null) {
            testRun.failed();
        }
    }
}
