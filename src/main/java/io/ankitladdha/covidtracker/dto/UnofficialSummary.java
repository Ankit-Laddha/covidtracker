package io.ankitladdha.covidtracker.dto;


@lombok.Data
class UnofficialSummary {
    private String source;
    private int total;
    private int recovered;
    private int deaths;
    private int active;
}
