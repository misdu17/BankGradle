package com.syful.framework.adapters;


import com.syful.framework.annotations.WebTest;
import com.syful.framework.web.config.Settings;
import com.syful.framework.web.platform.utilities.ReportUtils;
import com.testrail.rest.TestRailApi;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.uncommons.reportng.HTMLReporter;

import java.io.File;
import java.io.IOException;
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
    TestRailApi testRailApi = new TestRailApi();

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
        testRailApi.updateTestStatusInTestPlan(result);
        //BookerPlatform.cleanLocation();
        if(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(WebTest.class) != null){
            GridManager.getDriver().close();
            GridManager.getDriver().quit();
        }

    }

    @Override
    public void onTestFailure(ITestResult result) {
        testRailApi.updateTestStatusInTestPlan(result);
        //BookerPlatform.cleanLocation();
        if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(WebTest.class) != null) {
            reportLogScreenshot(String.format("%s.%s", result.getInstanceName(), result.getName()));
            GridManager.getDriver().close();
            GridManager.getDriver().quit();
        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testRailApi.updateTestStatusInTestPlan(result);
        //BookerPlatform.cleanLocation();

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
        //BookerPlatform.cleanLocation();
        // List of test results which we will delete later
        List<ITestResult> testsToBeRemoved = new ArrayList<>();

        //collect all id's form passed test

        Set <Integer> passedTestIds = context.getPassedTests()
                .getAllResults().stream().map(this::getId).collect(Collectors.toSet());

        Set<Integer> failedTestIds = new HashSet<>();
        context.getFailedTests().getAllResults().forEach( res -> {
            int failedTestId = getId(res);
            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)){
                testsToBeRemoved.add(res);
            }else {
                failedTestIds.add(failedTestId);
            }
        });

        // finally delete all tests that re marked
        for (Iterator<ITestResult> iterator = context.getFailedTests().getAllResults().iterator(); iterator.hasNext();){
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)){
                iterator.remove();
            }
        }
        System.out.println(String.format("** Passed: %s; Failed: %s; Skipped: %s **"
                , context.getPassedTests().size(), context.getFailedTests().size(), context.getSkippedTests().size()));
        DefaultGroovyMethods.println(String.format("** Passed: %s; Failed: %s; Skipped: %s **"
                , context.getPassedTests().size(), context.getFailedTests().size(), context.getSkippedTests().size()));

    }

    private String getCurrentTime() {
        DateTime currentTime = new DateTime();
        return currentTime.toString("E MMM, d hh:mm:ss", Locale.US);
    }


    protected void reportLogScreenshot(String screenName) {
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        createScreenshot(screenName);
        String destFile = String.format("%s.png", screenName);
        String logImage = "<a id=\"screenName\" class=\"picture\" href=screenshots/" + destFile + "><img style=\"width:300px\" src=screenshots/" + destFile + "></a></br>";
        Reporter.log(logImage);
        Reporter.setCurrentTestResult(null);
    }

    protected File createScreenshot(String screenName) {
        try {
            File srcFile = GridManager.getDriver().getScreenshotAs(OutputType.FILE);
            String destDir = "build/reports/tests/html/screenshots";
            boolean isFolderExists = (new File(destDir).exists());

            if (!isFolderExists) {
                boolean isFolder = (new File(destDir).mkdirs());
                Assert.assertTrue(isFolder, "Folder was not created");
            }
            String destFile = String.format("%s/%s.png", destDir, screenName);
            File screen = new File(destFile);
            FileUtils.copyFile(srcFile, screen);
            return screen;

        } catch (SessionNotCreatedException e) {
            e.printStackTrace();
            Assert.fail("WebDriver session is lost", e.getCause());
        } catch (IOException | WebDriverException e) {
            e.printStackTrace();
            System.out.println("Screenshot was not created. Due to an error: " + e.getMessage());
        }
        return null;
    }

    private int getId(ITestResult result){
        int id = result.getTestClass().getName().hashCode();
        id = 31 * id + result.getMethod().getMethodName().hashCode();
        id = 31 * id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

}
