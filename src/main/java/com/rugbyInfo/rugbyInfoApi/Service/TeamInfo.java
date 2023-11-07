package com.rugbyInfo.rugbyInfoApi.Service;

import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;

import java.util.List;

class TeamInfo {
    private List<RugbyPlayer> players;

    public List<RugbyPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<RugbyPlayer> players) {
        this.players = players;
    }
}
