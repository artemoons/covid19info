package com.artemoons.covid19info.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class JsonItem {

    @SerializedName(value = "LocationName")
    private String locationName;

    @SerializedName(value = "Lat")
    private String latitude;

    @SerializedName(value = "Lng")
    private String longitude;

    @SerializedName(value = "Observations")
    private String observations;

    @SerializedName(value = "Confirmed")
    private String confirmed;

    @SerializedName(value = "Recovered")
    private String recovered;

    @SerializedName(value = "Deaths")
    private String deaths;

    @SerializedName(value = "IsoCode")
    private String isoCode;

    @SerializedName(value = "New")
    private String isNew;
}
