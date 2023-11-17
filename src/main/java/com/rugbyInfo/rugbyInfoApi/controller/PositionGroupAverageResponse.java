package com.rugbyInfo.rugbyInfoApi.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PositionGroupAverageResponse {

    private String nationality;
    private String positionGroup;
    private Integer averageHeight;
    private Integer averageWeight;
}
