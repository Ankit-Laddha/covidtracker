package io.ankitladdha.covidtracker.service;

import io.ankitladdha.covidtracker.dto.AlertStatus;
import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MyCovidAlertService {

    @Autowired
    private CovidDataProvider covidDataProvider;

    public AlertStatus getAlertAboutState(String state) {
        var data = covidDataProvider.getStateData(state);
        var alertStatus = new AlertStatus();
        alertStatus.setAlertLevel("GREEN");
        if (data == null)
            return alertStatus;

        alertStatus.setSummaryData(data);

        if (data.getTotalConfirmed() < 1000) {
            alertStatus.setAlertLevel("GREEN");
            alertStatus.setMeasuresToBeTaken(Collections.singletonList("Everything is Normal !!"));
        } else if (data.getTotalConfirmed() >= 1000 && data.getTotalConfirmed() < 10000) {
            alertStatus.setAlertLevel("ORANGE");
            alertStatus.setMeasuresToBeTaken(Arrays.asList("Only Essential services are allowed", "List of services that come under essential service"));
        } else {
            alertStatus.setAlertLevel("RED");
            alertStatus.setMeasuresToBeTaken(Arrays.asList("Absolute lock-down", "Only Medical and food services are allowed here"));
        }

        return alertStatus;
    }

    public Summary getOverallSummary() {
        return covidDataProvider.getSummary();
    }

    public List<Regional> getAllStateSummary() {
        return covidDataProvider.getAllStateSummary();
    }
}
