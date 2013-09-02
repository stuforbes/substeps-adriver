package uk.co.stfo.adriver.substeps.runner.notification.reporter.file;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import java.io.*;

public class DumpPageSourceFileReporter implements FileReporter{

    private static final String FILENAME = "source.html";

    private static final Logger LOG = LoggerFactory.getLogger(DumpPageSourceFileReporter.class);

    private Supplier<TestRun> currentTestRun;

    public DumpPageSourceFileReporter(Supplier<TestRun> currentTestRun){
        this.currentTestRun = currentTestRun;
    }

    @Override
    public void reportTo(IExecutionNode node, File directory) {
        
        File file = createFile(directory);
        
        
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            currentTestRun.get().webDriver().dump().pageSourceTo(outputStream);
        } catch (FileNotFoundException ex) {
            LOG.error("Could not write page source to node "+node, ex);
        } catch (IOException ex) {
            LOG.error("Could not write page source to node " + node, ex);
        } finally{
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ex) {
                LOG.error("Could not close stream for node "+node, ex);
            }
        }
    }

    private File createFile(File directory) {
        File file = new File(directory, FILENAME);
        try {
            file.createNewFile();
            return file;
        } catch (IOException ex) {
            LOG.error("Could not create file "+FILENAME+" under "+directory.getAbsolutePath(), ex);
        }
        return null;
    }
}
