package uk.co.stfo.adriver.substeps.runner.notification;

import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.runner.INotifier;

public class NotifierAdapter implements INotifier {

    @Override
    public void notifyNodeFailed(final IExecutionNode rootNode, final Throwable cause) {
        // No-op
    }


    @Override
    public void notifyNodeStarted(final IExecutionNode node) {
        // No-op
    }


    @Override
    public void notifyNodeFinished(final IExecutionNode node) {
        // No-op
    }


    @Override
    public void notifyNodeIgnored(final IExecutionNode node) {
        // No-op
    }

}
