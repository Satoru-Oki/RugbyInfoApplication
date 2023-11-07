package com.rugbyInfo.rugbyInfoApi.Service;

import com.google.common.util.concurrent.RateLimiter;
import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RugbyPlayerFetchService {

    private final RestTemplate restTemplate;

    private final String API_URL = "https://api.sportradar.us/rugby-union/trial/v3/ja/competitors/%s/profile.json";

    private RateLimiter rateLimiter = RateLimiter.create(0.8);

    @Value("${rugby.api.key}")
    private String API_KEY;

    public RugbyPlayerFetchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RugbyPlayer> getDataFromExternalApi(List<String> competitorIds) {
        List<RugbyPlayer> allPlayers = new ArrayList<>();
        for(String competitorId : competitorIds) {
            rateLimiter.acquire();
            String url = String.format(API_URL, competitorId) + "?api_key=" + API_KEY;

            ResponseEntity<TeamInfo> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<TeamInfo>() {
                    });

            List<RugbyPlayer> players = response.getBody().getPlayers();
            allPlayers.addAll(players);
        }

        return allPlayers;
    }
}
