package com.artemoons.covid19info.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class JsonItem {

    @SerializedName(value = "location")
    private String locationName;

    @SerializedName(value = "sick")
    private String confirmed;

    @SerializedName(value = "healed")
    private String recovered;

    @SerializedName(value = "died")
    private String deaths;
}
