package io.ankitladdha.covidtracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Data {
    private Summary summary;
    @JsonProperty("unofficial-summary")
    private List<UnofficialSummary> unofficialSummary;
    private List<Regional> regional;
}
