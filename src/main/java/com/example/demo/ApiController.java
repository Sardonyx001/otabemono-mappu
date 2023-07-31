package com.example.demo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpSession;


@Controller
public class ApiController {
    
    @Cacheable("foodQueryToCodeMap")
    public List<String> getFoodQueryListAsStringList() throws Exception{
        ApiManager api = new ApiManager();
        URL apiGetMetaInfoUrl = api.makeUrlFromSomeParams(
                api.getMetaInfoApiBaseUrl(),
                api.getAppId(),
                api.getStatsDataId()
        );

        // JSON型でAPIへのGETリクエストのレスポンスを取得する
        // データセットのメタ情報
        String jsonMetaDataResponse = api.makeApiRequest(apiGetMetaInfoUrl).replaceAll("@", "");
        // データセットの実際データ
        ApiDataProcessor apiDataProcessor = new ApiDataProcessor();

        HashMap<String, String> foodQueryToCodeMap = apiDataProcessor.getfoodQueryToCodeMap(jsonMetaDataResponse);
        
        return new ArrayList<>(foodQueryToCodeMap.keySet());
    }

    @GetMapping("/apidata")
    public String getApiData(Model model, HttpSession session) throws Exception{
        ApiManager api = new ApiManager();
        String foodQueryCode = "010110001";
        URL apiGetMetaInfoUrl = api.makeUrlFromSomeParams(
                api.getMetaInfoApiBaseUrl(),
                api.getAppId(),
                api.getStatsDataId()
        );
        String _foodQuery = (String) session.getAttribute("food");
        System.out.println("food: {"+_foodQuery+"}");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            // JSON型でAPIへのGETリクエストのレスポンスを取得する
            // データセットのメタ情報
            String jsonMetaDataResponse = api.makeApiRequest(apiGetMetaInfoUrl).replaceAll("@", "");
            // データセットの実際データ
            ApiDataProcessor apiDataProcessor = new ApiDataProcessor();

            // TODO Refactor Data-Fetching to Input Controller and Optimize Data Processing Timing
            // Should probably move this data-fetching to the Input Controller in AppViewController
            // and validate the input before any requests are made
            // Also make the fooQueryToCodeMap data fetching and 
            // processing happen AFTER the user first lands on the home page
            HashMap<String, String> foodQueryToCodeMap = apiDataProcessor.getfoodQueryToCodeMap(jsonMetaDataResponse);
            String _foodQueryCode = foodQueryToCodeMap
                                    .getOrDefault(_foodQuery
                                                    .replaceAll( //Regex to remove anything but Japanese text for input sanitization
                                                        "[^\u3041-\u3093\u30A1-\u30F4\u30FC\u4E00-\u9FA0]+",
                                                        ""),
                                                    foodQueryCode
                                                );
            System.out.println("foodCode: {"+_foodQueryCode+"}");

            URL apiGetStatDataUrl = api.makeUrl(_foodQueryCode);
            System.out.println(apiGetStatDataUrl.toString());
            
            String jsonStatDataResponse = api.makeApiRequest(apiGetStatDataUrl).replaceAll("@", "");        
            HashMap<String, String> areaNameToDataValueMap = apiDataProcessor.getAreaNameToDataValueMap(jsonStatDataResponse); 

            model.addAttribute("jsonData", gson.toJson(areaNameToDataValueMap));
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "apidata";
    }

}
