package uk.co.stfo.adriver.substeps.runner.notification.reporter;

import com.technophobia.substeps.execution.node.IExecutionNode;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.FileReporter;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.NodeToDirectoryTransformer;

import java.io.File;

public class OutputToFileSystemStepReporterTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private NodeToDirectoryTransformer nodeToDirectoryTransformer;
    private FileReporter fileReporter1;
    private FileReporter fileReporter2;

    private StepReporter stepReporter;

    @Before
    public void initialise(){
        this.nodeToDirectoryTransformer = context.mock(NodeToDirectoryTransformer.class);
        this.fileReporter1 = context.mock(FileReporter.class, "fileReporter1");
        this.fileReporter2 = context.mock(FileReporter.class, "fileReporter2");

        this.stepReporter = new OutputToFileSystemStepReporter("parent", nodeToDirectoryTransformer, fileReporter1, fileReporter2);
    }

    @Test
    public void reportingANodeLooksUpFileHierarchyUsingAncestryAndCallsReporters(){

        final IExecutionNode grandparentNode = context.mock(IExecutionNode.class, "grandparentNode");
        final IExecutionNode parentNode = context.mock(IExecutionNode.class, "parentNode");
        final IExecutionNode childNode = context.mock(IExecutionNode.class, "childNode");

        final File grandparentDirectory = new File("grandparent");
        final File parentDirectory = new File("parent");
        final File childDirectory = new File("child");

        context.checking(new Expectations(){{
            oneOf(nodeToDirectoryTransformer).directoryFor(with(grandparentNode), with(any(File.class)));
            will(returnValue(grandparentDirectory));
            oneOf(nodeToDirectoryTransformer).directoryFor(parentNode, grandparentDirectory);
            will(returnValue(parentDirectory));
            oneOf(nodeToDirectoryTransformer).directoryFor(childNode, parentDirectory);
            will(returnValue(childDirectory));

            oneOf(fileReporter1).reportTo(childNode, childDirectory);
            oneOf(fileReporter2).reportTo(childNode, childDirectory);
        }});

        stepReporter.report(childNode, grandparentNode, parentNode);
    }



}
