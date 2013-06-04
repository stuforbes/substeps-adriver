package uk.co.stfo.adriver.substeps.configuration;

public enum CloseWebDriverStrategy {

    NEVER {
        @Override
        public boolean shouldClose(final boolean testSuccessful) {
            return false;
        }
    }, //
    ON_TEST_SUCCESS_ONLY {
        @Override
        public boolean shouldClose(final boolean testSuccessful) {
            return testSuccessful;
        }
    }, //
    ALWAYS {
        @Override
        public boolean shouldClose(final boolean testSuccessful) {
            return true;
        }
    };

    public abstract boolean shouldClose(boolean testSuccessful);
}
