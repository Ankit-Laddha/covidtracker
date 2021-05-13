package io.ankitladdha.covidtracker.service;

import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyCovidAlertServiceTest {

    @InjectMocks
    private MyCovidAlertService myCovidAlertService;

    @Mock
    private CovidDataProvider covidDataProvider;

    @Test
    public void testGreenState() {
        Regional state = new Regional();
        state.setTotalConfirmed(999);
        when(covidDataProvider.getStateData(anyString())).thenReturn(state);

        var result = myCovidAlertService.getAlertAboutState(anyString());

        assertThat(result.getAlertLevel()).isEqualTo("GREEN");
        assertThat(result.getMeasuresToBeTaken()).isEqualTo(Collections.singletonList("Everything is Normal !!"));
    }

    @Test
    public void testRedState() {
        Regional state = new Regional();
        state.setTotalConfirmed(10000);
        when(covidDataProvider.getStateData(anyString())).thenReturn(state);

        var result = myCovidAlertService.getAlertAboutState(anyString());

        assertThat(result.getAlertLevel()).isEqualTo("RED");
        assertThat(result.getMeasuresToBeTaken()).isEqualTo(Arrays.asList("Absolute lock-down", "Only Medical and food services are allowed here"));
    }

    @Test
    public void testOrangeStateLowerBoundary() {
        Regional state = new Regional();
        state.setTotalConfirmed(1000);
        when(covidDataProvider.getStateData(anyString())).thenReturn(state);

        var result = myCovidAlertService.getAlertAboutState(anyString());

        assertThat(result.getAlertLevel()).isEqualTo("ORANGE");
        assertThat(result.getMeasuresToBeTaken()).isEqualTo(Arrays.asList("Only Essential services are allowed", "List of services that come under essential service"));
    }

    @Test
    public void testOrangeStateUpperBoundary() {
        Regional state = new Regional();
        state.setTotalConfirmed(9999);
        when(covidDataProvider.getStateData(anyString())).thenReturn(state);

        var result = myCovidAlertService.getAlertAboutState(anyString());

        assertThat(result.getAlertLevel()).isEqualTo("ORANGE");
        assertThat(result.getMeasuresToBeTaken()).isEqualTo(Arrays.asList("Only Essential services are allowed", "List of services that come under essential service"));
    }

    @Test
    public void testOverallSummary() {
        var summaryData = new Summary();
        summaryData.setUpdatedTime(ZonedDateTime.now());
        summaryData.setConfirmedButLocationUnidentified(10);
        summaryData.setConfirmedCasesForeign(1);
        summaryData.setConfirmedCasesIndian(1000);
        summaryData.setDischarged(20);
        summaryData.setDeaths(2);
        summaryData.setTotal(1011);

        when(covidDataProvider.getSummary()).thenReturn(summaryData);

        var summaryResult = myCovidAlertService.getOverallSummary();
        assertThat(summaryResult).isEqualTo(summaryData);

    }


    @Test
    public void testAllStateSummary() {
        var state1 = new Regional();
        state1.setLoc("state1");
        state1.setTotalConfirmed(1000);
        state1.setConfirmedCasesIndian(999);
        state1.setConfirmedCasesForeign(1);
        state1.setDeaths(0);
        state1.setDischarged(500);

        var state2 = new Regional();
        state2.setLoc("state2");
        state2.setTotalConfirmed(2000);
        state2.setConfirmedCasesIndian(1999);
        state2.setConfirmedCasesForeign(1);
        state2.setDeaths(0);
        state2.setDischarged(500);

        when(covidDataProvider.getAllStateSummary()).thenReturn(Arrays.asList(state2, state1));

        List<Regional> allStateSummary = myCovidAlertService.getAllStateSummary();
        assertThat(allStateSummary).containsExactlyInAnyOrder(state1, state2);

    }
}
