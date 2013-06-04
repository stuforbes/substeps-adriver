package uk.co.stfo.adriver.substeps;

import org.junit.runner.RunWith;

import uk.co.stfo.adriver.substeps.step.AllADriverStepImplementations;

import com.technophobia.substeps.runner.JunitFeatureRunner;
import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;

@SubStepsConfiguration(featureFile = "./src/test/resources/features", subStepsFile = "./src/test/resources/substeps", stepImplementations = { AllADriverStepImplementations.class })
@RunWith(JunitFeatureRunner.class)
public class StepTest {
    // no op
}
