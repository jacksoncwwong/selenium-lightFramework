package tests;

import helpers.BaseTest;
import helpers.ExcelUtil;
import helpers.SharedInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExtendBase extends BaseTest {
    @Test
    public void extendBaseFlow() {
        //can change environment here for each of the tests
//        SharedInfo.env = "QA";
        SharedInfo.testName = "extendBaseFlow";

        ExcelUtil.setTestExcelData();
        setupEnv("");

        Assert.assertTrue(false);
    }
}
