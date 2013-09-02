package uk.co.stfo.adriver.substeps.runner.notification;

import com.google.common.collect.Sets;
import com.technophobia.substeps.execution.node.IExecutionNode;
import com.technophobia.substeps.execution.node.StepNode;
import com.technophobia.substeps.execution.node.TaggedNode;
import com.technophobia.substeps.runner.INotifier;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.StepReporter;

import java.util.Arrays;

public class ReportStepStatusForTaggedScenarioListenerTest {

    private static final String TAG = "tag";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private StepReporter stepReporter;

    private INotifier notifier;

    @Before
    public void initialise(){
        this.stepReporter = context.mock(StepReporter.class);
        this.notifier = new ReportStepStatusForTaggedScenarioListener(stepReporter, TAG);
    }

    @Test
    public void nodesThatAreNotTaggableDoNotCallReporterOnTestCompletion(){
        final IExecutionNode node = context.mock(IExecutionNode.class);

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void untaggedNodesDoNotCallReporterOnTestCompletion(){
        final StepNode node = context.mock(StepNode.class);

        context.checking(new Expectations(){{
            oneOf(node).getTags();
            will(returnValue(Sets.newHashSet("other-tag")));
        }});

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void taggedNodesDoCallReporterOnTestCompletion(){
        final StepNode node = context.mock(StepNode.class);

        context.checking(new Expectations(){{
            oneOf(node).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            exactly(2).of(node).getId();
            will(returnValue(1234l));

            oneOf(stepReporter).report(node);
        }});

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void untaggedNodesDoNotCallReporterOnTestFailure(){
        final StepNode node = context.mock(StepNode.class);

        context.checking(new Expectations(){{
            oneOf(node).getTags();
            will(returnValue(Sets.newHashSet("other-tag")));
        }});

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void taggedNodesDoCallReporterOnTestFailure(){
        final StepNode node = context.mock(StepNode.class);

        context.checking(new Expectations(){{
            oneOf(node).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            exactly(2).of(node).getId();
            will(returnValue(1234l));

            oneOf(stepReporter).report(node);
        }});

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void testCompletionRemovesKnowledgeOfCompletedNode(){
        final StepNode node = context.mock(StepNode.class);

        context.checking(new Expectations(){{
            oneOf(node).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            exactly(2).of(node).getId();
            will(returnValue(1234l));

            oneOf(stepReporter).report(node);
        }});

        notifier.notifyNodeStarted(node);
        notifier.notifyNodeFinished(node);
        notifier.notifyNodeFinished(node);
    }

    @Test
    public void taggedNodesInAHierarchyPassAncestorsToStepReporterOnTestCompletion(){
        final StepNode grandParent = context.mock(StepNode.class, "grandparent");
        final StepNode parent = context.mock(StepNode.class, "parent");
        final StepNode child = context.mock(StepNode.class, "child");

        context.checking(new Expectations(){{
            oneOf(grandParent).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            oneOf(parent).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            oneOf(child).getTags();
            will(returnValue(Sets.newHashSet(TAG)));

            exactly(2).of(child).getId();
            will(returnValue(1234l));

            oneOf(stepReporter).report(child, grandParent, parent);
        }});

        notifier.notifyNodeStarted(grandParent);
        notifier.notifyNodeStarted(parent);
        notifier.notifyNodeStarted(child);

        notifier.notifyNodeFinished(child);
    }
}
