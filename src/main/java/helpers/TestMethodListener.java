package helpers;

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
        System.out.println("Begin executing Suite:" + suite.getName());
    }

    // ISuiteListener and will execute, once the Suite is finished
    @Override
    public void onFinish(ISuite suite) {
        System.out.println("After executing Suite:" + suite.getName());
    }

    // ITestListener and will execute before starting of Test set
    @Override
    public void onStart(ITestContext arg0) {
        System.out.println("Begin executing Test:" + arg0.getName());
    }

    // ITestListener and will execute, once the Test set is finished
    @Override
    public void onFinish(ITestContext arg0) {
        System.out.println("Completed executing test:" + arg0.getName());
    }

    // ITestListener and will execute only when the test is pass
    @Override
    public void onTestSuccess(ITestResult arg0) {
        System.out.println("Completed executing test:" + arg0.getName());
    }

    // ITestListener and will execute only on the event of fail test
    @Override
    public void onTestFailure(ITestResult arg0) {
        System.out.println("Test Status: fail");
    }

    // ITestListener and will execute before the main test start (@Test)
    @Override
    public void onTestStart(ITestResult arg0) {
        System.out.println("Test Status:" + arg0.getName());
    }

    // ITestListener and will execute only if any of the main test(@Test) get skipped
    @Override
    public void onTestSkipped(ITestResult arg0) {
        System.out.println("Test Status: fail");
    }

    // IInvokedMethodListener and will execute before every method including
    // @Before @After @Test
    @Override
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        System.out.println("Test Status: fail");
    }

    // IInvokedMethodListener and will execute after every method including
    // @Before @After @Test
    @Override
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        System.out.println("Test Status: fail");
    }

    // ITestListener method and Success Percentage
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Status: fail");
    }
}