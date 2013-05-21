package uk.co.stfo.adriver.substeps;

import org.junit.runner.RunWith;

import uk.co.stfo.adriver.substeps.step.BaseADriverStepImplementations;

import com.technophobia.substeps.runner.JunitFeatureRunner;
import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;

@SubStepsConfiguration(featureFile = "./target/test-classes/features", subStepsFile = "./target/test-classes/substeps", stepImplementations = { BaseADriverStepImplementations.class })
@RunWith(JunitFeatureRunner.class)
public class SelfTests {
    // no op
}
