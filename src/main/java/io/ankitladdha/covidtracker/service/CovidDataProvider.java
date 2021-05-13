package io.ankitladdha.covidtracker.service;

import io.ankitladdha.covidtracker.dto.CovidApiResult;
import io.ankitladdha.covidtracker.dto.Regional;
import io.ankitladdha.covidtracker.dto.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CovidDataProvider {
    static final String URL = "https://api.rootnet.in/covid19-in/stats/latest";

    @Autowired
    private RestTemplate restTemplate;

    public Regional getStateData(String state) {
        var covidResults = restTemplate.getForObject(URL, CovidApiResult.class);

        if (!covidResults.isSuccess())
            return null;

        return covidResults
                .getData()
                .getRegional()
                .stream()
                .filter(it -> it.getLoc().replaceAll(" ", "").equalsIgnoreCase(state))
                .findFirst().orElse(null);

    }

    public Summary getSummary() {
        var covidResults = restTemplate.getForObject(URL, CovidApiResult.class);

        var summary = covidResults.getData().getSummary();
        summary.setUpdatedTime(covidResults.getLastRefreshed());

        return summary;
    }

    public List<Regional> getAllStateSummary() {
        var covidResults = restTemplate.getForObject(URL, CovidApiResult.class);
        return covidResults.getData().getRegional();
    }
}
