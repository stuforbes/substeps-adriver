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

package uk.co.stuforbes.windowlicker.substeps.driver;

import org.openqa.selenium.WebDriver;

import uk.co.stuforbes.windowlicker.substeps.configuration.WebdriverSubstepsConfiguration;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;

public class DriverTypeWebDriverFactory implements WebDriverFactory {

    public AsyncWebDriver createWebDriver() {

        final WebDriver webDriver = WebdriverSubstepsConfiguration.driverType().createWebDriverFrom();
        
        return new ActionableAsyncWebDriver(new UnsynchronizedProber(), webDriver);
    }

    public DriverType driverType() {
        return WebdriverSubstepsConfiguration.driverType();
    }

}
