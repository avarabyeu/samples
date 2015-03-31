package com.github.avarabyeu.samples.guiceng;

import com.google.inject.Injector;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Created by andrey.vorobyov on 3/18/15.
 */
public class GuiceScopeListener extends TestListenerAdapter {

    @Override
    public void onStart(ITestContext testContext) {
        System.out.println("ON START..." + Thread.currentThread().getId());

    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("ON START TEST..." + result.getInstanceName()  + " " + Thread.currentThread().getId());

        Injector injector = (Injector) result.getTestContext().getAttribute("injector");
        ManualScope instance = injector.getInstance(ManualScope.class);
        instance.enter();
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        System.out.println("ON FINISH TEST..." + tr.getInstanceName()  + " " + Thread.currentThread().getId());

        Injector injector = (Injector) tr.getTestContext().getAttribute("injector");
        ManualScope instance = injector.getInstance(ManualScope.class);
        instance.exit();
    }

    @Override
    public void onFinish(ITestContext testContext) {

    }

    @Override
    public void onTestFailure(ITestResult tr) {
        tr.getThrowable().printStackTrace();
    }
}
