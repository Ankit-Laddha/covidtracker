package io.ankitladdha.covidtracker.dto;

@lombok.Data
public class Regional {
    private String loc;
    private int confirmedCasesIndian;
    private int confirmedCasesForeign;
    private int discharged;
    private int deaths;
    private int totalConfirmed;
}