package com.syful.framework.web.platform.pages;


import com.syful.framework.adapters.GridManager;
import com.syful.framework.web.platform.decorator.MainElementFactory;
import com.syful.framework.web.platform.html.base.Element;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.concurrent.TimeUnit;

public class LoginPage {

    @FindBy(css = "input[name='uid']")
    protected Element userIdField;

    @FindBy(css = "input[name='password']")
    protected Element userPasswordField;

    @FindBy(css = "input[name='btnLogin']")
    protected Element loginButton;

    @FindBy(css = "input[name='btnReset']")
    protected Element resetButton;

    public LoginPage invalidLogin(String userName, String password){
        Reporter.log(String.format("Invalid login: with '%s' as userId and '%s' as password",
                userName, password), true);
        typeUserName(userName);
        typePassword(password);
        clickLoginButton();
        return this;
    }

    private LoginPage typeUserName(String userName){
        Reporter.log(String.format("Type username '%s' as userId", userName), true);
        userIdField
                .sendKeys(userName);
        return this;
    }
    private LoginPage typePassword(String password){
        Reporter.log(String.format("Type password '%s' as userId", password), true);
        userPasswordField
                .sendKeys(password);
        return this;
    }
    private LoginPage clickLoginButton(){
        Reporter.log("Click login button.", true);
        loginButton
                .hitButton();
        return this;
    }

    public LoginPage verifyAlertMessage(String message){
        Reporter.log(String.format("Verify alert message '%s'", message), true);

        WebDriverWait wait = new WebDriverWait(GridManager.getDriver(), 5);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = GridManager.getDriver().switchTo().alert();
        String actualMessage = alert.getText();
        Assert.assertEquals(actualMessage, message, "Message did not match.");
        alert.accept();


        return this;
    }
}
