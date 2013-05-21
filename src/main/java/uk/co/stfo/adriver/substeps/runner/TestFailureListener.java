package uk.co.stfo.adriver.substeps.runner;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.runner.INotifier;

public class TestFailureListener implements INotifier {

    private final Supplier<TestRun> testRunSupplier;


    public TestFailureListener(final Supplier<TestRun> testRunSupplier) {
        super();
        this.testRunSupplier = testRunSupplier;
    }


    public void notifyNodeFailed(final IExecutionNode arg0, final Throwable arg1) {

        final TestRun testRun = testRunSupplier.get();
        // possible to have a failure before the testrun has been
        // initialised - missing ' default.webdriver.timeout.secs' property for
        // example
        if (testRun != null) {
            testRun.failed();
        }

    }


    public void notifyNodeFinished(final IExecutionNode arg0) {
        // no op
    }


    public void notifyNodeStarted(final IExecutionNode arg0) {
        // no op
    }


    public void notifyNodeIgnored(final IExecutionNode node) {
        // TODO Auto-generated method stub

    }


    public void addListener(final INotifier listener) {
        // TODO Auto-generated method stub

    }
}
