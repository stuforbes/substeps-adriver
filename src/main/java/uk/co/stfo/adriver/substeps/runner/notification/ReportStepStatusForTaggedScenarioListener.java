package uk.co.stfo.adriver.substeps.runner.notification;

import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.execution.node.NodeWithChildren;
import com.technophobia.substeps.execution.node.StepNode;
import com.technophobia.substeps.execution.node.TaggedNode;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.StepReporter;

import java.util.*;

public class ReportStepStatusForTaggedScenarioListener extends NotifierAdapter{

    private final List<IExecutionNode> stack;
    private List<String> applicableTags;
    private StepReporter stepReporter;

    public ReportStepStatusForTaggedScenarioListener(StepReporter stepReporter, final String... applicableTags){
        this(stepReporter, Arrays.asList(applicableTags));
    }

    public ReportStepStatusForTaggedScenarioListener(StepReporter stepReporter, final List<String> applicableTags){
        this.stepReporter = stepReporter;
        this.applicableTags = applicableTags;
        this.stack = new ArrayList<IExecutionNode>();
    }

    @Override
    public void notifyNodeStarted(final IExecutionNode node) {
        if(isTagged(node)){
            stack.add(node);
        }
    }

    @Override
    public void notifyNodeFinished(final IExecutionNode node) {
        doNodeComplete(node);
    }

    @Override
    public void notifyNodeFailed(final IExecutionNode node, final Throwable cause) {
        doNodeComplete(node);
    }

    private boolean isTagged(IExecutionNode node) {
        if(node instanceof TaggedNode){
            List<String> tags = new ArrayList<String>(((TaggedNode)node).getTags());
            tags.retainAll(applicableTags);

            return !tags.isEmpty();
        }
        return false;
    }

    private void doNodeComplete(IExecutionNode node) {
        IExecutionNode currentNode = popCurrentNode();

        if(isCurrentNodeValid(node, currentNode) && isLeafNode(node)){
            stepReporter.report(currentNode, stackArray());
        }
    }

    private boolean isLeafNode(IExecutionNode node) {
        return (node instanceof StepNode) && (!(node instanceof NodeWithChildren) || (node instanceof NodeWithChildren && !((NodeWithChildren)node).hasChildren()));
    }

    private boolean isCurrentNodeValid(IExecutionNode node, IExecutionNode currentNode) {
        return currentNode != null && node.getId() == currentNode.getId();
    }

    private IExecutionNode popCurrentNode() {
        if(!stack.isEmpty()){
            return stack.remove(stack.size()-1);
        }
        return null;
    }

    private IExecutionNode[] stackArray() {
        return stack.toArray(new IExecutionNode[stack.size()]);
    }
}
