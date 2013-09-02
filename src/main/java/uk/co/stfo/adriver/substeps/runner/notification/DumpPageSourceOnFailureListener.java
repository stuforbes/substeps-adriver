package uk.co.stfo.adriver.substeps.runner.notification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.ExecutionState;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;

public class DumpPageSourceOnFailureListener extends NotifierAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DumpPageSourceOnFailureListener.class);

    private final Supplier<ADriverConfiguration> configuration;
    private final Supplier<TestRun> currentTestRun;

    private final Stack<IExecutionNode> nodeStack;

    public DumpPageSourceOnFailureListener(){
        this(ExecutionState.configuration(), ExecutionState.currentTestRun());
    }

    public DumpPageSourceOnFailureListener(final Supplier<ADriverConfiguration> configuration,
            final Supplier<TestRun> currentTestRun) {
        this.configuration = configuration;
        this.currentTestRun = currentTestRun;

        this.nodeStack = new Stack<IExecutionNode>();
    }


    @Override
    public void notifyNodeStarted(final IExecutionNode node) {
        System.out.println("Notifying node started ("+nodeStack.size()+"): "+node.getLine());
        nodeStack.push(node);
    }


    @Override
    public void notifyNodeFinished(final IExecutionNode node) {
        System.out.println("Notifying node finished ("+nodeStack.size()+"): "+node.getLine());
        if(!nodeStack.isEmpty()){
            nodeStack.pop();
        }
    }


    @Override
    public void notifyNodeFailed(final IExecutionNode rootNode, final Throwable cause) {
        if (configuration.get().dumpHtmlOnFailure()) {
            final OutputStream outputStream = outputStreamFor(rootNode);

            final String message = errorHeaderMessage(cause);

            try {
                outputStream.write(message.getBytes());
                currentTestRun.get().webDriver().dump().pageSourceTo(outputStream);
            } catch (final IOException ex) {
                LOG.error("Could not write to writer", ex);
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (final IOException ex) {
                    LOG.error("IOException trying to close output stream", ex);
                }
            }

        }
    }


    private String errorHeaderMessage(final Throwable cause) {
        final StringBuilder sb = new StringBuilder();

        int depth = 0;
        final Iterator<IExecutionNode> it = nodeStack.iterator();
        while (it.hasNext()) {
            addTabs(depth, sb);
            final IExecutionNode node = it.next();

            sb.append(node.getId());
            sb.append(": ");
            sb.append(node.getLine());
            depth++;
        }

        sb.append("\n\n");
        sb.append(writeErrorToString(cause));
        return sb.toString();
    }


    private void addTabs(final int depth, final StringBuilder sb) {
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
    }


    private OutputStream outputStreamFor(final IExecutionNode rootNode) {
        try {
            return new FileOutputStream(fileNameFor(rootNode));
        } catch (final IOException ex) {
            throw new IllegalStateException("Could not create writer", ex);
        }
    }


    private String fileNameFor(final IExecutionNode rootNode) throws IOException {
        final File folder = new File(configuration.get().getDumpHtmlFolder());
        if (!folder.exists()) {
            FileUtils.forceMkdir(folder);
        }

        final File file = new File(folder, rootNode.getId() + ".txt");
        file.createNewFile();
        return file.getAbsolutePath();
    }


    private String writeErrorToString(final Throwable cause) {
        final StringWriter stringWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }
}
