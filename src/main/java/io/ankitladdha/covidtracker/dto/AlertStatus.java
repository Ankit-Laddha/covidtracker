package io.ankitladdha.covidtracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlertStatus {

    private String alertLevel;//RED, GREEN, ORNAGE
    private List<String> measuresToBeTaken;
    private Regional summaryData;

}
