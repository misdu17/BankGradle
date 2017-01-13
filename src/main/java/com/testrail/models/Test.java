package com.testrail.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zakia on 1/12/17.
 */
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
