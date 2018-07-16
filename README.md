# selenium-lightFramework
a light set of functions that will generate csv files for test results, read test data off excel file, and test using TestNG

## Instructions
### Quick Intro
I'm under the assumption you are writing tests using the Page Object Model. If that term is new to you please feel free to look it up, but for the most part this just means that you have a file for every page which grabs all the elements you need to use, and you have a separate file that will utilize these elements and test their functionality. The biggest advantage I see for Page Object Model is the ease of refactoring if these elements have changed. Since the management of these elements are all in one place, you only have to make these changes one time. Otherwise, you'd have to go into every test that is affected by the change and update the xpath/selectors.

### Why read Excel but write to csv?
This is because I don't currently have a paid version of Excel and am too lazy to ask for one. From my personal experience I'm still able to read excel files but can't save or write them, and writing to csv is free (and really we're just writing out a log, we don't need anything fancy).

The major downside here is that comments in the logs cannot contain commas, or should refrain from using commas since it would cause the rest of that comment to be in the next cell (csv stands for comma separated values afterall).

## Folder Structure
This is what makes sense to me, but then what do I know? In anycase, here's how they're organized:
### Pages
This is where I would recommend you store all your page models.

### Tests
This is where I would recommend you store all your tests.

### Helpers
This folder is meant for all methods and functions that get used by the tests you write, separating these from the tests themselves help with organization and refactoring (I often name tests after their features, which can make them easily confused with my helpers)

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

### Resources
Throw all the test data and credentials etc into here (preferrably in an excel spreadsheet). Basically any kind of data that is to be read by your tests should go here. If you're having a tough time with this one, just throw anything that needs to be used but isn't code in here.

#### dateTracker.csv
This is a csv file I use to keep track of the date and sequence number last used. Please refrain from moving this or changing this in anyway (or else the file names will be wrong).

#### projectTestData.xlsx
This is a sample spreadsheet of how I organize login credentials. If the current structure works for you then you can use as is (if you don't need a sub category for your users like "Sponsor" then just leave that blank). If you end up changing the order of columns then you'll have to update the setupEnv() method in the BaseTest.java file from Helpers folder. I was lazy and it currently grabs username and password just based on the column number I wrote in, I will update this in the future and have it read the column header to know what it's reading.
