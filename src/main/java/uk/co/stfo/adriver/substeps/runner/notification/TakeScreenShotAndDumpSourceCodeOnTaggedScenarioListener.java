package uk.co.stfo.adriver.substeps.runner.notification;

import com.google.common.base.Supplier;
import uk.co.stfo.adriver.substeps.configuration.ADriverConfiguration;
import uk.co.stfo.adriver.substeps.runner.ExecutionState;
import uk.co.stfo.adriver.substeps.runner.TestRun;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.OutputToFileSystemStepReporter;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.StepReporter;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.DumpPageSourceFileReporter;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.NodeLineToDirectoryTransformer;
import uk.co.stfo.adriver.substeps.runner.notification.reporter.file.TakeScreenshotFileReporter;

public class TakeScreenShotAndDumpSourceCodeOnTaggedScenarioListener extends ReportStepStatusForTaggedScenarioListener{

    public TakeScreenShotAndDumpSourceCodeOnTaggedScenarioListener(){
        this(ExecutionState.configuration(), ExecutionState.currentTestRun());
    }

    public TakeScreenShotAndDumpSourceCodeOnTaggedScenarioListener(Supplier<ADriverConfiguration> configuration, Supplier<TestRun> currentTestRun) {
        super(stepReporter(configuration, currentTestRun), configuration.get().getProfilingTags());
    }

    private static StepReporter stepReporter(Supplier<ADriverConfiguration> configuration, Supplier<TestRun> currentTestRun){
        return new OutputToFileSystemStepReporter(configuration.get().getProfilingOutputFolder(), new NodeLineToDirectoryTransformer(), new DumpPageSourceFileReporter(currentTestRun), new TakeScreenshotFileReporter(currentTestRun));
    }
}
