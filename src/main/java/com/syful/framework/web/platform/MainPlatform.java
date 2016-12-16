package com.syful.framework.web.platform;


import com.syful.framework.adapters.GridManager;
import com.syful.framework.web.config.Settings;
import com.syful.framework.web.platform.pages.LoginPage;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

public class MainPlatform {

    static Settings settings = new Settings();

    private static ThreadLocal<String> sessionVariable = new ThreadLocal<>();

    public static String getSessionVariable() {
        return sessionVariable.get();
    }

    public static void setSessionVariable(String variable) {
        sessionVariable.set(variable);
    }

    public static LoginPage loginAs(){
        String environment = settings.getStartPointUrl();
        Reporter.log("Open 'Login' page: " + environment, true);
        GridManager.getDriver().navigate().to(environment);
        GridManager.getDriver().manage().window().maximize();

        return LoginPage.initialize(GridManager.getDriver());
    }


}
