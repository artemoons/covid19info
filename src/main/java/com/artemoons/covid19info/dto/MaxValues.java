package com.artemoons.covid19info.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaxValues {

    @SerializedName(value = "confirmedMaxValue")
    private Long confirmedMaxValue;

    @SerializedName(value = "healedMaxValue")
    private Long healedMaxValue;

    @SerializedName(value = "deathsMaxValue")
    private Long deathsMaxValue;

    @SerializedName(value = "activeMaxValue")
    private Long activeMaxValue;

}
