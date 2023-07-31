package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConsumableCategory {
    
    @SerializedName("name") private String name;
    @SerializedName("code") private String code;
    @SerializedName("unit") private String unit;

    public String getPureUnit(){
        return this.unit.replaceAll("[0-9]+", "");
    }

    public boolean isCodeBetween(String start, String end){
        return this.code.compareTo(start) >= 0 && this.code.compareTo(end) <= 0;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this, ConsumableCategory.class);
    }
}
