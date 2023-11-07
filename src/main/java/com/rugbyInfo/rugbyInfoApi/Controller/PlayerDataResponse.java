package com.rugbyInfo.rugbyInfoApi.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PlayerDataResponse {

    private String nationality;

    private String name;

    private Integer height;

    private Integer weight;

    @JsonProperty("rugby_position")
    private String rugbyPosition;
}
