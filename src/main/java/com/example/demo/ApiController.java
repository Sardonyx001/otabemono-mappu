package com.example.demo;

import java.net.URL;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Controller
public class ApiController {
    
    @GetMapping("/apidata")
    public String getApiData(Model model) throws Exception{
        ApiManager api = new ApiManager();
        String foodQueryCode = "010110001";
        URL apiGetMetaInfoUrl = api.makeUrlFromSomeParams(
                api.getMetaInfoApiBaseUrl(),
                api.getAppId(),
                api.getStatsDataId()
        );
        URL apiGetStatDataUrl = api.makeUrl(foodQueryCode);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            // JSON型でAPIへのGETリクエストのレスポンスを取得する
            // データセットのメタ情報
            String jsonMetaDataResponse = api.makeApiRequest(apiGetMetaInfoUrl);
            // データセットの実際データ
            ApiDataProcessor apiDataProcessor = new ApiDataProcessor();
            HashMap<String, String> foodQueryToCodeMap = apiDataProcessor.getfoodQueryToCodeMap(jsonMetaDataResponse);

            String jsonStatDataResponse = api.makeApiRequest(apiGetStatDataUrl);        
            HashMap<String, String> areaNameToDataValueMap = apiDataProcessor.getAreaNameToDataValueMap(jsonStatDataResponse); 

            model.addAttribute("jsonData", gson.toJson(areaNameToDataValueMap));
            model.addAttribute("jsonData2", gson.toJson(foodQueryToCodeMap));
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "api-result";
    }

}
