package testcases.web;

import com.syful.framework.adapters.MainTestSessionListener;
import com.syful.framework.annotations.WebTest;
import com.syful.framework.web.platform.MainPlatform;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({MainTestSessionListener.class})
public class LoginTestSuite {

    @Test(groups = {"mainRegression", "loginPage"}
            , testName = "Login test case one")
    @WebTest(id = "1234", locationAccountName = "none")
    public void validLoginTest(){

        MainPlatform.loginAs();
    }
}
