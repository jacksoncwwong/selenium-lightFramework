package helpers;

public class SharedInfo {
    //things you might want to change
    public static final String env = "QA";
    public static final String projectName = "TestingFramework";
    public static final String receivingEmail = "jwong@konradgroup.com";
    public static final String fakePhoneNumber = "4161234567";
    public static final String firstName = "Jackson";
    public static final String lastName = "Wong";
    public static final String postalCode = "M1M 1M1";

    //things you probably don't want to change
    public static final String qaUrl = "https://qa.com/";
    public static final String uatUrl = "https://uat.com/";
    public static final String prodUrl = "https://prod.ca/";
    public static int credentialsCount = 20; //this is just a placeholder, this value gets set within setTestExcelData() and will count the rows that exist in the spreadsheet

    //things you should never change unless you're very very very sure
    public static final String testDataExcelFileName = "projectTestData.xlsx";
    public static final String dateTrackerFileName = "dateTracker.csv";
    public static final String testDataInternalPathMAC = "//src/main/java/resources/";
    public static final String testResultsInternalPathMAC = "//src/main/results/";
    public static final String testDataInternalPathWIN = "\\src\\main\\java\\resources\\";;
    public static final String testResultsInternalPathWIN = "\\src\\main\\results\\";;
    public static final String COMMA_DELIMITER = ",";
    public static String testName = "";
}

// To Do:
// - add dependencies so they can be easily loaded
// - add instructions on how to add dependencies
// - before invocation is causing some kind of issue when I'm finishing a test, we're generating a file before I actually want to do so
// - also I need to write out the actual errors when failure happens, right now it's just writing out it failed and which method

// Instructions:
// - add dependencies
// - setup folder structure
// - setup testDataExcelFile
// - explain how the file name generator works
// - explain how the listener works