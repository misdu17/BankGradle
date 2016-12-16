package com.syful.framework.adapters;


import com.syful.framework.annotations.WebTest;
import com.syful.framework.web.config.Settings;
import com.syful.framework.web.platform.utilities.ReportUtils;
import org.apache.velocity.VelocityContext;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.joda.time.DateTime;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.uncommons.reportng.HTMLReporter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MainTestSessionListener extends HTMLReporter implements ITestListener{

    private static final String UTILS_KEY = "utils";
    private static final String ENVIRONMENT = "environment";
    private static final String BROWSER = "browser";
    private static final String TESTPLAN = "testPlan";
    private static final String GROUPS = "groups";
    private static final ReportUtils REPORT_UTILS = new ReportUtils();
    protected Settings settings = new Settings();

    protected VelocityContext createContext() {
        final VelocityContext context = super.createContext();
        context.put(UTILS_KEY, REPORT_UTILS);
        context.put(ENVIRONMENT, Settings.getStagingUrl());
        context.put(BROWSER, settings.getBrowser());
        context.put(TESTPLAN, Settings.getTestPlan());
        context.put(GROUPS, settings.getGroups());
        return context;
    }

    @Override
    public void onTestStart(ITestResult result){
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        if(method.getAnnotation(WebTest.class) != null){
            Reporter.log(String.format("%s - %s %s started", getCurrentTime()
                    , method.getAnnotation(WebTest.class).id(), result.getName()), true);
            /*

            if(BookerPlatform.getLocation() == null){
                System.out.println(String.format("Configuring location for test %s with account name: %s."
                        , result.getName(), method.getAnnotation(WebTest.class).locationAccountName()));
                DefaultGroovyMethods.println(String.format("Configuring location for test %s with account name: %s."
                        , result.getName(), method.getAnnotation(WebTest.class).locationAccountName()));

                SqlAdapter
                        .newBuilder()
                        .withConnection(Settings.getSqlAutomationServerConnection())
                        .getBookerPlatform(Settings.getStagingUrl(), result.getMethod().getConstructorOrMethod().getMethod());
            }
            */

            //setPaymentProcessor(result.getMethod().getConstructorOrMethod().getMethod());
            //MerchantApi.getLocationSchedule(BookerPlatform.getLocation(), BookerPlatform.getLocation().getAccessToken());
            RemoteWebDriver driver = settings.getDriver();
            GridManager.setWebDriver(driver);
        }


    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestFailure(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println(String.format("Test suite is %s started", context.getName()));
        DefaultGroovyMethods.println(String.format("Test suite is %s started", context.getName()));
    }

    @Override
    public void onFinish(ITestContext context) {

    }

    private String getCurrentTime() {
        DateTime currentTime = new DateTime();
        return currentTime.toString("E MMM, d hh:mm:ss", Locale.US);
    }

}
