package tests;

import helpers.BaseTest;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class ExtendBase extends BaseTest {
    @Test
    public void extendBaseFlow() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.ca");
        Assert.assertTrue(false);
    }
}
