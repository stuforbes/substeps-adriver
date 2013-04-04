package uk.co.stuforbes.windowlicker.substeps.runner;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.runner.INotifier;

/**
 * 
 * @author imoore
 * 
 */
public class TestFailureListener implements INotifier {

    private final Supplier<TestRun> testRunSupplier;


    public TestFailureListener(final Supplier<TestRun> testRunSupplier) {
        super();
        this.testRunSupplier = testRunSupplier;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.technophobia.substeps.runner.INotifier#notifyTestFailed(com.technophobia
     * .substeps.execution.ExecutionNode, java.lang.Throwable)
     */
    public void notifyNodeFailed(final IExecutionNode arg0, final Throwable arg1) {

        TestRun testRun = testRunSupplier.get();
        // possible to have a failure before the testrun has been
        // initialised - missing ' default.webdriver.timeout.secs' property for
        // example
        if (testRun != null) {
            testRun.failed();
        }

    }


    /*
     * (non-Javadoc)
     * 
     * @see com.technophobia.substeps.runner.INotifier#notifyTestFinished(com.
     * technophobia.substeps.execution.ExecutionNode)
     */
    public void notifyNodeFinished(final IExecutionNode arg0) {
        // no op
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.technophobia.substeps.runner.INotifier#notifyTestStarted(com.technophobia
     * .substeps.execution.ExecutionNode)
     */
    public void notifyNodeStarted(final IExecutionNode arg0) {
        // no op
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.technophobia.substeps.runner.INotifier#notifyTestIgnored(com.technophobia
     * .substeps.execution.ExecutionNode)
     */
    public void notifyNodeIgnored(final IExecutionNode node) {
        // TODO Auto-generated method stub

    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.technophobia.substeps.runner.INotifier#addListener(com.technophobia
     * .substeps.runner.INotifier)
     */
    public void addListener(final INotifier listener) {
        // TODO Auto-generated method stub

    }
}
