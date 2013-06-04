package uk.co.stfo.adriver.substeps.step;

import uk.co.stfo.adriver.substeps.runner.DriverInitialisation;

import com.technophobia.substeps.model.SubSteps.AdditionalStepImplementations;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = DriverInitialisation.class)
@AdditionalStepImplementations({ ActionADriverStepImplementations.class, AssertionADriverStepImplementations.class,
        FinderADriverStepImplementations.class, FormADriverStepImplementations.class })
public class AllADriverStepImplementations {
}
