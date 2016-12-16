package com.syful.framework.web.platform.pages;


import com.syful.framework.web.platform.decorator.MainElementFactory;
import com.syful.framework.web.platform.html.base.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

    @FindBy(css = "input[name='uid']")
    protected Element userIdField;

    @FindBy(css = "input[name='password']")
    protected Element userPasswordField;

    @FindBy(css = "input[name='btnLogin']")
    protected Element loginButton;

    @FindBy(css = "input[name='btnReset']")
    protected Element resetButton;

    private static WebDriver bDriver;

    public static LoginPage initialize(WebDriver driver) {
        bDriver = driver;
        return MainElementFactory.initElements(driver, LoginPage.class);
    }
}
