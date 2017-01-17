package com.syful.framework.adapters;

import com.syful.framework.web.config.Settings;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class FalseFailAnalyzer implements IRetryAnalyzer {
    private int count = 0;

    public int getCounter() { return  count; }

    @Override
    public boolean retry(ITestResult result) {
        if(count < Settings.getRetryCount()){
            count++;
            return true;
        }
        return false;
    }
}
