package com.rugbyInfo.rugbyInfoApi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RugbyPlayer {

    private String id;

    private String nationality;

    private String name;

    private Integer height;

    private Integer weight;

    @JsonProperty("type")
    private String rugbyPosition;

    public RugbyPlayer(String nationality, String name, Integer height, Integer weight, String rugbyPosition) {
        this.nationality = nationality;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.rugbyPosition = rugbyPosition;
    }
}
