package com.artemoons.covid19info.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonItems {

    @SerializedName(value = "Regions")
    private List<JsonItem> items;
}
