package com.syful.framework.web.platform.html.base;

import com.syful.framework.web.platform.html.support.ElementImpl;
import com.syful.framework.web.platform.html.support.ImplementedBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

/**
 * wraps a web element interface with extra functionality. Anything added here will be added to all descendants.
 */
@ImplementedBy(ElementImpl.class)
public interface Element extends WebElement, WrapsElement, Locatable{

    /**
     * Returns true when the inner element is ready to be used.
     *
     * @return boolean true for an initialized WebElement, or false if we were somehow passed a null WebElement.
     */
    boolean elementWired();

    Element waitUntilVisible();

    Element waitUntilVisible(long timeout);

    Element waitUntilNotVisible();

    Element waitUntilEnabled();

    Element waitForJQueryComplete();

    Element waitForJavascriptComplete();

    Element shouldBeVisible();

    Element shouldNotBeVisible();

    Element shouldBeEnabled();

    Element shouldBeDisabled();

    Element shouldBePresent();

    Element shouldNotBePresent();

    Element and();

    Element then();

    boolean isPresent();

    boolean isVisible();

    void click();

    void hitButton();

    Element jClick();

    Element check();

    Element uncheck();

    boolean isTextPresentInDropdown(String text);

    Element selectByText(String text);
    Element selectByExactText(String text);

    String selectFirstElementInDropdown();

    Element selectByIndex(Integer index);

    String getSelectedText();

    Element type(String text);

    String getValue();

    void selectCheckbox(boolean flag);

    Element switchToFrame();

    boolean isElementPresent();
}
