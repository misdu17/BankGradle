package com.testcaseapi.models;



/**
 * Created by Zakia on 1/12/17.
 */
public class AutomationTestResult {
    private String testId;
    private int statusId;
    private String revision;
    private String comment;
    private String elapsed;
    private String dynamicCaseId;
    private String staticCaseId;
    private String testPlanId;

    public AutomationTestResult(Builder builder) {
        testId = builder.testId;
        comment = builder.comment;
        statusId = builder.statusId;
        revision = builder.revision;
        elapsed = builder.elapsed;
        dynamicCaseId = builder.dynamicCaseId;
        staticCaseId = builder.staticCaseId;
        testPlanId = builder.testPlanId;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getRevision() {
        return revision;
    }

    public String getComment() {
        return comment;
    }

    public String getElapsed() {
        return elapsed;
    }

    public String getDynamicCaseId() {
        return dynamicCaseId;
    }

    public void setDynamicCaseId(String dynamicCaseId) {
        this.dynamicCaseId = dynamicCaseId;
    }

    public String getStaticCaseId() {
        return staticCaseId;
    }

    public void setStaticCaseId(String staticCaseId) {
        this.staticCaseId = staticCaseId;
    }

    public String getTestPlanId() {
        return testPlanId;
    }

    public String getTestId() {
        return testId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String testId;
        private int statusId;
        private String revision;
        private String comment;
        private String elapsed;
        private String dynamicCaseId;
        private String staticCaseId;
        private String testPlanId;

        public Builder withTestId(String testId){
            this.testId = testId;
            return this;
        }

        public Builder withStatusId(int statusId){
            this.statusId = statusId;
            return this;
        }

        public Builder withRevision(String revision){
            this.revision = revision;
            return this;
        }

        public Builder withComment(String comment){
            this.comment = comment;
            return this;
        }

        public Builder withElapsed(String elapsed){
            this.elapsed = elapsed;
            return this;
        }

        public Builder withDynamicCaseId(String dynamicCaseId){
            this.dynamicCaseId = dynamicCaseId;
            return this;
        }

        public Builder withStaticCaseId(String staticCaseId){
            this.staticCaseId = staticCaseId;
            return this;
        }

        public Builder withTestPlanId(String testPlanId){
            this.testPlanId = testPlanId;
            return this;
        }

        public AutomationTestResult build(){
            return new AutomationTestResult(this);
        }
    }



}
