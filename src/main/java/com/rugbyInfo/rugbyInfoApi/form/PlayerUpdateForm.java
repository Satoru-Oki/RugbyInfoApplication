package com.rugbyInfo.rugbyInfoApi.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerUpdateForm {
    private String id;

    private String nationality;

    private String name;

    @Min(value = 100, message = "身長は100から300の間で登録してください")
    @Max(value = 300, message = "身長は100から300の間で登録してください")
    private Integer height;

    @Min(value = 10, message = "体重は10から300の間で登録してください")
    @Max(value = 300, message = "体重は10から300の間で登録してください")
    private Integer weight;

    @JsonProperty("rugby_position")
    private String rugbyPosition;
}
