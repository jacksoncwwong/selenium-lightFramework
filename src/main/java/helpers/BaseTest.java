package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
        //may want a logout function here
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

    //generic function for a short and long waits
    //this way we standardize all waits and can change it all in one place
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

    //used in conjunction with the waitForElementToBeGone method
    //to wait out a spinner so that you don't just fail assertions or clicks because the spinner is blocking
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
        //we set the url depending on environment with the if statement below, we also preset the login credentials
        if (SharedInfo.env.equals("QA")) {
            getDriver().get(SharedInfo.qaUrl);
            writeData("", "", "Url is " + SharedInfo.qaUrl);
        }
        else if (SharedInfo.env.equals("UAT")) {
            getDriver().get(SharedInfo.uatUrl);
            writeData("", "", "Url is " + SharedInfo.uatUrl);
        }
        else if (SharedInfo.env.equals("PROD")) {
            getDriver().get(SharedInfo.prodUrl);
            writeData("", "", "Url is " + SharedInfo.prodUrl);
        }

        //if sponsor is empty then we skip all the sponsor logic, and just match environment
        if (sponsor.equals("")) {
            writeData("", "", "Environment is " + SharedInfo.env + " and sponsor not specified");
            for (int i = 0; i < SharedInfo.credentialsCount; i++) {
                String testEnv = ExcelUtil.getCellData(i, 0);
                String testSponsor = ExcelUtil.getCellData(i, 1);

                //only need to match environment
                if (testEnv.equals(SharedInfo.env)) {
                    loginUser = ExcelUtil.getCellData(i, 3);
                    loginPwd = ExcelUtil.getCellData(i, 4);
                    writeData("", "", "credential match WITHOUT tier OR sponsor info provided");
                    break;
                }
            }
        }
        else { // if a sponsor is specified we then reset the login credentials with the sponsor in mind
            // assigning the shared variable currentSponsor as the sponsor provided
            SharedInfo.currentSponsor = sponsor;

            // loop to go through spreadsheet per row to find the a match of sponsor and environment (optionally checking tier)
            for (int i = 0; i < SharedInfo.credentialsCount; i++) {
                String testEnv = ExcelUtil.getCellData(i, 0);
                String testSponsor = ExcelUtil.getCellData(i, 1);
                String testTier = ExcelUtil.getCellData(i, 2);

                //this if statement will check if currentTier is empty (meaning tier is not required)
                if (SharedInfo.currentTier.equals("")) {
                    //if tier info is not required then we just match env and sponsor
                    if (testEnv.equals(SharedInfo.env) && testSponsor.equals(sponsor)) {
                        loginUser = ExcelUtil.getCellData(i, 3);
                        loginPwd = ExcelUtil.getCellData(i, 4);
                        writeData("", "", "credential match WITHOUT tier info found");
                        break;
                    }
                }
                else {
                    //given that currentTier is not empty, we'll have to match for that too here
                    if ( testEnv.equals(SharedInfo.env) && testSponsor.equals(sponsor) && testTier.equals(SharedInfo.currentTier) ) {
                        loginUser = ExcelUtil.getCellData(i, 3);
                        loginPwd = ExcelUtil.getCellData(i, 4);
                        writeData("", "", "credential match with tier info found");
                        break;
                    }
                }
                //probably a better way to write this, but the main concern I had was to make sure if a tier is specified then we must find one with the correct tier
                //I wrote this another way before and the logic was flawed, if tier didn't match, the following if statement just tests whether sponsor and env match and would take that, ignoring the tier requirement
            }
            writeData("", "", "Environment is " + SharedInfo.env + " the sponsor is " + sponsor + " and the credentials used are " + loginUser + "/" + loginPwd);
        }
    }
}
