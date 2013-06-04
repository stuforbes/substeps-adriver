package uk.co.stfo.adriver.substeps.driver;

public enum DriverType {

    FIREFOX(true), //
    PHANTOMJS(false), //
    HTMLUNIT(false), //
    CHROME(true), //
    IE(true);

    private final boolean visual;


    private DriverType(final boolean visual) {
        this.visual = visual;
    }


    public boolean isVisual() {
        return visual;
    }
}
