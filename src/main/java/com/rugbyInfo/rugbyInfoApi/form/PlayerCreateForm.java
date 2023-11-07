package com.rugbyInfo.rugbyInfoApi.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerCreateForm {

    @NotBlank
    private String nationality;

    @NotBlank
    private String name;

    @NotNull(message = "入力が必要です")
    @Min(value = 100, message = "身長は100から300の間で登録してください")
    @Max(value = 300, message = "身長は100から300の間で登録してください")
    private Integer height;

    @NotNull(message = "入力が必要です")
    @Min(value = 10, message = "体重は10から300の間で登録してください")
    @Max(value = 300, message = "体重は10から300の間で登録してください")
    private Integer weight;

    @NotBlank
    @JsonProperty("rugby_position")
    private String rugbyPosition;
}
