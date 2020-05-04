package com.artemoons.covid19info.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class JsonItem {

    @SerializedName(value = "title")
    private String locationName;

    @SerializedName(value = "sick")
    private String confirmed;

    @SerializedName(value = "healed")
    private String recovered;

    @SerializedName(value = "died")
    private String deaths;

    @SerializedName(value = "code")
    private String code;

    @SerializedName(value = "is_city")
    private String isCity;

    @SerializedName(value = "coord_x")
    private String coordX;

    @SerializedName(value = "coord_y")
    private String coordY;

    @SerializedName(value = "sick_incr")
    private String sickInc;

    @SerializedName(value = "healed_incr")
    private String healedInc;

    @SerializedName(value = "died_incr")
    private String diedInc;
}
