package com.srm.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.srm.utils.DriverFactory;
import com.srm.utils.ExtentManager;
import com.srm.utils.ScreenshotUtil;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test Passed");
        System.out.println(result.getName() + " PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) { 
        extentTest.get().fail(result.getThrowable());

        try {
            String path = ScreenshotUtil.capture(DriverFactory.getDriver(), result.getName());
            extentTest.get().addScreenCaptureFromPath(path);
        } catch (Exception e) {
            System.out.println("Screenshot capture failed: " + e.getMessage());
        }

        System.out.println(result.getName() + " FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}