package com.syful.framework.adapters;


import org.openqa.selenium.remote.RemoteWebDriver;

public class GridManager {

    private static ThreadLocal<RemoteWebDriver> webDriver = new ThreadLocal<>();

    public static RemoteWebDriver getDriver() {
        return webDriver.get();
    }

    static void setWebDriver(RemoteWebDriver driver) {
        webDriver.set(driver);
    }
}
