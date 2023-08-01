package com.example.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;


public class ApiDataProcessor {
    
    // 複数の市が同じ県に対応するデータに関する値を県ごとに平均をとるためのデータ構造
    static class AreaDataSumCount {
        double sum;
        int count;
    }

    @Getter
    public String jpTextOnlyRegex = "[^\\u3041-\\u3093\\u30A1-\\u30F4\\u30FC\\u4E00-\\u9FA0]+";

    @Setter
    @Getter
    private ConsumableCategory consumableCategoryInfo = new ConsumableCategory();

    public HashMap<String, String> getfoodQueryToCodeMap(String jsonMetadataResponse) {

        HashMap<String,String> foodQueryToCodeMap = new HashMap<String,String>();
        // JSONデータを操作するためオブジェクトへ変換する
        JsonArray classObjectArray = getMetaDataClassObjectInfoArray(jsonMetadataResponse);        
        
        classObjectArray.forEach((JsonElement _classObject) -> {
            JsonObject classObject = _classObject.getAsJsonObject();
            
            if(classObject.get("id").getAsString().equals("cat01")){
                classObject.getAsJsonArray("CLASS")
                .forEach((JsonElement consumableCodeClass) -> {
                    if(isComsumableCategoryClassCodeBetween(consumableCodeClass) ){
                            foodQueryToCodeMap.put(
                                consumableCodeClass
                                    .getAsJsonObject()
                                    .get("name")
                                    .getAsString()
                                    .replaceAll(this.jpTextOnlyRegex,""),
                                consumableCodeClass
                                    .getAsJsonObject()
                                    .get("code")
                                    .getAsString()
                            );  
                    }
                });
            }
        });

        return foodQueryToCodeMap;
    }


    // Use this getConsumableCategory to write 2 functions for getCategoryNameToCodeMap and getCategoryNameToUnitMap
    public List<ConsumableCategory> getConsumableCategoryListFromRange(String jsonMetaDataResponse,String start,String end){
        Gson gson = new Gson();
        List<ConsumableCategory> consumableCategoryList = new LinkedList<ConsumableCategory>();
        
        JsonArray classObjectArray = getMetaDataClassObjectInfoArray(jsonMetaDataResponse);        
        
        classObjectArray.forEach((JsonElement _classObject) -> {
            JsonObject classObject = _classObject.getAsJsonObject();
            if(classObject.get("id").getAsString().equals("cat01")){
                classObject.getAsJsonArray("CLASS")
                .forEach((JsonElement consumableCategoryClass) -> {
                    ConsumableCategory consumableCategory = gson.fromJson(consumableCategoryClass.getAsJsonObject(),ConsumableCategory.class);
                    if(consumableCategory.isCodeBetween(start, end)){
                        consumableCategoryList.add(consumableCategory);
                    }
                });
            }
        });
        return consumableCategoryList;
    }

                    
    private boolean isComsumableCategoryClassCodeBetween(JsonElement consumableCategoryClass) {
        return consumableCategoryClass
            .getAsJsonObject()
            .get("code")
            .getAsString()
            .compareTo("010110001") >= 0 
            && 
            consumableCategoryClass
            .getAsJsonObject()
            .get("code")
            .getAsString()
            .compareTo("011100070") <= 0;
    }

    private JsonArray getMetaDataClassObjectInfoArray(String jsonMetadataResponse) {
        JsonArray classObjectInfoArray = JsonParser
            .parseString(jsonMetadataResponse)
            .getAsJsonObject()
            .getAsJsonObject("GET_META_INFO")
            .getAsJsonObject("METADATA_INF")
            .getAsJsonObject("CLASS_INF")
            .getAsJsonArray("CLASS_OBJ");
        return classObjectInfoArray;
    }
    

    public HashMap<String, String> getAreaNameToDataValueMap(String jsonResponse) throws Exception {
        Gson gson = new Gson();
        HashMap<String, String> areaNameToDataValueMap = new HashMap<String,String>();
        try {
            // JSONデータを操作するためオブジェクトへ変換する
            JsonObject responseRootObject = getRootContainer(jsonResponse);
            
            handleResponseCodes(responseRootObject);
            
            JsonArray statDataValueArray    = getStatDataValueArray  (responseRootObject);
            JsonArray classObjectInfoArray  = getClassObjectInfoArray(responseRootObject);

            classObjectInfoArray.forEach((JsonElement _classObject) -> {
                JsonObject classObject = _classObject.getAsJsonObject();
                if(classObject.get("id").getAsString().equals("cat01")){
                    JsonObject consumableCategoryClass = classObject.get("CLASS").getAsJsonObject();
                    this.setConsumableCategoryInfo(gson.fromJson(consumableCategoryClass,ConsumableCategory.class));
                }
            });

            JsonArray areaClassInfoArray = getAreaClassInfoFrom(classObjectInfoArray);
            HashMap<String, String> areaCodeToNamesMap = constructAreaCodeToNamesMapFrom(areaClassInfoArray);

            // このデータセットでは、県道府県ではなく、市区分になっている。
            HashMap<String, String> cityNameToDataValueMap = makeCityNameToDataValueMap(
                areaCodeToNamesMap,
                statDataValueArray);

            // ? Everything after this is to be refactored            
            HashMap<String, String> cityToPrefectureMapping = getCityToPrefectureMapping();
            // using area and prefecture interchangibly
            HashMap<String,AreaDataSumCount> areaToAreaDataSumCountMap = new HashMap<>();
            
            // 各市の値を県に蓄積する。
            cityNameToDataValueMap.forEach( (String city,String _value) -> {
                double value = Double.parseDouble(_value);
                String prefecture = cityToPrefectureMapping.getOrDefault(city, city);
                areaToAreaDataSumCountMap.putIfAbsent(prefecture, new AreaDataSumCount());
                AreaDataSumCount areaDataSumCount = areaToAreaDataSumCountMap.get(prefecture);
                areaDataSumCount.sum += value;
                areaDataSumCount.count++;
            });

            // 各都道府県の平均を計算し、最終結果の配列を作成する。
            areaToAreaDataSumCountMap.forEach((String area,AreaDataSumCount sumCount) -> {
                Double average = sumCount.sum / sumCount.count;
                areaNameToDataValueMap.put(area, average.toString() );
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaNameToDataValueMap;
    }

    // TODO Improve Safety by Comparing with Actual API Responses and Fetching City Data from e-stat.go.jp
    //   ? このように実際のAPI応答と比較せずに利用するのが危険かと
    //  ? 今度ちゃんとe-stat.go.jpのメタデータ情報APIから、地域区別の市を取得すべき。
    private HashMap<String, String> getCityToPrefectureMapping() {
        HashMap<String, String> cityToPrefectureMapping = new HashMap<>();
        cityToPrefectureMapping.put("金沢市", "石川");
        cityToPrefectureMapping.put("宮崎市", "宮崎");
        cityToPrefectureMapping.put("相模原市", "神奈川");
        cityToPrefectureMapping.put("福岡市", "福岡");
        cityToPrefectureMapping.put("堺市", "大阪");
        cityToPrefectureMapping.put("浜松市", "静岡");
        cityToPrefectureMapping.put("秋田市", "秋田");
        cityToPrefectureMapping.put("福井市", "福井");
        cityToPrefectureMapping.put("松江市", "島根");
        cityToPrefectureMapping.put("那覇市", "沖縄");
        cityToPrefectureMapping.put("千葉市", "千葉");
        cityToPrefectureMapping.put("岐阜市", "岐阜");
        cityToPrefectureMapping.put("仙台市", "宮城");
        cityToPrefectureMapping.put("富山市", "富山");
        cityToPrefectureMapping.put("大阪市", "大阪");
        cityToPrefectureMapping.put("全国", "全国");
        cityToPrefectureMapping.put("佐賀市", "佐賀");
        cityToPrefectureMapping.put("札幌市", "北海道");
        cityToPrefectureMapping.put("宇都宮市", "栃木");
        cityToPrefectureMapping.put("名古屋市", "愛知");
        cityToPrefectureMapping.put("さいたま市", "埼玉");
        cityToPrefectureMapping.put("新潟市", "新潟");
        cityToPrefectureMapping.put("大津市", "滋賀");
        cityToPrefectureMapping.put("川崎市", "神奈川");
        cityToPrefectureMapping.put("松山市", "愛媛");
        cityToPrefectureMapping.put("高知市", "高知");
        cityToPrefectureMapping.put("鳥取市", "鳥取");
        cityToPrefectureMapping.put("福島市", "福島");
        cityToPrefectureMapping.put("横浜市", "神奈川");
        cityToPrefectureMapping.put("徳島市", "徳島");
        cityToPrefectureMapping.put("高松市", "香川");
        cityToPrefectureMapping.put("盛岡市", "岩手");
        cityToPrefectureMapping.put("山口市", "山口");
        cityToPrefectureMapping.put("長崎市", "長崎");
        cityToPrefectureMapping.put("熊本市", "熊本");
        cityToPrefectureMapping.put("静岡市", "静岡");
        cityToPrefectureMapping.put("甲府市", "山梨");
        cityToPrefectureMapping.put("水戸市", "茨城");
        cityToPrefectureMapping.put("広島市", "広島");
        cityToPrefectureMapping.put("青森市", "青森");
        cityToPrefectureMapping.put("奈良市", "奈良");
        cityToPrefectureMapping.put("前橋市", "群馬");
        cityToPrefectureMapping.put("長野市", "長野");
        cityToPrefectureMapping.put("京都市", "京都");
        cityToPrefectureMapping.put("東京都区部", "東京");
        cityToPrefectureMapping.put("津市", "三重");
        cityToPrefectureMapping.put("鹿児島市", "鹿児島");
        cityToPrefectureMapping.put("岡山市", "岡山");
        cityToPrefectureMapping.put("山形市", "山形");
        cityToPrefectureMapping.put("神戸市", "兵庫");
        cityToPrefectureMapping.put("大分市", "大分");
        cityToPrefectureMapping.put("和歌山市", "和歌山");
        cityToPrefectureMapping.put("北九州市", "福岡");
        return cityToPrefectureMapping;
    }

    private JsonObject getRootContainer(String jsonResponse) {
        JsonObject responseRootContainer = JsonParser
            .parseString(jsonResponse)
            .getAsJsonObject()
            .getAsJsonObject("GET_STATS_DATA");
        return responseRootContainer;
    }

    private JsonArray getClassObjectInfoArray(JsonObject responseRootContainer) {
        return responseRootContainer
            .getAsJsonObject("STATISTICAL_DATA")
            .getAsJsonObject("CLASS_INF")
            .getAsJsonArray("CLASS_OBJ");
    }

    private JsonArray getAreaClassInfoFrom(JsonArray classObjectInfoContainer) {
        JsonArray areaClassInfoContainer = new JsonArray();
        for(JsonElement classInfoObject: classObjectInfoContainer){
            if(classInfoObject.getAsJsonObject().get("id").getAsString().equals("area")){
                areaClassInfoContainer = classInfoObject.getAsJsonObject().getAsJsonArray("CLASS");    
            }
        }
        return areaClassInfoContainer;
    }

    private HashMap<String, String> makeCityNameToDataValueMap(
            HashMap<String, String> areaCodeToNamesMap,
            JsonArray statDataValueContainer) {
        HashMap<String,String> cityNameToDataValueMap = new HashMap<String,String>();
        for(JsonElement value: statDataValueContainer){
            String regionCode = value.getAsJsonObject().get("area").getAsString();
            cityNameToDataValueMap.put(
                areaCodeToNamesMap.get(regionCode),
                value.getAsJsonObject().get("$").getAsString());
        }
        return cityNameToDataValueMap;
    }

    private JsonArray getStatDataValueArray(JsonObject responseRootContainer) {
        JsonArray statDataValueContainer = responseRootContainer
            .getAsJsonObject("STATISTICAL_DATA")
            .getAsJsonObject("DATA_INF")
            .getAsJsonArray("VALUE");
        return statDataValueContainer;
    }

    private HashMap<String, String> constructAreaCodeToNamesMapFrom(JsonArray areaClassInfoContainer) {
        HashMap<String,String> areaCodeToNamesMap = new HashMap<String,String>();
        for(JsonElement areaClassInfo: areaClassInfoContainer){
            areaCodeToNamesMap.put(
                areaClassInfo
                    .getAsJsonObject()
                    .get("code")
                    .getAsString(),
                areaClassInfo
                    .getAsJsonObject()
                    .get("name")
                    .getAsString()
                    .replaceAll("[0-9 ]", ""));
        }
        return areaCodeToNamesMap;
    }

    private void handleResponseCodes(JsonObject responseRootContainer) throws Exception {
        JsonObject responseResultContainer = responseRootContainer.getAsJsonObject("RESULT");
        if(responseResultContainer.get("STATUS").getAsInt() != 0){
            throw new Exception(responseResultContainer.get("ERROR_MSG").getAsString());
        }
    }

}
