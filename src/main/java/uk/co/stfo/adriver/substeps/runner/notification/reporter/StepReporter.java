package uk.co.stfo.adriver.substeps.runner.notification.reporter;

import com.technophobia.substeps.execution.node.IExecutionNode;

public interface StepReporter {

    void report(IExecutionNode node, IExecutionNode... ancestry);
}
