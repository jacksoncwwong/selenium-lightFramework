package tests;

import helpers.BaseTest;
import helpers.ExcelUtil;
import helpers.SharedInfo;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SamplePage;

public class ExtendBase extends BaseTest {
    static SamplePage SamplePage = PageFactory.initElements(getDriver(), SamplePage.class);

    @Test
    public void extendBaseFlow() {
        //can change environment for each of the tests like so:
        SharedInfo.env = "QA";
        //declaring test name below
        SharedInfo.testName = "extendBaseFlow";

        //this line sets up your spreadsheets:
        ExcelUtil.setTestExcelData();
        //this line sets up your environment and login credentials:
        setupEnv("");

        //*** most likely start writing your test from here ***
        //loginUser and loginPwd should be set at this point, you can use them like so:
        //(the code below is just an example, it doesn't work)
//        SignInPage.signInUserInput.sendKeys(loginUser);
//        SignInPage.signInPwdInput.sendKeys(loginPwd);

        //example of how to call elements from SamplePage
        //the line below only works if SharedInfo.env = "QA":
        SamplePage.continueBtn.click();

        //the line below is just something to trigger a failure
        //run the test as is to demonstrate what a failed report would look like
        Assert.assertTrue(false);
    }
}
