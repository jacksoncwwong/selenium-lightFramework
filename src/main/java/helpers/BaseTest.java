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

  //pre initializing Actions here because why not, we'll need it
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

    //if we're just writing a comment, the if/else statement helps so we don't clutter the system print logs
    if(feature.equals("") && status.equals("")) {
      System.out.println("Comments: " + comments);
    }
    else {
      System.out.println(
          "Time: " + timeString
              + " Test Feature: " + feature
              + " Status: " + status
              + " Comments: " + comments
      );
    }

    //if statement to check if something has failed or been skipped, and if so we update testFailChecker
    if( status.toUpperCase().equals("FAIL") || status.toUpperCase().equals("SKIPPED") ) {
      SharedInfo.testFailChecker = true;
//            System.out.println("failure detected");
    }
    //regardless we will insert the timestamp into our results
    ExcelUtil.WriteCsvRecords(new String[]{timeString, feature, status, comments});
  }

  public static void writeReport() {
    ExcelUtil.testResultsExcelFileName = ExcelUtil.generateFileName();

    if (SharedInfo.testFailChecker == true) {
      ExcelUtil.testResultsExcelFileName = ExcelUtil.testResultsExcelFileName + "-FAIL";
//            System.out.println("FAIL added to file name, new name is " + ExcelUtil.testResultsExcelFileName);
    }

    ExcelUtil.testResultsExcelFileName = ExcelUtil.testResultsExcelFileName + ".csv";
//        System.out.println(ExcelUtil.testResultsExcelFileName);

    ExcelUtil.csvFile = ExcelUtil.testResultsFilePath + ExcelUtil.testResultsExcelFileName;
//        System.out.println("csvFile =  " + ExcelUtil.csvFile);

    try {
      ExcelUtil.WriteToFile(ExcelUtil.csvHeader + ExcelUtil.csvRecords.toString(), ExcelUtil.testResultsExcelFileName);
      //resetting testFailChecker after results are written
    } catch (Exception e) {
      try {
        throw (e);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    SharedInfo.testFailChecker = false;
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
    //we set the url depending on environment with the if statement below
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

    //we initialize the column numbers based on how I know they're structured
    int environmentColumnNumber = 0;
    int sponsorColumnNumber = 1;
    int tierColumnNumber = 2;
    int loginUserColumnNumber = 3;
    int loginPwdColumnNumber = 4;
    String testCell = "not blank";
    int cellCount = 0;

    //we also run a loop to update the column numbers if they are incorrect
    //this only works if you use the same headings of course
    while(!testCell.equals("")) {
      testCell = ExcelUtil.getCellData(0, cellCount);

      if (testCell.toUpperCase().equals("ENVIRONMENT")) {
        environmentColumnNumber = cellCount;
        System.out.println("environmentColumnNumber is " + environmentColumnNumber);
      }
      else if (testCell.toUpperCase().equals("SPONSOR")) {
        sponsorColumnNumber = cellCount;
        System.out.println("sponsorColumnNumber is " + sponsorColumnNumber);
      }
      else if (testCell.toUpperCase().equals("TIER")) {
        tierColumnNumber = cellCount;
        System.out.println("tierColumnNumber is " + tierColumnNumber);
      }
      else if (testCell.toUpperCase().equals("LOGINUSER")) {
        loginUserColumnNumber = cellCount;
        System.out.println("loginUserColumnNumber is " + loginUserColumnNumber);
      }
      else if (testCell.toUpperCase().equals("LOGINPWD")) {
        loginPwdColumnNumber = cellCount;
        System.out.println("loginPwdColumnNumber is " + loginPwdColumnNumber);
      }

      cellCount++;
    }

    //if sponsor is empty then we skip all the sponsor logic, and just match environment for credentials
    if (sponsor.equals("")) {
      writeData("", "", "Environment is " + SharedInfo.env + " and sponsor not specified");
      for (int i = 0; i < SharedInfo.credentialsCount; i++) {
        String testEnv = ExcelUtil.getCellData(i, environmentColumnNumber);

        //only need to match environment
        if (testEnv.toUpperCase().equals(SharedInfo.env)) {
          loginUser = ExcelUtil.getCellData(i, loginUserColumnNumber);
          loginPwd = ExcelUtil.getCellData(i, loginPwdColumnNumber);
          writeData("", "", "credential match WITHOUT tier OR sponsor info provided");
          writeData("", "", "username is " + loginUser + " and password is " + loginPwd);
          break;
        }
      }
    }
    else { // if a sponsor is specified we then reset the login credentials with the sponsor in mind
      // assigning the shared variable currentSponsor as the sponsor provided
      SharedInfo.currentSponsor = sponsor;

      // loop to go through spreadsheet per row to find the a match of sponsor and environment (optionally checking tier)
      for (int i = 0; i < SharedInfo.credentialsCount; i++) {
        String testEnv = ExcelUtil.getCellData(i, environmentColumnNumber);
        String testSponsor = ExcelUtil.getCellData(i, sponsorColumnNumber);
        String testTier = ExcelUtil.getCellData(i, tierColumnNumber);

        //this if statement will check if currentTier is empty (meaning tier is not required)
        if (SharedInfo.currentTier.equals("")) {
          //if tier info is not required then we just match env and sponsor
          if (testEnv.equals(SharedInfo.env) && testSponsor.equals(sponsor)) {
            loginUser = ExcelUtil.getCellData(i, loginUserColumnNumber);
            loginPwd = ExcelUtil.getCellData(i, loginPwdColumnNumber);
            writeData("", "", "credential match WITHOUT tier info found");
            break;
          }
        }
        else {
          //given that currentTier is not empty, we'll have to match for that too here
          if ( testEnv.equals(SharedInfo.env) && testSponsor.equals(sponsor) && testTier.equals(SharedInfo.currentTier) ) {
            loginUser = ExcelUtil.getCellData(i, loginUserColumnNumber);
            loginPwd = ExcelUtil.getCellData(i, loginPwdColumnNumber);
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
