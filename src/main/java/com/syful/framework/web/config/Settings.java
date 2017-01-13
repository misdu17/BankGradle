package com.syful.framework.web.config;


import com.syful.framework.web.browser.BrowserType;
import com.syful.framework.web.exception.UnknownBrowserException;
import com.syful.framework.web.exception.UnknownPropertyException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class Settings {
    private static final String SELENIUM_BASEURL = "selenium.baseUrl";
    private static final String SELENIUM_BROWSER = "selenium.browser";

    private static final String TEST_PLAN = "testrail.test.plan";
    private static final String GROUPS = "groups";
    private static final String RETRY_COUNT = "retryCount";

    private static final String SQL_AUTOMATION_SERVER_CONNECTION = "sql.automation.server.connection";
    private static final String USER_NAME = "sql.user.name";
    private static final String USER_PASSWORD = "sql.user.pwd";
    //read property file field declaration
    private static final String SELENIUM_PROPERTIES = "src/test/resources/syful.properties";
    private static final String GUI_ELEMENTS_LOCATION = "gui.elements.location";
    private static String stagingUrl;
    private BrowserType browser;
    private static String sqlAutomationServerConnection;
    private String userName;
    private String userPwd;
    private static String testPlan;
    private String groups;
    private static String retryCount;

    private String getGuiElementsPath;
    private Properties properties = new Properties();

    public Settings() {
        loadSettings();
    }

    public static String getStagingUrl(){
        return stagingUrl;
    }

    private void loadSettings() {
        properties = loadPropertiesFile();
        getGuiElementsPath = getPropertyOrNull(GUI_ELEMENTS_LOCATION);
        stagingUrl = getPropertyOrNull(SELENIUM_BASEURL);
        sqlAutomationServerConnection = getPropertyOrNull(SQL_AUTOMATION_SERVER_CONNECTION);
        userName = getPropertyOrNull(USER_NAME);
        userPwd = getPropertyOrNull(USER_PASSWORD);
        browser = BrowserType.Browser(getPropertyOrNull(SELENIUM_BROWSER));
        testPlan = getPropertyOrNull(TEST_PLAN);
        groups = getPropertyOrNull(GROUPS);
        retryCount = getPropertyOrNull(RETRY_COUNT);
    }

    private Properties loadPropertiesFile(){
        try {
            // get specified property file
            String fileName = getPropertyOrNull(SELENIUM_PROPERTIES);
            if (fileName == null) {
                fileName = SELENIUM_PROPERTIES;
            }
            // try to load from classpath
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            // no file in classpath, look on disk
            if (stream == null) {
                stream = new FileInputStream(new File(fileName));
            }
            Properties result = new Properties();
            result.load(stream);
            return result;
        } catch (IOException e){
            throw new UnknownPropertyException("Property file is not found");
        }
    }

    public String getPropertyOrNull(String name){
        return getProperty(name, false);
    }

    public String getPropertyOrThrowException(String name) {
        return getProperty(name, true);
    }

    private String getProperty(String name, boolean forceExceptionIfNotDefined){
        String result;
        if ((result = System.getProperty(name)) != null && result.length() > 0) {
            return result;
        } else if ((result = getPropertyFromPropertiesFile(name)) != null && result.length() > 0) {
            return result;
        } else if (forceExceptionIfNotDefined) {
            throw new UnknownPropertyException("Unknown property: [" + name + "]");
        }
        return result;
    }

    private String getPropertyFromPropertiesFile(String name){
        Object result = properties.get(name);
        if (result == null) {
            return null;
        } else {
            return result.toString();
        }
    }

    public String getGuiElementsPath() {
        return getGuiElementsPath;
    }

    public RemoteWebDriver getDriver(String driver){
        BrowserType browserType = BrowserType.Browser(driver);
        return getDriver(browserType);
    }

    public RemoteWebDriver getDriver(){
        return getDriver(browser);
    }

    private RemoteWebDriver getDriver(BrowserType browserType){
        try {

            switch (browserType){
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver", "/Users/Zakia/IntelliJProjects/BankGradle/geckodriver");
                    URL remoteUrl = new URL("http://localhost:4444/wd/hub");
                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability("marionette", true);
                    capabilities.setBrowserName("firefox");
                    capabilities.setPlatform(Platform.MAC);
                    capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
                    return new RemoteWebDriver(remoteUrl, capabilities);
                case IE:
                    return new InternetExplorerDriver();
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", "/Users/Zakia/IntelliJProjects/BankGradle/chromedriver");
                    remoteUrl = new URL("http://localhost:4444/wd/hub");
                    capabilities = DesiredCapabilities.chrome();
                    capabilities.setBrowserName("chrome");
                    capabilities.setPlatform(Platform.MAC);
                    return new RemoteWebDriver(remoteUrl, capabilities);
                default:
                    throw new UnknownBrowserException("Cannot create driver for unknown browser type.");
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getStartPointUrl() {
        return String.format("http://%s", stagingUrl);
    }

    public static String getTestPlan() {
        return testPlan;
    }

    public String getGroups() {
        return groups;
    }

    public String getRevision() {
        return String.format("https://%s", stagingUrl);
    }

    public static String getSqlAutomationServerConnection() {
        return sqlAutomationServerConnection;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public BrowserType getBrowser() {
        return browser;
    }

    public static int getRetryCount(){
        return Integer.parseInt(retryCount);
    }
}
