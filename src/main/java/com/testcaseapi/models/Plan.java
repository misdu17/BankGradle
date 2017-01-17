package com.testcaseapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Plan {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("project_id")
    public int project_id;

    @JsonProperty("milestone_id")
    public int milestone_id;

    @JsonProperty("passed_count")
    public Integer passed_count;

    @JsonProperty("blocked_count")
    public int blocked_count;

    @JsonProperty("untested_count")
    public int untested_count;

    @JsonProperty("retest_count")
    public int retest_count;

    @JsonProperty("failed_count")
    public int failed_count;

    public List<Entry> entries;
}
