# selenium-lightFramework
A light set of functions that will generate csv files for test results, read test data off Excel files, and test using TestNG.

## Instructions
### Quick Intro
For the most part this is meant to help with reporting for automated tests. I want to start by mentioning that there are other reporting tools, such as [ReportNG](https://reportng.uncommons.org/), [Maven SureFire plugin](https://maven.apache.org/surefire/maven-surefire-report-plugin/), [ExtentReport](http://extentreports.com/) just to name a few. You may be wondering: "If there were others already, why did you do this?" and I would say that's a great question. 

**_Was it in-part due to the fact that I started writing this before I found out about these existing tools?_** 

~YES~ Perhaps... 

However, I decided to continue finishing this framework because there were just things I didn't really like in the alternatives I found: some of them I had a tough time getting to work (or they're not maintained anymore and documentation is lacking), others didn't really have features that I felt were needed (or you had to pay for them) etc.

### Using Page Object Model
This is written with the assumption that you're working with Page Object Model(POM). There are many resources online that will explain what POM is, but for the most part this just means that you have a file for every page which grabs all the elements, and you have a separate file that will utilize these elements and test their functionality. 

One big advantage of POM is of course the ease of refactoring if these elements have changed. Since the management of these elements are organized by page, and all in one place, making changes are easy and non-repetitive. If you don't have a centralized place for all your elements, you'd have to go into every test that is affected by the change and update the xpath/selectors which is a major pain.

### Why read Excel but write to csv?
This is because I don't currently have a paid version of Excel and am too lazy to ask for one. This also made me realize that there are likely others stuck in my predicament, and I don't see any significant downside therefore I rolled with it. From my personal experience I'm still able to read excel files but can't save or write them, and writing to csv is free (and really we're just writing out a log, we don't need anything fancy).

The major downside here is that comments in the logs cannot contain commas, or should refrain from using commas since it would cause what comes after the comma to be in the next cell (CSV = comma separated values afterall).

### How to use this?
For the most part, you just need to download this and you can start writing tests within the "tests" directory and page objects within the "pages" directory. Refer to the sample test I wrote called ExtendBase.java and the sample page I wrote called SamplePage.java, here's SamplePage.java:
```java
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SamplePage {
    final WebDriver driver;

    @FindBy(how = How.XPATH, using = "//*[@id=\"qacookies-continue-button\"]")
    public WebElement continueBtn;

    public SamplePage (WebDriver driver) {
        this.driver = driver;
    }
}
```

and here's ExtendBase.java:
```java
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
```

So as you can see, this basically works just like how you would with POM (although I admit everyone implements POM slightly differently), the main thing is to start every test with these four lines:
```java
SharedInfo.env = "QA";    
SharedInfo.testName = "extendBaseFlow";
ExcelUtil.setTestExcelData();
setupEnv("");
```

## What the results look like
So you know what you're getting yourself into :D
The results folder has a bunch of test results in there, I've also linked to one below for convenience. There's a screenshot of what the results folder would look like also. It may seem crazy at first, but if you bear with me you may find this to be useful (hopefully).

### Example of csv file
[This](https://github.com/jacksoncwwong/selenium-lightFramework/blob/master/src/main/results/2018.07.18-001-QA-TestingFramework-extendBaseFlow-FAIL.csv) is a sample file of a test that failed. It's not pretty, but I would say everything you really need is in there.

### Example of folder populated with tests
![alt text](https://github.com/jacksoncwwong/selenium-lightFramework/blob/master/results-screenshot.png "image showing what the files look like in the results folder")

Please refer to the "Results" section below (under Folder Structure) to get an idea of why these are named like that.

## Folder Structure
This is what makes sense to me, but then what do I know? In anycase, here's how they're organized:
### Pages
This is where I would recommend you store all your page models.

### Tests
This is where I would recommend you store all your tests.

### Helpers
This is where I would recommend you store all methods and functions that get used by the tests you write. Separating these from the tests themselves help with organization and refactoring (I often name tests after their features, which can make them easily confused with my helpers).

I'll discuss more about the methods used here, however for now I just want to bring your attention to **SharedInfo.java**. This file contains variables that are important and used in various places. I've separated these variables into 3 categories: 
#### Things you might want to change
So these are things like: what's the current environment you're testing in, what is the project name, what is the email address you want to use (for those times when you need to enter an email into a form and receive some response back), etc etc. These are generally things that won't really break your tests even if they're wrong.

#### Things you probably don't want to change
I've got the urls for QA, UAT and PROD here, you can obviously put other things here as well.

#### Things you should never change unless you're very very very sure
This is where I put very very important variables, like file paths, variables that are initialized and very important to various functions/methods, important file names that shouldn't be changed etc. These are things that can definitely break this framework if you change them, so I've separated them a bit to give you fair warning :D.

### Results
This folder is where all the csv files for your test results will go. The function that generates the file names are under this format 
```
<year>.<month>.<day>-<sequence number>-<environment>-<project name>-<test name>.csv
```
Here's an example: 2018.07.13-023-QA-jupiterTesting-createAccountFlow.csv

Since we often run multiple times a day, the sequence number gets incremented each time you run a test for that day (gives you an idea of which tests were run first etc). Based on the example provided above, the sequnce number "023" means this is the 23rd test I've run on July 13th, 2018, for the project "jupiterTesting", for testing the feature called "createAccountFlow".

You might be wondering why I did this... 
1. Due to the naming convention this means that all files will automatically be organized in chronological order.
2. I really like having as much information in the title of a file as possible, that way I'm sure this is the file I want to open. 
3. It really helps when I'm trying to see all the tests I've ever run for a particular feature (just search for that feature in the folder), and they will automatically be listed in chronological order (instead of going into logs of tests you ran for that day, trying to find the part that has to do with that feature, extracting that for each day/test and then comparing them).

One last thing, if a test fails, the csv file name will have a suffix of -FAIL. The way this is managed is via the testFailChecker boolean, which is used in the writeData method and gets reset in the writeReport method. For more details refer to the Methods section in this readme.

### Resources
Throw all the test data and credentials etc into here (preferrably in an excel spreadsheet). Basically any kind of data that is to be read by your tests should go here. If you're having a tough time with this one, just throw anything that needs to be used but isn't code in here.

#### dateTracker.csv
This is a csv file I use to keep track of the date and sequence number last used. Please refrain from moving this or changing this in anyway (or else the file names will be wrong).

#### projectTestData.xlsx
This is a sample spreadsheet of how I organize login credentials. If the current structure works for you then you can use as is (if you don't need a sub category for your users like "Sponsor" then just leave that blank). If you end up changing the order of columns then you'll have to update the setupEnv() method in the BaseTest.java file from Helpers folder. I was lazy and it currently grabs username and password just based on the column number I wrote in, I will update this in the future and have it read the column header to know what it's reading.

## Listeners
Listeners are used to trigger certain actions/methods/functions when certain events happen, and they are managed in the TestMethodListener.java file. For the most part they are very self-explanatory, there are triggers for the start/end of a suite/test, when a test is successful, **when a test failed** (probably the most important one) etc etc. If you need more control when these kinds of situations occur, this is where you'd want to do some tinkering. 

## Methods
There are a number of pre-written methods/functions in here. Few are written by me, most are just copied and refactored to fit what I'm trying to do, I will do my best here to explain what each is for:

### BaseTest.java
This class contains methods that are commonly used for almost every test.

#### getDriver()
Just a centralized function for setting up your driver. So that you don't have to initialize your driver at the beginning of every test, and if you need to change to another browser you can easily do so for all tests. You'll have to write some extra code if you want it to run through a bunch of different browsers for each test, or you can wait and I'll end up doing that at some point. 

In terms of usage, instead of driver.get("www.google.com") it's getDriver().get("www.google.com"). 

#### setUp()
The name isn't actually important, but you'll notice in the code that setup() method is "prefixed" with @BeforeClass, which is a TestNG thing. This basically gets run at the beginning of every test. You can throw all kinds of things in here if you want, like if you wrote a login function and all your tests always require login, you can throw that in here. I've just got some timeouts being set here at the moment.

#### wrapUp()
This is very similar to setUp(), instead of @BeforeClass it's got @AfterClass instead, and it just runs this at the end of every test. I've got it running the writeReport() method for now, which writes the results into a csv file. You may likely want to include a logout function for yourself, if your tests require logging in and out with different users and capabilities for each user etc.

#### writeData(String feature, String status, String comments)
This is a function that helps us log things, and it automatically adds a timestamp. When using this you have to provide a string for feature, status, and comments, however these can be blank strings of course. It will automatically System.out.println the whole thing, and it also detects if you or any of the listeners are sending a "fail" for the status, in which case it updates the testFailChecker boolean to "true". The testFailChecker boolean is used by writeReport() method to determine if we need to add a suffix of "FAIL" to the title of the test results csv file. I've also commented on the code so it shouln't be hard to follow.

#### writeReport()
This function is used to do a few things:
1. generate the file name
2. check if the current test has failed in any respect, and if so it adds a "-FAIL" suffix to the file name
3. writes the header and all the test results into the csv file

So basically how the csv file writing works is that, we store the header, and then the content, as strings separately. These are stored as the variables csvHeader and csvRecords respectively. We then only append them all together and write it to the csv file at the end, not writing to the file each time we run writeData(). This is in part because we don't know the file name until the end (because if it fails then the name changes), and it also feels like it would cost more time and resources to write to the file each time (have not tested this, just assuming, so I could be completely wrong).

#### shortWait() and longWait()
Just a basic Thread.sleep() function with the try catch written in for you. Helps save you time, and also centralizes all your waits if you need to change them later. From my experiences, if you really need to force a wait in your tests, it's usually pretty consistent in that it's a relatively short wait, or a loooong wait. You don't need to use any of these and can write custom Thread.sleep() or any other kind of waitTillVisible or whatever you want!

#### isElementDisplayed(WebElement element) and waitForElementToBeGone(WebElement element, int timeout)
These two functions are niftly little things I found on StackOverflow. I kept running into this issue of using ExpectedConditions.elementToBeClickable or visibilityOfElementLocated and it failing because of a loading spinner that is semi-transparent. The stupid spinner blocks us from clicking the element, but it's clickable and visible so you just get a failure instantly, very frustrating and annoying.

To use this function, you need to grab the element and give it to waitForElementToBeGone:
```java
WebElement loadSpinner = getDriver().findElement(By.className("loadSpinner"));
waitForElementToBeGone(loadSpinner, 30);
```
This basically will sit there and wait for however long you assigned it to wait (in this case as you can see, it's 30 seconds), or until the load spinner is gone (if it happens before the assigned amount of time). You don't actually end up using isElementDisplayed yourself, if you look at the code you'll see that isElementDisplayed() is called within waitForElementToBeGone().

#### setupEnv(String sponsor)
This function does a little too much, I might need to re-write this a bit... anyway it does the following:
1. depending on what SharedInfo.env is, it grabs the appropriate url
2. it also checks the testing credentials spreadsheet and tries to grab the appropriate crendentials depending on the environment, if you specified a sponsor (leave as a blank string if this is unneccessary), if the users are split into different tiers (again, ignore if unneccessary) etc.

The code has a good amount of comments in it, feel free to have a look and tell me how bad it is :P

### ExcelUtil.java
This class contains methods that mostly deal with reading Excel files and writing csv files. Many of these are just retreived from StackOverflow with minor changes by me to work for my needs. I won't talk about all of them here as many are self-explanatory.

#### generateFileName()
This is the function that generates the crazy and complicated file name that I explained under Results near the top of this readme. I basically:
1. grab todays date
2. compare that date to the date written in the dateTracker.csv file (which stores the date of the last time I ran a test)
3. if the date is different, we update the "sequence number" to 1 (which is also explained under Results)
4. if the date is the same, then we keep using this date, but increment the sequence number
5. then we grab the rest of the good stuff, like project name, test name etc, and return the whole string

#### setTestExcelData()
This function gets all the excel spreadsheets and csv files ready. We setup the file paths, initalize the spreadsheets, and write out the header for the csv file here. For more info on what I mean by initializing the spreadsheet (or how to use the API that works with excel spreadsheets) you can read about that from a tutorial [here](https://www.tutorialspoint.com/apache_poi/apache_poi_workbooks.htm) and the documentation [here](https://poi.apache.org/apidocs/org/apache/poi/xssf/usermodel/XSSFWorkbook.html).

#### WriteCsvHeader()
This function just helps to build the String properly: makes sure each header is comma separated, appends a new line \n at the end of the header. Just a little helper (I stole this).

#### WriteCsvRecords()
Similar to the function above, this is just a helper function to make sure the records are properly comma separated, and appends a new line \n at the end.

#### WriteToFile()
This is the function that actually finalizes the file name and writes everything into the new file. It also clears the csvRecords variable so we don't carry over old records to the new files.
