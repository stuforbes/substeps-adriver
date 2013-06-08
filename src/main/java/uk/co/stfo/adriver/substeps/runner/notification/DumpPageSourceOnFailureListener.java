package uk.co.stfo.adriver.substeps.runner.notification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;

public class DumpPageSourceOnFailureListener extends NotifierAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DumpPageSourceOnFailureListener.class);

    private final Supplier<ADriverConfiguration> configuration;
    private final Supplier<TestRun> currentTestRun;


    public DumpPageSourceOnFailureListener(final Supplier<ADriverConfiguration> configuration,
            final Supplier<TestRun> currentTestRun) {
        this.configuration = configuration;
        this.currentTestRun = currentTestRun;
    }


    @Override
    public void notifyNodeFailed(final IExecutionNode rootNode, final Throwable cause) {
        if (configuration.get().dumpHtmlOnFailure()) {
            final FileWriter writer = writerFor(rootNode);

            try {
                writer.write("Executing node " + rootNode.getId() + " failed\n");
                writer.write(rootNode.getDescription() + "\n\n");
                writer.write(writeErrorToString(cause));
                currentTestRun.get().webDriver().dumpSourceTo(writer);
            } catch (final IOException ex) {
                LOG.error("Could not write to writer", ex);
            }

        }
    }


    private FileWriter writerFor(final IExecutionNode rootNode) {
        try {
            return new FileWriter(fileNameFor(rootNode));
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
