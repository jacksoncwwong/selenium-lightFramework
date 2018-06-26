package helpers;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Listeners(TestMethodListener.class)

public class BaseTest {
    private static WebDriver driver;

    static WebDriverWait wait = new WebDriverWait(getDriver(), 60);

    static Actions actions = new Actions(getDriver());

    public static WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--kiosk");
            driver = new ChromeDriver(chromeOptions);
            return driver;
        }
        else {
            return driver;
        }
    }

    @BeforeClass
    public void setUp() throws Exception{
        getDriver().manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
    }

    @AfterClass
    public void wrapUp(){
//        logoutFlow();
        writeReport();
    }

    public static void writeData(String feature, String status, String comments) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        String timeString = timeFormat.format(time);

        if( feature == "" && status == "" ) {
            System.out.println("Comments: " + comments);
        }
        else {
            System.out.println("Time: " + timeString + " Test Feature: " + feature + " Status: " + status + " Comments: " + comments);
        }
        ExcelUtil.WriteCsvRecords(new String[]{timeString, feature, status, comments});
    }

    public static void writeReport() {
        try {
            ExcelUtil.WriteToFile(ExcelUtil.csvHeader + ExcelUtil.csvRecords.toString(), ExcelUtil.testResultsExcelFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shortWait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void longWait() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isElementDisplayed(WebElement element) {
        try {
            WebDriverWait spinnerWait = new WebDriverWait(getDriver(), 1);
            spinnerWait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException
                | org.openqa.selenium.StaleElementReferenceException
                | org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public static void waitForElementToBeGone(WebElement element, int timeout) {
        if (isElementDisplayed(element)) {
            new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
        }
    }

    public static String loginUser = "";
    public static String loginPwd = "";
    public static void setupEnv(String sponsor) {
        String env = SharedInfo.env;

        //we set the url depending on environment with the if statement below, we also preset the login credentials
        if (env == "QA") {
            getDriver().get(SharedInfo.qaUrl);
            loginUser = ExcelUtil.getCellData(1, 2);
            loginPwd = ExcelUtil.getCellData(1, 3);
        }
        else if (env == "UAT") {
            getDriver().get(SharedInfo.uatUrl);
            loginUser = ExcelUtil.getCellData(12, 2);
            loginPwd = ExcelUtil.getCellData(12, 3);
        }
        else if (env == "PROD") {
            getDriver().get(SharedInfo.prodUrl);
            loginUser = ExcelUtil.getCellData(13, 2);
            loginPwd = ExcelUtil.getCellData(13, 3);
        }

        if (sponsor == "") {
            writeData("", "", "Environment is " + env + " and sponsor not specified");

        }
        else { // if a sponsor is specified we then reset the login credentials with the sponsor in mind
            for (int i = 0; i < SharedInfo.credentialsCount; i++) {
                String testEnv = ExcelUtil.getCellData(i, 0);
                String testSponsor = ExcelUtil.getCellData(i, 1);
                if (testEnv == env && testSponsor == sponsor) {
                    loginUser = ExcelUtil.getCellData(i, 2);
                    loginPwd = ExcelUtil.getCellData(i, 3);
                }
                writeData("", "", "Environment is " + env + " and the sponsor is " + sponsor);

            }
        }
    }
}
