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

    private String jpTextOnlyRegex = "[^\\u3041-\\u3093\\u30A1-\\u30F4\\u30FC\\u4E00-\\u9FA0]+";
    
    public ConsumableCategory() {
    }

    public ConsumableCategory(String name, String code, String unit) {
        this.name = name;
        this.code = code;
        this.unit = unit;
    }

    public String getName(){
        return this.name.replaceAll(jpTextOnlyRegex,"");
    }

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
