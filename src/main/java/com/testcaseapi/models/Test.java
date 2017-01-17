package com.testcaseapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Test {
    @JsonProperty("id")
    public int id;

    @JsonProperty("type_id")
    public int type_id;

    public int custom_auto_status;

    @JsonProperty("run_id")
    public int run_id;

    @JsonProperty("case_id")
    public int case_id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("custom_preconds")
    public String custom_preconds;

    public int getId() {
        return id;
    }
}
