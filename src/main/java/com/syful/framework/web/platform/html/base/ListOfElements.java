package com.syful.framework.web.platform.html.base;


import com.syful.framework.web.platform.html.support.ElementImpl;
import com.syful.framework.web.platform.html.support.ImplementedBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@ImplementedBy(ElementImpl.class)
public interface ListOfElements extends Element{

    WebElement getElement(int index);

    List<WebElement> getElements();

    ListOfElements waitElementsReady();

    WebElement getFirstElementFromList();

    WebElement getFirstRelatedElementFromList(String filter);

    WebElement getFirstRelatedElementFromList(String filter1, String filter2);

    boolean isElementPresentInList(String filter);

    boolean isElementPresentInList(String filter1, String filter2);
    WebElement getChildElement(String filter, By locator);
}

