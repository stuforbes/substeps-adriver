package uk.co.stfo.adriver.substeps.runner.notification.reporter.file;

import com.google.common.base.Supplier;
import com.technophobia.substeps.execution.node.IExecutionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.stfo.adriver.substeps.runner.TestRun;

import java.io.*;

public class TakeScreenshotFileReporter implements FileReporter{

    private static final Logger LOG = LoggerFactory.getLogger(TakeScreenshotFileReporter.class);

    private static final String FILENAME = "image.png";

    private Supplier<TestRun> currentTestRun;

    public TakeScreenshotFileReporter(Supplier<TestRun> currentTestRun){
        this.currentTestRun = currentTestRun;
    }

    @Override
    public void reportTo(IExecutionNode node, File directory) {
        
        File file = createFile(directory);
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            currentTestRun.get().webDriver().dump().screenshotTo(outputStream);
        } catch (FileNotFoundException ex) {
            LOG.error("Could not take screenshot for node "+node, ex);
        } catch (IOException ex) {
            LOG.error("Could not take screenshot for node "+node, ex);
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
