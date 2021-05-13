package io.ankitladdha.covidtracker.controller;


import io.ankitladdha.covidtracker.dto.AlertStatus;
import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import io.ankitladdha.covidtracker.service.MyCovidAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/india/")
public class MyCovidAlertController {

    @Autowired
    private MyCovidAlertService myAlertService;

    @GetMapping("/{state}")
    public AlertStatus getStateData(@PathVariable String state) {
        return myAlertService.getAlertAboutState(state);
    }

    @GetMapping("/all")
    public List<Regional> getAllStateData() {
        return myAlertService.getAllStateSummary();
    }

    @GetMapping("/summary")
    public Summary getSummary() {
        return myAlertService.getOverallSummary();
    }
}
