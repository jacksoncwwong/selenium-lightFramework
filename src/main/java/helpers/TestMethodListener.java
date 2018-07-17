package helpers;

import com.google.common.base.Throwables;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestMethodListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

    // ISuiteListener and will execute before the Suite start
    @Override
    public void onStart(ISuite suite) {
        BaseTest.writeData(SharedInfo.testName, "", "Begin executing Suite: " + suite.getName());
    }

    // ISuiteListener and will execute, once the Suite is finished
    @Override
    public void onFinish(ISuite suite) {
        BaseTest.writeData(SharedInfo.testName, "", "After executing Suite: " + suite.getName());
    }

    // ITestListener and will execute before starting of Test set
    @Override
    public void onStart(ITestContext arg0) {
        BaseTest.writeData(SharedInfo.testName, "", "Begin executing Test: " + arg0.getName());
    }

    // ITestListener and will execute, once the Test set is finished
    @Override
    public void onFinish(ITestContext arg0) {
        BaseTest.writeData(SharedInfo.testName, "", "Completed executing test: " + arg0.getName());
    }

    // ITestListener and will execute only when the test is pass
    @Override
    public void onTestSuccess(ITestResult arg0) {
        BaseTest.writeData(SharedInfo.testName, "pass", "Completed executing test: " + arg0.getName());
    }

    // ITestListener and will execute only on the event of fail test
    @Override
    public void onTestFailure(ITestResult arg0) {
        BaseTest.writeData(SharedInfo.testName, "fail", "Test Status: " + arg0.getName());
        BaseTest.writeData(SharedInfo.testName, "fail", "Test Context: " + arg0.getTestContext());
        Throwable test = arg0.getThrowable();
        String stackTraceString = Throwables.getStackTraceAsString(test);
        stackTraceString = stackTraceString.replace("\n", "\n,,,");
        BaseTest.writeData(SharedInfo.testName, "fail", "Test Throwable: " + stackTraceString);
    }

    // ITestListener and will execute before the main test start (@Test)
    @Override
    public void onTestStart(ITestResult arg0) {
        BaseTest.writeData(SharedInfo.testName, "", "Test Status: " + arg0.getName());
    }

    // ITestListener and will execute only if any of the main test(@Test) get skipped
    @Override
    public void onTestSkipped(ITestResult arg0) {
        BaseTest.writeData(SharedInfo.testName, "skipped", "Test Status: " + arg0.getName());
        BaseTest.writeData(SharedInfo.testName, "skipped", "Test Context: " + arg0.getTestContext());
        Throwable test = arg0.getThrowable();
        String stackTraceString = Throwables.getStackTraceAsString(test);
        stackTraceString = stackTraceString.replace("\n", "\n,,,");
        BaseTest.writeData(SharedInfo.testName, "skipped", "Test Throwable: " + stackTraceString);
    }

    // IInvokedMethodListener and will execute before every method including
    // @Before @After @Test
    @Override
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        BaseTest.writeData("", "", "Before Invocation of method: " + arg0.getTestMethod().getMethodName());
    }

    // IInvokedMethodListener and will execute after every method including
    // @Before @After @Test
    @Override
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        BaseTest.writeData(SharedInfo.testName, "", "After Invocation of method: " + arg0.getTestMethod().getMethodName());
    }

    // ITestListener method and Success Percentage
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        BaseTest.writeData(SharedInfo.testName, "within percentage", "onTestFailedButWithinSuccessPercentage: " + result.getTestName());
        BaseTest.writeData(SharedInfo.testName, "within percentage", "Test Status: " + result.getName());
        BaseTest.writeData(SharedInfo.testName, "within percentage", "Test Context: " + result.getTestContext());
        Throwable test = result.getThrowable();
        String stackTraceString = Throwables.getStackTraceAsString(test);
        stackTraceString = stackTraceString.replace("\n", "\n,,,");
        BaseTest.writeData(SharedInfo.testName, "within percentage", "Test Throwable: " + stackTraceString);
    }
}