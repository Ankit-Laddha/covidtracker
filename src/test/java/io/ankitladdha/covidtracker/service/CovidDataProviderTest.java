package io.ankitladdha.covidtracker.service;


import io.ankitladdha.covidtracker.dto.CovidApiResult;
import io.ankitladdha.covidtracker.dto.Data;
import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CovidDataProviderTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CovidDataProvider covidDataProvider;

    @Test
    public void testGetCovidData() {
        when(restTemplate.getForObject(anyString(), any())).thenReturn(getCovidApiResult());

        var result = covidDataProvider.getStateData("Gujarat");

        assertAll(
                () -> assertEquals("Gujarat", result.getLoc()),
                () -> assertEquals(1001, result.getTotalConfirmed()),
                () -> assertEquals(1001, result.getDischarged())
        );
    }

    @Test
    public void testNoCovidData() {
        when(restTemplate.getForObject(anyString(), any())).thenReturn(getCovidApiResult());

        var result = covidDataProvider.getStateData("invalid-state");

        assertThat(result).isNull();
    }

    private CovidApiResult getCovidApiResult() {
        var covidApiResult = new CovidApiResult();
        covidApiResult.setSuccess(true);
        covidApiResult.setLastOriginUpdate(ZonedDateTime.now());
        covidApiResult.setLastRefreshed(ZonedDateTime.now());

        var state = new Regional();
        state.setLoc("Gujarat");
        state.setTotalConfirmed(1000);
        state.setDischarged(1000);

        var state2 = new Regional();
        state2.setLoc("Rajasthan");
        state2.setTotalConfirmed(10000);
        state2.setDischarged(9999);

        var data = new Data();
        data.setRegional(Arrays.asList(state, state2));
        covidApiResult.setData(data);
        return covidApiResult;
    }


    private CovidApiResult getCovidApiResultForSummary() {
        var covidApiResult = new CovidApiResult();

        var summary = new Summary();
        summary.setDischarged(1000);

        var data = new Data();
        data.setSummary(summary);

        covidApiResult.setData(data);
        covidApiResult.setLastRefreshed(ZonedDateTime.now());
        return covidApiResult;
    }

    @Test
    public void testSummary() {
        when(restTemplate.getForObject(anyString(), any())).thenReturn(getCovidApiResultForSummary());

        var result = covidDataProvider.getSummary();

        assertAll(
                () -> assertThat(ZonedDateTime.now()).isAfter(result.getUpdatedTime()),
                () -> assertThat(1000).isEqualTo(result.getDischarged())
        );
    }

    @Test
    public void testAllStateSummary() {
        var states = getCovidApiResult();
        when(restTemplate.getForObject(anyString(), any())).thenReturn(states);

        var result = covidDataProvider.getAllStateSummary();

        assertAll(
                () -> assertThat(states.getData().getRegional()).containsExactlyElementsOf(result)
        );
    }
/*
    public List<Regional> getAllStateSummary(){
        var covidResults = restTemplate.getForObject(URL, CovidApiResult.class);
        return covidResults.getData().getRegional();
    }*/
}
