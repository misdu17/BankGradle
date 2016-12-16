package testcases.web;


import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SampleTest {

    WebDriver driver;
    String baseUrl, nodeUrl;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        baseUrl = "http://demo.guru99.com/V4/";
        nodeUrl = "http://localhost:4444/wd/hub";
        //System.setProperty("webdriver.gecko.driver", "/Users/Zakia/IntelliJProjects/BankGradle/geckodriver");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setBrowserName("firefox");
        driver = new RemoteWebDriver(new URL(nodeUrl), capabilities);
    }

    @Test
    public void simpleTest() {
        driver.get(baseUrl);
        driver.manage().window().maximize();
    }
}
