package com.testrail.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zakia on 1/12/17.
 */
public class Run {
    @JsonProperty("id")
    public int id;

    @JsonProperty("suite_id")
    public int suite_id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("project_id")
    public int project_id;

    @JsonProperty("milestone_id")
    public int milestone_id;

    @JsonProperty("passed_count")
    public int passed_count;

    @JsonProperty("blocked_count")
    public int blocked_count;

    @JsonProperty("untested_count")
    public int untested_count;

    @JsonProperty("retest_count")
    public int retest_count;

    @JsonProperty("failed_count")
    public int failed_count;

    @JsonProperty("plan_id")
    public int plan_id;

    @JsonProperty("url")
    public String url;
}
