package io.ankitladdha.covidtracker.dto;

import java.time.ZonedDateTime;

@lombok.Data
public class CovidApiResult {
    private boolean success;
    private Data data;
    private ZonedDateTime lastRefreshed;
    private ZonedDateTime lastOriginUpdate;
}
