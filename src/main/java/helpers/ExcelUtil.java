package helpers;

import org.openqa.selenium.Platform;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExcelUtil {
    //Main Directory of the project
    public static final String currentDir = System.getProperty("user.dir");

    public static String testResultsExcelFileName = null;

    //Location of Test data excel file
    public static String testDataExcelPath = null;
    public static String testResultsFilePath = null;

    //Excel WorkBook
    private static XSSFWorkbook testDataWBook;
    private static XSSFWorkbook testResultsWBook = new XSSFWorkbook();

    //Excel Sheet
    private static XSSFSheet testDataWSheet;
    private static XSSFSheet testResultsWSheet;

    //Excel cell
    private static XSSFCell cell;

    //Excel row
    private static XSSFRow row;

    //Row Number
    public static int rowNumber;

    //Column Number
    public static int columnNumber;

    //Setter and Getters of row and columns
    public static void setRowNumber(int pRowNumber) {
        rowNumber = pRowNumber;
    }

    public static int getRowNumber() {
        return rowNumber;
    }

    public static void setColumnNumber(int pColumnNumber) {
        columnNumber = pColumnNumber;
    }

    public static int getColumnNumber() {
        return columnNumber;
    }

    // The generateFileName() method is used to generate the correct file name for dateTrackerFileName
    // it also manages the dateTracker.csv file, which helps determines the dateTrackerFileName based on version # and date
    public static String dateUsed = "";
    public static int dateUsageTracker = 1;
    public static String[] dataRead = {};
    public static String generateFileName() {
        // Create object of SimpleDateFormat class and decide the format
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-");

        //get current date time with Date()
        Date date = new Date();

        // Now format the date
        String newDate = dateFormat.format(date);

        BufferedReader br = null;
        String line = "";

        //we read the date in the dateTracker.csv and if there's anything written in there we save it to dateUsed and dateUsageTracker
        try {
            br = new BufferedReader(new FileReader(testDataExcelPath + SharedInfo.dateTrackerFileName));
            while((line = br.readLine()) != null) {
                dataRead = (line).split(SharedInfo.COMMA_DELIMITER);
                dateUsed = dataRead[0].replace(String.valueOf((char) 160), " ").trim();
                System.out.println("dateUsed value in loop " + dateUsed);
                dateUsageTracker = Integer.parseInt(dataRead[1]);
                System.out.println("dateUsed and dateUsageTracker values retrieved");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //if newDate is equal to the date being used, that means this is not the first time therefore version number gets increased
        if (newDate.equals(dateUsed)) {
            dateUsageTracker++;
            System.out.println("newDate equals dateUsed");
        }
        else {
            //assuming if they are not equal, then it's a new date, which means version number back to 1
            dateUsed = newDate;
            dateUsageTracker = 1;
            System.out.println("newDate DOES NOT equal to dateUsed");
        }
        System.out.println("newDate is " + newDate);
        System.out.println("dateUsed is " + dateUsed);
        System.out.println("dateUsageTracker is " + dateUsageTracker);

        // short explanation of possible cases in regards to the if statement and it's variables, we basically have 2 main cases with 2 sub-cases, where the main cases are dependant on whether the dates are equal or not:
        // dates are equal
        //      dateUsageTracker = 1 (using existing date second time)
        //      dateUsageTracker > 1 (using existing date > 2 times)
        // dates are not equal
        //      dateUsageTracker = 1 (basically new date but this is first time function was ever called, or last version was also 1)
        //      dateUsageTracker > 1 (basically new date but previous version was > 1)

        //formatting the sequenceNumber string to ensure congruence, I don't anticipate us testing more than 999 times in a day
        String sequenceNumber = String.format("%03d", dateUsageTracker);
        System.out.println("sequence number used for file name is " + sequenceNumber);

        // concatenates the filename
        String generatedFileName = newDate + sequenceNumber + "-" + SharedInfo.env + "-jupiterTesting-" + SharedInfo.testName + ".csv";

        // writes the used date along with the sequence number into dateTracker.csv
        File fileOut = new File(testDataExcelPath + SharedInfo.dateTrackerFileName);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fileOut));
            bw.append(newDate);
            bw.append(SharedInfo.COMMA_DELIMITER);
            bw.append(sequenceNumber);
            System.out.println(bw);
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return generatedFileName;
    }

    // This method helps setup all the files that get read and written to
    // We are mainly dealing with 3 files:
    // - testDataExcelFileName is the name of the file responsible for test data, we only read from this file
    // - testResultsExcelFileName is a csv file we generate every time a test is run, it stores the test results from testing
    // - dateTrackerFileName is a csv file keeping track of date and version number which then gets used for testResultsExcelFileName, this is dealt with in the generateFileName() method
    public static String csvFile = "";
    public static String csvHeader = "";
    public static void setTestExcelData() {
        //MAC or Windows Selection for excel path
        if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
            testDataExcelPath = currentDir + SharedInfo.testDataInternalPathMAC;
            testResultsFilePath = currentDir + SharedInfo.testResultsInternalPathMAC;
        } else if (Platform.getCurrent().toString().contains("WIN")) {
            testDataExcelPath = currentDir + SharedInfo.testDataInternalPathWIN;
            testResultsFilePath = currentDir + SharedInfo.testResultsInternalPathWIN;
        }
        try {
            //setting up access to the testData file, also counting how many test accounts there are
            FileInputStream dataFIS = new FileInputStream(testDataExcelPath + SharedInfo.testDataExcelFileName);
            testDataWBook = new XSSFWorkbook(dataFIS);
            testDataWSheet = testDataWBook.getSheet("Sheet1");
            SharedInfo.credentialsCount = testDataWSheet.getLastRowNum() + 1;
            System.out.println("row count = " + SharedInfo.credentialsCount);

            //setting up the results spreadsheet
            testResultsExcelFileName = generateFileName();
            csvFile = testResultsFilePath + testResultsExcelFileName;
            csvHeader = WriteCsvHeader(new String[]{"Time", "Test Feature", "Status", "Comments"});
        } catch (Exception e) {
            try {
                throw (e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //This method reads the test data from the Excel cell.
    //We are passing row number and column number as parameters.
    //Remember that we start counting from 0s.
    public static String getCellData(int RowNum, int ColNum) {
        try {
            cell = testDataWSheet.getRow(RowNum).getCell(ColNum);
            DataFormatter formatter = new DataFormatter();
            String cellData = formatter.formatCellValue(cell);
            return cellData;
        } catch (Exception e) {
            throw (e);
        }
    }

    //This method takes row number as a parameter and returns the data of given row number.
    public static XSSFRow getRowData(int RowNum) {
        try {
            row = testDataWSheet.getRow(RowNum);
            return row;
        } catch (Exception e) {
            throw (e);
        }
    }

    //below are methods for writing csv files, starting with deciding what the header of the csv file will be
    public static String WriteCsvHeader(String[] headers) {
        StringBuilder csvHeader = new StringBuilder();
        int totalHeaders = headers.length;
        for (int i = 0; i < totalHeaders; i++) {
            //if not last header, append comma after each header
            if (i != totalHeaders - 1) {
                csvHeader.append(headers[i]);
                csvHeader.append(",");
            }
            //if last header, just append the column
            else {
                csvHeader.append(headers[i]);
            }
        }
        csvHeader.append("\n");
        return csvHeader.toString();
    }

    //this method is used to store the test results
    public static StringBuilder csvRecords = new StringBuilder();
    public static void WriteCsvRecords(String[] records) {
        int numberOfColumnsInRecords = records.length;
        for (int i = 0; i < numberOfColumnsInRecords; i++) {
            //if not the last column, append comma after each column
            if (i != numberOfColumnsInRecords - 1) {
                csvRecords.append(records[i]);
                csvRecords.append(",");
            }
            //if last column, just append the column
            else {
                csvRecords.append(records[i]);
            }
        }
        csvRecords.append("\n");
    }

    //this is method that actually writes the header and the results into a csv file
    public static void WriteToFile(String fileContent, String fileName) throws IOException {
        String tempFile = csvFile;
        File file = new File(tempFile);
        // if file does exists, then delete and create a new file
        if (file.exists()) {
            try {
                File newFileName = new File(testResultsFilePath + "backup_" + fileName);
                file.renameTo(newFileName);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(fileContent);
        System.out.println("test results file successfully written");
        bw.close();
        csvRecords = new StringBuilder();
    }
}