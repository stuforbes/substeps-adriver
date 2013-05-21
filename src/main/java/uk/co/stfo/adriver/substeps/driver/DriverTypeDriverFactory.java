/*
 * Copyright Technophobia Ltd 2013
 *
 *     This file is part of Substeps.
 *
 *     Substeps is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version
 *
 *     Substeps is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.stfo.adriver.substeps.driver;

import org.openqa.selenium.WebDriver;

import uk.co.stfo.adriver.driver.AsyncDriver;
import uk.co.stfo.adriver.driver.Driver;
import uk.co.stfo.adriver.substeps.configuration.WebdriverSubstepsConfiguration;

public class DriverTypeDriverFactory implements DriverFactory {

    public Driver createDriver() {

        final WebDriver webDriver = WebdriverSubstepsConfiguration.driverType().createWebDriverFrom();

        return AsyncDriver.createAsynDriver(webDriver, 5000, 100);
    }


    public DriverType driverType() {
        return WebdriverSubstepsConfiguration.driverType();
    }

}
