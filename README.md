# selenium-lightFramework
A light set of functions that will generate csv files for test results, read test data off Excel files, and test using TestNG.

## Instructions
### Quick Intro
For the most part this is meant to help with reporting for automated tests. I want to start by mentioning that there are other reporting tools, such as [ReportNG](https://reportng.uncommons.org/), [Maven SureFire plugin](https://maven.apache.org/surefire/maven-surefire-report-plugin/), [ExtentReport](http://extentreports.com/) just to name a few. You may be wondering: "If there were others already, why did you do this?" and I would say that's a great question. 

**_Was it in-part due to the fact that I started writing this before I found out about these alternatives?_** 

~YES~ Perhaps... 

However, I decided to continue finishing this framework because there were just things I didn't really like in the alternatives I found: some of them I had a tough time getting to work, others didn't really have features that I felt were needed etc.

### Using Page Object Model
This is written with the assumption that you're working with Page Object Model(POM). There are many resources online that will explain what POM is, but for the most part this just means that you have a file for every page which grabs all the elements, and you have a separate file that will utilize these elements and test their functionality. 

One big advantage of POM is of course the ease of refactoring if these elements have changed. Since the management of these elements are organized by page, and all in one place, making changes are easy and non-repetitive. If you don't have a centralized place for all your elements, you'd have to go into every test that is affected by the change and update the xpath/selectors which is a major pain.

### Why read Excel but write to csv?
This is because I don't currently have a paid version of Excel and am too lazy to ask for one. This also made me realize that there are likely others stuck in my predicament, and I don't see any significant downside therefore I rolled with it. From my personal experience I'm still able to read excel files but can't save or write them, and writing to csv is free (and really we're just writing out a log, we don't need anything fancy).

The major downside here is that comments in the logs cannot contain commas, or should refrain from using commas since it would cause what comes after the comma to be in the next cell (CSV = comma separated values afterall).

### How to use this?
For the most part, you just need to download this and you can start writing tests within the "tests" directory and page objects within the "pages" directory. Refer to the sample test I wrote called ExtendBase.java, basically you need to follow the basic structure of extending BaseTest, starting with @Test, and naming your method within the class:
```java
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

        //start writing your test from here, the line below is just something to trigger a failure, please remember to remove when writing your own test
        Assert.assertTrue(false);
    }
}
```

## Folder Structure
This is what makes sense to me, but then what do I know? In anycase, here's how they're organized:
### Pages
This is where I would recommend you store all your page models.

### Tests
This is where I would recommend you store all your tests.

### Helpers
This is where I would recommend you store all methods and functions that get used by the tests you write. Separating these from the tests themselves help with organization and refactoring (I often name tests after their features, which can make them easily confused with my helpers)

### Results
This folder is where all the csv files for your test results will go. The function that generates the file names are under this format 
```
<year>.<month>.<day>-<sequence number>-<environment>-<project name>-<test name>.csv
```
Here's an example: 2018.07.13-023-QA-jupiterTesting-createAccountFlow.csv

Since we often run multiple times a day, the sequence number gets incremented each time you run a test for that day (gives you an idea of which tests were run first etc). Based on the example provided above, the sequnce number "023" means this is the 23rd test I've run on July 13th, 2018, for the project "jupiterTesting", for testing the feature called "createAccountFlow".

You might be wondering why I did this... 
1. Due to the naming convention this means that all files will automatically be organized in reverse chronological order (the most recent being first). 
2. I really like having as much information in the title of a file as possible, that way I'm sure this is the file I want to open. 
3. It really helps when I'm trying to see all the tests I've ever run for a particular feature (just search for that feature in the folder), and they will automatically be listed in reverse chronological order (instead of going into logs of tests you ran for that day, trying to find the part that has to do with that feature, extracting that for each day/test and then comparing them).

One last thing, if a test fails, the csv file name will have a suffix of -FAIL. The way this is managed is via the testFailChecker boolean, which is used in the writeData method and gets reset in the writeReport method. For more details refer to the Methods section in this readme.

### Resources
Throw all the test data and credentials etc into here (preferrably in an excel spreadsheet). Basically any kind of data that is to be read by your tests should go here. If you're having a tough time with this one, just throw anything that needs to be used but isn't code in here.

#### dateTracker.csv
This is a csv file I use to keep track of the date and sequence number last used. Please refrain from moving this or changing this in anyway (or else the file names will be wrong).

#### projectTestData.xlsx
This is a sample spreadsheet of how I organize login credentials. If the current structure works for you then you can use as is (if you don't need a sub category for your users like "Sponsor" then just leave that blank). If you end up changing the order of columns then you'll have to update the setupEnv() method in the BaseTest.java file from Helpers folder. I was lazy and it currently grabs username and password just based on the column number I wrote in, I will update this in the future and have it read the column header to know what it's reading.

## Methods
There are a number of pre-written methods/functions in here. Few are written by me, most are just copied and refactored to fit what I'm trying to do, I will do my best here to explain what each is for:

### BaseTest.java
This class contains methods that are commonly used for almost every test.

#### getDriver()

#### setUp()

#### wrapUp()

#### writeData(String feature, String status, String comments)

#### writeReport()

#### shortWait() and longWait()

#### isElementDisplayed(WebElement element) and waitForElementToBeGone(WebElement element, int timeout)

#### setupEnv(String sponsor)

### ExcelUtil.java
This class contains methods that mostly deal with reading Excel files and writing csv files. Many of these are just retreived from StackOverflow with minor changes by me to work for my needs. I won't talk about all of them here as many are self-explanatory.

#### generateFileName()

#### setTestExcelData()

#### WriteCsvHeader()

#### WriteCsvRecords()

#### WriteToFile()
