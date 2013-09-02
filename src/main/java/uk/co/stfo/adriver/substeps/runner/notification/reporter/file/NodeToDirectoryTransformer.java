package uk.co.stfo.adriver.substeps.runner.notification.reporter.file;

import com.technophobia.substeps.execution.node.IExecutionNode;

import java.io.File;

public interface NodeToDirectoryTransformer {
    File directoryFor(IExecutionNode node, File parentDirectory);
}
