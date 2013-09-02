package uk.co.stfo.adriver.substeps.runner.notification.reporter;

import com.technophobia.substeps.execution.node.IExecutionNode;
import org.apache.commons.io.FileUtils;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.FileReporter;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.NodeToDirectoryTransformer;

import java.io.File;
import java.io.IOException;

public class OutputToFileSystemStepReporter implements StepReporter {

    private String parentFilename;
    private final NodeToDirectoryTransformer nodeToDirectoryTransformer;
    private final FileReporter[] fileReporters;

    public OutputToFileSystemStepReporter(String parentFilename, final NodeToDirectoryTransformer nodeToDirectoryTransformer, FileReporter... fileReporters) {
        this.parentFilename = parentFilename;
        this.nodeToDirectoryTransformer = nodeToDirectoryTransformer;
        this.fileReporters = fileReporters;
    }

    @Override
    public void report(IExecutionNode node, IExecutionNode... ancestry) {
        File parent = getOrCreateParentFile();
        for (IExecutionNode ancestor : ancestry){
            parent = nodeToDirectoryTransformer.directoryFor(ancestor, parent);
        }

        File nodeDirectory = nodeToDirectoryTransformer.directoryFor(node, parent);

        for (FileReporter reporter : fileReporters){
            reporter.reportTo(node, nodeDirectory);
        }
    }

    private File getOrCreateParentFile() {
        final File folder = new File(parentFilename);
        if (!folder.exists()) {
            try {
                FileUtils.forceMkdir(folder);
                return folder;
            } catch (IOException ex) {
                throw new IllegalStateException("Could not create the parent folder "+parentFilename, ex);
            }
        }
        return folder;
    }
}