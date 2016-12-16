package com.syful.framework.web.platform.html.support;


import com.google.common.base.Optional;
import com.syful.framework.adapters.GridManager;
import com.syful.framework.web.platform.html.base.Element;
import com.syful.framework.web.platform.html.base.ListOfElements;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.*;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class ElementImpl implements Element, ListOfElements{

    private final WebElement element;
    private WebElement resolvedELement;
    private ElementLocator locator;
    private Optional<String> expectedErrorMessage;

    /**
     * Creates a Element for a given WebElement.
     *
     * @param element element to wrap up
     */
    public ElementImpl(final WebElement element, ElementLocator locator) {
        this.element = element;
        this.locator = locator;
    }

//    public static <T extends Element> T wrapWebElement(WebElement element) {
//        return (T) new ElementImpl(element, this.locator);
//    }

    protected WebElement getElement() {
        int counter = 0;
        while (counter < 5) {
            try {
                WebElement result;
                if (this.locator == null) {
                    result = null;
                } else {
                    this.resolvedELement = this.getLocator().findElement();
                    if (this.resolvedELement == null) {
                        throw new ElementNotVisibleException(this.locator.toString());
                    }

                    result = this.resolvedELement;
                }

                return result;
            } catch (StaleElementReferenceException var3) {
                counter++;

            } catch (Throwable var3) {
                counter++;
            }
        }
        throw new ElementNotVisibleException(this.locator.toString());
    }

    public List<WebElement> getElements() {
        List<WebElement> foundElements = null;
        try {
            foundElements = locator.findElements();
//            } else {
//                foundElements = parent.locateChildElements(getLocator());
//            }
        } catch (NoSuchElementException n) {
            //addInfoForNoSuchElementException(n);
        }

        return foundElements;
    }

    public WebElement getElement(int index) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .get(index);
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | java.util.NoSuchElementException e) {
                counter++;
            }
        }
        return null;
    }

    public ElementLocator getLocator() {
        return this.locator;
    }

    /**
     * Checks if element is present in the html dom. An element that is present in the html dom does not mean it is
     * visible. To check if element is visible, use element to get {@link WebElement} and then invoke
     * {@link WebElement#isDisplayed()}.
     *
     * @return True if element is present, false otherwise.
     */
    public boolean isElementPresent() {
        boolean returnValue = false;
        try {
            if (getElement() != null) {
                returnValue = true;
            }
        } catch (NoSuchElementException e) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Is this element displayed or not? This method avoids the problem of having to parse an element's "style"
     * attribute.
     *
     * @return Whether or not the element is displayed
     */
    public boolean isVisible() {
        try {
            return getElement() != null && getElement().isDisplayed();
        } catch (ElementNotVisibleException var1) {
            return false;
        } catch (NoSuchElementException var2) {
            return false;
        } catch (StaleElementReferenceException var3) {
            return false;
        }
    }

    public boolean isCurrentlyVisible() {
        return this.isVisible();
    }

    public boolean isCurrentlyEnabled() {
        return this.isEnabled();
    }

    public boolean isPresent() {
        if (this.driverIsDisabled()) {
            return false;
        } else {
            try {
                return this.getElement() != null && (this.getElement().isDisplayed());
            } catch (NoSuchElementException var2) {
                return var2.getCause().getMessage().contains("Element is not usable");
            }catch (ElementNotVisibleException var2){
                return false;
            }
        }
    }

    public boolean isAllEnabled() {
        try {
            return getElements().stream().anyMatch(WebElement::isDisplayed);
        } catch (ElementNotVisibleException var1) {
            return false;
        } catch (NoSuchElementException var2) {
            return false;
        } catch (StaleElementReferenceException var3) {
            return false;
        }
    }

    public Element shouldBeVisible() {
        if (!this.isVisible()) {
            this.failWithMessage("Element should be visible");
        }
        return this;
    }

    public Element shouldNotBeVisible() {
        if (this.isCurrentlyVisible()) {
            this.failWithMessage("Element should not be visible");
        }
        return this;
    }

    public Element shouldBeEnabled() {
        if (!this.isEnabled()) {
            String errorMessage = String.format("Field \'%s\' should be enabled", new Object[]{this.toString()});
            this.failWithMessage(errorMessage);
        }
        return this;
    }

    public Element shouldBeDisabled() {
        if (this.isEnabled()) {
            String errorMessage = String.format("Field \'%s\' should be disabled", new Object[]{this.toString()});
            this.failWithMessage(errorMessage);
        }
        return this;
    }

    public Element shouldBePresent() {
        if (!this.isPresent()) {
            this.failWithMessage("Field should be present");
        }
        return this;
    }

    public Element shouldNotBePresent() {
        if (this.isPresent()) {
            this.failWithMessage("Field should not be present");
        }
        return this;
    }

    public Element waitUntilVisible() {
        if (this.driverIsDisabled()) {
            return this;
        } else {
            int counter = 0;
            while (counter < 10) {
                try {
                    this.waitForCondition().until(this.elementIsDisplayed());
                    return this;
                } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
                    counter++;
                }
            }
            waitForJQueryComplete();
            return this;
        }
    }

    public Element waitUntilVisible(long timeout) {
        if (this.driverIsDisabled()) {
            return this;
        } else {
            int counter = 0;
            while (counter < 10) {
                try {
                    this.waitForCondition(timeout).until(this.elementIsDisplayed());
                    return this;
                } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
                    counter++;
                }
            }
            waitForJQueryComplete();
            return this;
        }
    }

    public Element waitUntilNotVisible() {
        if (this.driverIsDisabled()) {
            return this;
        } else {
            int counter = 0;
            while (counter < 10) {
                try {
                    this.getElement().isDisplayed();
                    counter++;
                } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | ElementNotVisibleException e) {
                    return this;
                }
            }
            waitForJQueryComplete();
            return this;
        }
    }

    public Element waitUntilEnabled() {
        if (this.driverIsDisabled()) {
            return this;
        } else {
            int counter = 0;
            while (counter < 10) {
                try {
                    this.waitForCondition().until(this.elementIsEnabled());
                    return this;
                } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
                    counter++;
                    System.out.print(String.format("Trying to recover after Exception %s", e.getMessage()));
                }
            }
            waitForJQueryComplete();

            return this;
        }
    }

    public ListOfElements waitElementsReady() {
        if (this.driverIsDisabled()) {
            return this;
        } else {
            try {
                this.waitForCondition().until(this.allElementsEnabled());
            } catch (Throwable var2) {
                this.throwErrorWithCauseIfPresent(var2, var2.getMessage());
            }
            waitForJQueryComplete();

            return this;
        }
    }

    public Wait<WebDriver> waitForCondition() {
        return (new FluentWait(GridManager.getDriver()))
                .withTimeout(30000, TimeUnit.MILLISECONDS)
                .pollingEvery(250, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public Wait<WebDriver> waitForCondition(long timeout) {
        return (new FluentWait(GridManager.getDriver()))
                .withTimeout(timeout, TimeUnit.MILLISECONDS)
                .pollingEvery(250, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public WebElement getFirstElementFromList() {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .findFirst()
                        .get();
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | java.util.NoSuchElementException e) {
                counter++;
            }
        }
        return null;
    }

    public WebElement getFirstRelatedElementFromList(String filter) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream().filter(tab -> tab.getText()
                                .contains(filter))
                        .findFirst().get();
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | java.util.NoSuchElementException e) {
                counter++;
            }
        }
        return null;
    }

    public WebElement getFirstRelatedElementFromList(String filter1, String filter2) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream().filter(tab ->
                                tab.getText().contains(filter1)
                                        && tab.getText().contains(filter2))
                        .findFirst().get();
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | java.util.NoSuchElementException e) {
                counter++;
            }
        }
        return null;
    }

    public boolean isElementPresentInList(String filter) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .anyMatch(row -> row.getText().contains(filter));
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
            } catch (org.openqa.selenium.NoSuchElementException e) {
                return false;
            }
        }
        return false;
    }

    public boolean isElementPresentInList(String filter1, String filter2) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .anyMatch(row -> row.getText().contains(filter1)
                                && row.getText().contains(filter2));
            } catch (StaleElementReferenceException | java.util.NoSuchElementException e) {
                counter++;
            } catch (org.openqa.selenium.NoSuchElementException e) {
                return false;
            }
        }
        return false;
    }

    public void click() {
        waitForJQueryComplete();
        waitForJavascriptComplete();
        getElement()
                .click();
        waitForJQueryComplete();
        waitForJavascriptComplete();
    }

    public void hitButton() {
        waitForJQueryComplete();
        waitForJavascriptComplete();
        getElement().click();
    }

    /*
    * Available only if element has ID
    * */
    public Element jClick() {
        String id = getElement().getAttribute("id");
        if (!id.equals("")) {
            JavascriptExecutor jse = (JavascriptExecutor) GridManager.getDriver();
            String command = String.format("$('#%s').click();", id);
            jse.executeScript(command);
        }
        return this;
    }

    public Element selectByIndex(Integer index) {
        int counter = 0;
        while (counter < 10) {
            try {
                waitForJQueryComplete();
                waitForJavascriptComplete();
                waitUntilEnabled();

                new Select(getElement()).selectByIndex(index);
                waitForJQueryComplete();
                return this;
            } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
                counter++;
            }
        }

        return this;
    }

    public String selectFirstElementInDropdown() {
        int counter = 0;
        while (counter < 10) {
            try {
                waitForJQueryComplete();
                waitForJavascriptComplete();
                waitUntilEnabled();

                Select dropdown = new Select(getElement());
                String textOption = dropdown
                        .getOptions()
                        .parallelStream()
                        .filter(el -> !el.getText().equals(""))
                        .findFirst()
                        .get().getText();

                dropdown.selectByVisibleText(textOption);
                waitForJQueryComplete();
                return textOption;
            } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
                counter++;
            }
        }
        return "";
    }

    public Element selectByText(String text) {
        waitForJQueryComplete();
        waitForJavascriptComplete();

        Select dropdown = new Select(getElement());
        String textOption = dropdown
                .getOptions()
                .parallelStream().filter(opt -> opt.getText()
                        .contains(text))
                .findFirst()
                .get().getText();

        dropdown
                .selectByVisibleText(textOption);
        waitForJQueryComplete();
        waitForJavascriptComplete();

        return this;
    }

    public Element selectByExactText(String text) {
        waitForJQueryComplete();
        waitForJavascriptComplete();

        Select dropdown = new Select(getElement());
        String textOption = dropdown
                .getOptions()
                .parallelStream().filter(opt -> opt.getText()
                        .equalsIgnoreCase(text))
                .findFirst()
                .get().getText();

        dropdown
                .selectByVisibleText(textOption);
        waitForJQueryComplete();
        waitForJavascriptComplete();

        return this;
    }

    public String getSelectedText() {
        return new Select(getElement())
                .getFirstSelectedOption()
                .getText();

    }



    public Element uncheck() {
        if(getElement().isSelected())
            getElement().click();

        return this;
    }

    public Element check() {
        waitForJQueryComplete();
        waitForJavascriptComplete();
        if(!getElement().isSelected())
            getElement().click();

        return this;
    }

    public WebElement getChildElement(String filter, By locator) {
        int counter = 0;

        while (counter < 10) {
            try {
                return getElements()
                        .stream()
                        .filter(tab ->
                                tab.getText().contains(filter))
                        .findFirst().get()
                        .findElement(locator);
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException | java.util.NoSuchElementException e) {
                counter++;
            }
        }
        return null;
    }

    public void selectCheckbox(boolean flag) {
        if (getElement().isSelected() != flag) {
            getElement().click();
            waitForJavascriptComplete();
        }
    }

    public Element switchToFrame() {
        GridManager.getDriver().switchTo().frame(getElement());

        return this;
    }

    public void sendKeys(CharSequence... keysToSend) {
        getElement().sendKeys(keysToSend);
    }

    public Point getLocation() {
        return getElement().getLocation();
    }

    public void submit() {
        getElement().submit();
    }

    public String getAttribute(String name) {
        return getElement().getAttribute(name);
    }

    public String getCssValue(String propertyName) {
        return getElement().getCssValue(propertyName);
    }

    public String getValue() {
        return getElement().getAttribute("value");
    }

    public Dimension getSize() {
        return getElement().getSize();
    }

    public org.openqa.selenium.Rectangle getRect() {
        return null;
    }

    public List<WebElement> findElements(By by) {
        return getElement().findElements(by);
    }

    public String getText() {
        return getElement().getText();
    }

    public String getTagName() {
        return getElement().getTagName();
    }

    public boolean isSelected() {
        return getElement().isSelected();
    }

    public WebElement findElement(By by) {
        return getElement().findElement(by);
    }


    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    public void clear() {
        getElement().clear();
    }

    public WebElement getWrappedElement() {
        return getElement();
    }

    public Coordinates getCoordinates() {
        return ((Locatable) getElement()).getCoordinates();
    }

    public boolean elementWired() {
        return (getElement() != null);
    }

    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }

    public ElementImpl and() {
        return this;
    }

    public ElementImpl then() {
        return this;
    }

    public boolean isTextPresentInDropdown(String text) {

        try {
            waitForJQueryComplete();
            waitForJavascriptComplete();
            waitUntilEnabled();

            Select dropdown = new Select(getElement());
            return   dropdown
                    .getOptions()
                    .parallelStream()
                    .anyMatch(element -> element.getText().contains(text));

        } catch (StaleElementReferenceException | java.util.NoSuchElementException | ElementNotVisibleException e) {
            return false;
        }
    }

    public ElementImpl type(String text){
        int i = 0;

        while(i < 5){
            try{
                waitUntilVisible();
                getElement().clear();
                getElement().sendKeys(text);
                waitForJavascriptComplete();
                waitForJQueryComplete();
                return this;
            }
            catch (org.openqa.selenium.StaleElementReferenceException e){
                System.out.println("Refresh Element in DOM");
                i++;
            }
        }

        throw new StaleElementReferenceException(this.locator.toString());
    }

    protected void validatePresenceOfAlert() {
        try {
            GridManager.getDriver().switchTo().alert();
            String errorMsg = "Encountered an alert. Cannot wait for an element when an operation triggers an alert.";
            throw new InvalidElementStateException(errorMsg);
        } catch (NoAlertPresentException exception) {
            // gobble the exception and do nothing with it. No alert was
            // triggered.
            // so its safe to proceed ahead.
        }
    }

    private boolean driverIsDisabled() {
        Stack<Boolean> webdriverSuspensions = new Stack<Boolean>();
        return !webdriverSuspensions.isEmpty();
    }

    private ExpectedCondition elementIsDisplayed() {
        return driver -> Boolean.valueOf(ElementImpl.this.isCurrentlyVisible());
    }

    private ExpectedCondition elementIsEnabled() {
        return driver -> Boolean.valueOf(ElementImpl.this.isCurrentlyEnabled());
    }

    private ExpectedCondition allElementsEnabled() {
        return driver -> Boolean.valueOf(ElementImpl.this.isAllEnabled());
    }

//    private ExpectedCondition allElementsEnabled() {
//        return driver -> Boolean.valueOf(ElementImpl.this.isAllEnabled());
//    }

    public ElementImpl waitForJQueryComplete() {
        try {
            hardWait();
            while (true) // Handle timeout somewhere
            {
                boolean ajaxIsComplete = (boolean) ((JavascriptExecutor) GridManager.getDriver()).executeScript("return jQuery.active == 0");
                if (ajaxIsComplete)
                    break;
                hardWait();
            }
        } catch (Exception e) {
            //Implement logger
        }
        return this;
    }

    public ElementImpl waitForJavascriptComplete() {
        try {
            WebDriverWait wait = new WebDriverWait(GridManager.getDriver(), WebDriverWait.DEFAULT_SLEEP_TIMEOUT);
            wait.until((WebDriver driver) -> {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
            );
        } catch (Exception e) {
            //Implement logger
        }
        return this;
    }

    private void hardWait() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected String getErrorMessage(String defaultErrorMessage) {
        return (String) this.expectedErrorMessage.or(defaultErrorMessage);
    }

    private void failWithMessage(String errorMessage) {
        throw new AssertionError(this.getErrorMessage(errorMessage));
    }

    private void throwErrorWithCauseIfPresent(Throwable timeout, String defaultMessage) {
        String timeoutMessage = timeout.getCause() != null ? timeout.getCause().getMessage() : timeout.getMessage();
        String finalMessage = StringUtils.isNotEmpty(timeoutMessage) ? timeoutMessage : defaultMessage;
        throw new ElementNotVisibleException(finalMessage, timeout);
    }
}
