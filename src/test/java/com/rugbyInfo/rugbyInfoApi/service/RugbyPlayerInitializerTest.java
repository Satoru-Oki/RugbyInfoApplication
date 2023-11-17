package com.rugbyInfo.rugbyInfoApi.service;

import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.mapper.RugbyPlayerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RugbyPlayerInitializerTest {

    @Mock
    private RugbyPlayerFetchService rugbyPlayerFetchService;

    @Mock
    private RugbyPlayerInfoService rugbyPlayerInfoService;

    @Mock
    private RugbyPlayerMapper rugbyPlayerMapper;
    @InjectMocks
    private RugbyPlayerInitializer rugbyPlayerInitializer;

    @Test
    public void アプリ起動時にFetchServiceで取得した外部APIのデータをInfoServiceを使用してデータベースに投入できること() {
        when(rugbyPlayerMapper.countPlayers()).thenReturn(0);
        List<String> competitorIds = List.of("sr:competitor:4223", "sr:competitor:4224", "sr:competitor:4225", "sr:competitor:4226", "sr:competitor:4227", "sr:competitor:4228", "sr:competitor:4230", "sr:competitor:4231", "sr:competitor:4232", "sr:competitor:7058", "sr:competitor:7951", "sr:competitor:7057", "sr:competitor:7952", "sr:competitor:7953", "sr:competitor:7954", "sr:competitor:7955", "sr:competitor:7956", "sr:competitor:7957", "sr:competitor:42549", "sr:competitor:393526");
        RugbyPlayer player = new RugbyPlayer("1", "Japan", "Sinki, Gen", 180, 80, "BR");
        List<RugbyPlayer> playersList = List.of(player);

        doReturn(playersList).when(rugbyPlayerFetchService).getDataFromExternalApi(competitorIds);

        rugbyPlayerInitializer.init();

        verify(rugbyPlayerFetchService, times(1)).getDataFromExternalApi(competitorIds);
        verify(rugbyPlayerInfoService, times(1)).insertRugbyPlayers(playersList);
    }
}
