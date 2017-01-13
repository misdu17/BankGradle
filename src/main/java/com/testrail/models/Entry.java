package com.testrail.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Zakia on 1/12/17.
 */
public class Entry {
    @JsonProperty("id")
    public String id;

    @JsonProperty("id")
    public int suite_id;

    @JsonProperty("name")
    public String name;

    public List<Run> runs;
}
