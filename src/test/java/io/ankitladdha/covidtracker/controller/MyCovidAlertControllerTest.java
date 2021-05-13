package io.ankitladdha.covidtracker.controller;

import io.ankitladdha.covidtracker.dto.AlertStatus;
import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import io.ankitladdha.covidtracker.service.MyCovidAlertService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MyCovidAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyCovidAlertService myCovidAlertService;

    @Test
    @DisplayName("should give green status for not-found state")
    public void getStateData() throws Exception {
        var alertStatus = new AlertStatus();
        alertStatus.setAlertLevel("GREEN");
        Mockito.when(myCovidAlertService.getAlertAboutState(ArgumentMatchers.anyString())).thenReturn(alertStatus);

        mockMvc.perform(get("/india/randomState"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'alertLevel':'GREEN','measuresToBeTaken':null,'summaryData':null}"));
    }

    @Test
    @DisplayName("should give correct status for found state")
    public void getCorrectStateData() throws Exception {
        var alertStatus = new AlertStatus();
        alertStatus.setAlertLevel("RED");
        alertStatus.setMeasuresToBeTaken(Arrays.asList("Absolute lock-down", "Only Medical and food services are allowed here"));
        alertStatus.setSummaryData(null);
        Mockito.when(myCovidAlertService.getAlertAboutState(anyString())).thenReturn(alertStatus);

        mockMvc.perform(get("/india/gujarat"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'alertLevel':'RED','measuresToBeTaken':['Absolute lock-down','Only Medical and food services are allowed here'],'summaryData':null}"));
    }

    @Test
    @DisplayName("should return covid cases summary for INDIA")
    void getSummaryTest() throws Exception {

        Summary sd = new Summary();
        Mockito.when(myCovidAlertService.getOverallSummary()).thenReturn(sd);

        mockMvc.perform(get("/india/summary"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'total':0,'confirmedCasesIndian':0,'confirmedCasesForeign':0,'discharged':0,'deaths':0,'confirmedButLocationUnidentified':0,'updatedTime':null}"));
    }

    @Test
    @DisplayName("should return covid cases summary for ALL states of INDIA")
    void getAllStateSummaryTest() throws Exception {

        List<Regional> sd = Collections.emptyList();
        Mockito.when(myCovidAlertService.getAllStateSummary()).thenReturn(sd);

        mockMvc.perform(get("/india/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}
