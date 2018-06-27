package tests;

import helpers.BaseTest;
import helpers.ExcelUtil;
import helpers.SharedInfo;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class ExtendBase extends BaseTest {
    @Test
    public void extendBaseFlow() {
        SharedInfo.testName = "extendBaseFlow";
        ExcelUtil.setTestExcelData();
        setupEnv("");

        Assert.assertTrue(false);
    }
}
