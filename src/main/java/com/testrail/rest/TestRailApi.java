package com.testrail.rest;

import com.google.gson.Gson;
import com.syful.framework.annotations.Apitest;
import com.syful.framework.annotations.WebTest;
import com.syful.framework.web.config.Settings;
import com.syful.framework.web.platform.utilities.DateTimeFormatter;
import com.testrail.models.*;
import com.testrail.references.TestRailApiReferences;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.testng.Assert;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by Zakia on 1/12/17.
 */
public class TestRailApi extends Base {
    private static Gson jsonParser = new Gson();
    Settings settings = new Settings();

    public TestRailApi(){
        try {
            AuthInfo = Base64.encodeBase64String(AUTH.getBytes("UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns an existing test plan from TestRail by specified ID
     */
    public Plan getPlan(String testPlanId){
        HttpURLConnection request = createGetRequest(TestRailApiReferences.getPlan, testPlanId);
        String response = makeRawRequest(request);
        Plan plan = jsonParser.fromJson(response, Plan.class);

        if (plan != null) return plan;
        throw new InvalidOperationException("Error occured during Get Plan request");
    }

    /**
     * Returns list of tests for specific test run by run ID
     */
    public Test[] getTests(int runId) {
        HttpURLConnection request;
        try {
            request = createGetRequest(TestRailApiReferences.getTest, runId);
            String response = makeRawRequest(request);
            Test[] tests = jsonParser.fromJson(response, Test[].class);
            if (tests != null) return tests;
            throw new InvalidOperationException("Error occurred during Get Tests request");
        } catch (IOException e){
            e.printStackTrace();
        }
        throw new InvalidOperationException("Tests were not found in current test plan");
    }

    /**
     * Returns list of cases for specific test suite by suite ID
     */
    public List<Case> getCases(int suiteId) throws IOException {
        HttpURLConnection request = createGetRequest(TestRailApiReferences.getCases, String.format("1&suite_id=%s", suiteId));
        String response = makeRawRequest(request);

        List<Case> cases = (List<Case>) jsonParser.fromJson(response, Case.class);
        if (cases != null) return cases;
        throw new InvalidOperationException("Error occurred during Get Cases request");
    }

    public void updateTestStatusInTestPlan(ITestResult testResult){
        try {
            String testPlan = Settings.getTestPlan();
            if (!testPlan.equals("") && !testPlan.equals("0")){
                AutomationTestResult automationTestResult  = parseTestResult(testResult);
                Plan plan = getPlan(testPlan);
                if (plan.entries == null || plan.entries.size() <= 0) return;
                System.out.println("Plan Id: " + plan.id);
                for (Entry entry : plan.entries){
                    for (Run run : entry.runs){
                        Test[] tests = getTests(run.id);
                        for (Test test : tests){
                            String caseId = String.format("C%s", test.case_id);
                            if (caseId.equals(automationTestResult.getStaticCaseId())){
                                automationTestResult.setDynamicCaseId(String.valueOf(test.id));
                                sendTestStatus(automationTestResult);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            System.out.println("Test case was not updated due to an error. Error: " + e.getStackTrace().toString());
        }
    }

    private AutomationTestResult parseTestResult(ITestResult testResult){
        DateTimeFormatter formatter = new DateTimeFormatter();
        String comment;
        int status = 0;

        long duration = testResult.getEndMillis() - testResult.getStartMillis();
        String testDuration = formatter.secondsTimeFormatter(duration);

        if (testResult.getStatus() == 1){
            comment = String.format("Test Passed; Environment: %s; Browser: %s", Settings.getStagingUrl(), settings.getBrowser());
            status = 1;
        } else
            try {
                status = 7;
                comment = String.format("Test Failed; Environment: %s; Browser: %s;"
                        , Settings.getStagingUrl(), settings.getBrowser());
            } catch (NullPointerException e){
                comment = "Test Failed";
            }
        HttpURLConnection request = createGetRequest(settings.getRevision());
        String revision = makeRawRequest(request);

        return AutomationTestResult.newBuilder()
                    .withStaticCaseId(getTestCaseId(testResult))
                    .withStatusId(status)
                    .withComment(comment)
                    .withElapsed(testDuration)
                    .withRevision(revision)
                    .withTestPlanId(Settings.getTestPlan())
                    .build();
    }

    public String getTestCaseId(ITestResult testResult){
        boolean webTest = testResult.getMethod().getConstructorOrMethod()
                                    .getMethod().getAnnotation(WebTest.class) != null;
        if (webTest){
            return testResult.getMethod()
                        .getConstructorOrMethod()
                        .getMethod()
                        .getAnnotation(WebTest.class)
                        .id();
        } else {
            return testResult.getMethod()
                        .getConstructorOrMethod()
                    .getMethod()
                    .getAnnotation(Apitest.class)
                    .id();
        }
    }

    private void sendTestStatus(AutomationTestResult automationTestResult){
        try {
            String body =
                    String.format("{\"status_id\" : \"%s\", \"version\" : \"%s\", \"elapsed\" : \"%s\" , \"comment\" : \"%s\"}",
                            automationTestResult.getStatusId(), automationTestResult.getRevision(), automationTestResult.getElapsed(),
                            automationTestResult.getComment());

            HttpURLConnection postRequest = createPostRequest(TestRailApiReferences.addResult, automationTestResult.getDynamicCaseId(), body);
            String response = makeRawRequest(postRequest);
            Assert.assertTrue(response.contains(automationTestResult.getDynamicCaseId()), "Response does not contain info about update status");
            System.out.println("Sending test case status has passed");
        } catch (Exception e) {
            System.out.println("Sending test case status has failed due to an error. Error: " + e.getMessage());
        }
    }


































}
