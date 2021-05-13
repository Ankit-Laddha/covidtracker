package io.ankitladdha.covidtracker.dto;

import java.time.ZonedDateTime;

@lombok.Data
public class Summary {
    private int total;
    private int confirmedCasesIndian;
    private int confirmedCasesForeign;
    private int discharged;
    private int deaths;
    private int confirmedButLocationUnidentified;
    private ZonedDateTime updatedTime;
}
