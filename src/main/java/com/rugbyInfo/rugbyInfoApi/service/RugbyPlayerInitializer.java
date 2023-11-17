package com.rugbyInfo.rugbyInfoApi.service;

import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.mapper.RugbyPlayerMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RugbyPlayerInitializer {

    private final RugbyPlayerFetchService rugbyPlayerFetchService;

    private final RugbyPlayerInfoService rugbyPlayerInfoService;

    private final RugbyPlayerMapper rugbyPlayerMapper;

    public RugbyPlayerInitializer(RugbyPlayerFetchService rugbyPlayerFetchService, RugbyPlayerInfoService rugbyPlayerInfoService, RugbyPlayerMapper rugbyPlayerMapper)
        {
            this.rugbyPlayerFetchService = rugbyPlayerFetchService;
            this.rugbyPlayerInfoService = rugbyPlayerInfoService;
            this.rugbyPlayerMapper = rugbyPlayerMapper;
        }

        @PostConstruct
        public void init () {

            if (isDatabaseEmpty()) {
            List<String> competitorIds = Arrays.asList("sr:competitor:4223", "sr:competitor:4224", "sr:competitor:4225", "sr:competitor:4226", "sr:competitor:4227",
                    "sr:competitor:4228", "sr:competitor:4230", "sr:competitor:4231", "sr:competitor:4232", "sr:competitor:7058", "sr:competitor:7951", "sr:competitor:7057",
                    "sr:competitor:7952", "sr:competitor:7953", "sr:competitor:7954", "sr:competitor:7955", "sr:competitor:7956", "sr:competitor:7957", "sr:competitor:42549",
                    "sr:competitor:393526");

            List<RugbyPlayer> rugbyPlayersList = rugbyPlayerFetchService.getDataFromExternalApi(competitorIds);
            rugbyPlayerInfoService.insertRugbyPlayers(rugbyPlayersList);
        }
}

    private boolean isDatabaseEmpty() {
        return rugbyPlayerMapper.countPlayers() == 0;
    }
}
