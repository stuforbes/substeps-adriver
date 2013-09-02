package uk.co.stfo.adriver.substeps.runner.notification;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.ExecutionState;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;

public class TakeScreenshotOnFailureListener extends NotifierAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TakeScreenshotOnFailureListener.class);

    private final Supplier<ADriverConfiguration> configuration;
    private final Supplier<TestRun> currentTestRun;

    public TakeScreenshotOnFailureListener(){
        this(ExecutionState.configuration(), ExecutionState.currentTestRun());
    }

    public TakeScreenshotOnFailureListener(final Supplier<ADriverConfiguration> configuration,
            final Supplier<TestRun> currentTestRun) {
        this.configuration = configuration;
        this.currentTestRun = currentTestRun;
    }


    @Override
    public void notifyNodeFailed(final IExecutionNode rootNode, final Throwable cause) {
        if (configuration.get().takeScreenshotOnFailure()) {
            final BufferedOutputStream os = outputStreamFor(rootNode);
            if (os != null) {
                try {
                    currentTestRun.get().webDriver().dump().screenshotTo(os);
                } catch (final IOException ex) {
                    LOG.error("Could not write to writer", ex);
                } finally {
                    try {
                        os.flush();
                        os.close();
                    } catch (final IOException ex) {
                        LOG.error("IOException trying to close output stream", ex);
                    }
                }
            }
        }
    }


    private BufferedOutputStream outputStreamFor(final IExecutionNode rootNode) {
        try {
            return new BufferedOutputStream(new FileOutputStream(fileNameFor(rootNode)));
        } catch (final FileNotFoundException ex) {
            LOG.error("Could not create screenshot file for node {}", rootNode.getId(), ex);
        } catch (final IOException ex) {
            LOG.error("Could not create screenshot file for node {}", rootNode.getId(), ex);
        }
        return null;
    }


    private String fileNameFor(final IExecutionNode rootNode) throws IOException {
        final File folder = new File(configuration.get().getScreenshotFolder());
        if (!folder.exists()) {
            FileUtils.forceMkdir(folder);
        }

        final File file = new File(folder, rootNode.getId() + ".gif");
        file.createNewFile();
        return file.getAbsolutePath();
    }
}
