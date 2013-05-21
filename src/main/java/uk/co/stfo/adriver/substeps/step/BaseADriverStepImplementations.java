package uk.co.stfo.adriver.substeps.step;

import uk.co.stfo.adriver.substeps.runner.DefaultExecutionSetupTearDown;

import com.technophobia.substeps.model.SubSteps.AdditionalStepImplementations;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
@AdditionalStepImplementations({ ActionADriverStepImplementations.class, AssertionADriverStepImplementations.class,
        FinderADriverStepImplementations.class, FormADriverStepImplementations.class })
public class BaseADriverStepImplementations {
}
