package uk.co.stfo.adriver.substeps.driver;

import org.openqa.selenium.WebDriver;

/**
 * Created with IntelliJ IDEA.
 * User: dmoss
 * Date: 14/03/13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public interface DriverType {
    boolean isVisual();
    
    WebDriver createWebDriverFrom();
}
