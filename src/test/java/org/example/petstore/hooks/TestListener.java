package org.example.petstore.hooks;

import io.qameta.allure.Attachment;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener extends AllureTestNg implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) { }

    @Override
    public void onTestSuccess(ITestResult result) { }

    @Override
    public void onTestFailure(ITestResult result) {
        saveFailureLog(result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) { }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }

    @Override
    public void onStart(ITestContext context) { }

    @Override
    public void onFinish(ITestContext context) { }

    @Attachment(value = "Failure Log", type = "text/plain")
    public static String saveFailureLog(String message) {
        return message;
    }
}
