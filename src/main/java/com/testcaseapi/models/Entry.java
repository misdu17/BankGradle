package com.testcaseapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Entry {
    @JsonProperty("id")
    public String id;

    @JsonProperty("id")
    public int suite_id;

    @JsonProperty("name")
    public String name;

    public List<Run> runs;
}
