package com.syful.framework.web.browser;


import com.syful.framework.web.exception.UnknownBrowserException;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.HashMap;

public enum BrowserType {

    FIREFOX,
    IE,
    CHROME,
    HTMLUNIT;

    private static Map<String, BrowserType> browsersMap = new HashMap();

    static {
        browsersMap.put("firefox", BrowserType.FIREFOX);
        browsersMap.put("ie", BrowserType.IE);
        browsersMap.put("chrome", BrowserType.CHROME);
        browsersMap.put("htmlunit", BrowserType.HTMLUNIT);
    }

    public static BrowserType Browser(String name){
        BrowserType browserType = null;
        if (name != null) {
            browserType = browsersMap.get(name.toLowerCase().trim());
            if (browserType == null) {
                throw new UnknownBrowserException("Unknown browser [" + name + ". Use one of the following: "
                    + StringUtils.join(browsersMap.keySet().toArray(), ", "));
            }
        }

        return browserType;
    }
}
