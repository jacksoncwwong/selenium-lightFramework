package helpers;

public class SharedInfo {
  //things you might want to change
  public static String env = "QA";
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

  //things you should never change unless you're very very very sure
  public static final String testDataExcelFileName = "projectTestData.xlsx";
  public static final String dateTrackerFileName = "dateTracker.csv";
  public static final String testDataInternalPathMAC = "//src/main/java/resources/";
  public static final String testResultsInternalPathMAC = "//src/main/results/";
  public static final String testDataInternalPathWIN = "\\src\\main\\java\\resources\\";;
  public static final String testResultsInternalPathWIN = "\\src\\main\\results\\";;
  public static final String COMMA_DELIMITER = ",";
  public static int credentialsCount;
  public static String testName = "";
  public static String currentSponsor = "";
  public static String currentTier = "";
  public static boolean testFailChecker = false;
}