package uk.co.stfo.adriver.substeps.runner.notification.reporter.file;

import com.technophobia.substeps.execution.node.IExecutionNode;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;

public class NodeLineToDirectoryTransformer implements NodeToDirectoryTransformer {

    private static final int MAX_LENGTH = 100;


    @Override
    public File directoryFor(IExecutionNode node, File parentDirectory) {
        File existingDirectory = existingDirectoryOrNull(node, parentDirectory);
        if (existingDirectory != null) {
            return existingDirectory;
        }
        return createDirectory(node, parentDirectory);
    }


    public File existingDirectoryOrNull(IExecutionNode node, File parentDirectory) {
        File[] files = parentDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for (File file : files) {
                String[] split = file.getName().split("_");

                if (split.length > 2) {
                    if (Long.valueOf(split[1]).longValue() == node.getId()) {
                        return file;
                    }
                }
            }
        }
        return null;
    }


    private File createDirectory(IExecutionNode node, File parentDirectory) {
        File[] files = parentDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        int num = files != null ? files.length + 1 : 1;
        String filename = num + "_" + node.getId() + "_" + sanitise(node.getDescription());

        File directory = new File(parentDirectory, filename);
        directory.mkdir();
        return directory;
    }


    private String sanitise(String name) {
        String sanitised = StringUtils.isNotBlank(name) ? name.replaceAll("[/\\ ]", "_") : "<UNKNOWN>";
        
        return sanitised.length() < MAX_LENGTH ? sanitised : (sanitised.substring(0, 100) + "...");
    }
}
