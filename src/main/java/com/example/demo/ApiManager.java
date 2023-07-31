package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiManager {
	// ? Maybe make a super class for the Api and 
	// ? take 2 child classes for the statDataApi and the metaInfoApi
	private String statDataApiBaseUrl			= "https://api.e-stat.go.jp/rest/3.0/app/json/getStatsData";
	private String metaInfoApiBaseUrl			= "https://api.e-stat.go.jp/rest/3.0/app/json/getMetaInfo";
	
	private String appId				= "ae3f7461800c55d3fae49fe62e7db799f449b866";
	private String statsDataId			= "0003348235"; 
	private String cdCat01				= "010110001"; 

	// ? There has to be a better way than hardcoding these values
	// ? Maybe abstract them away to an Api model class.
	private String cdCat01From 			= "010110001"; // 食料カテゴリーの初め
	private String cdCat01To 			= "011100070"; // 食料カテゴリーの終わり
	private String cdCat02				= "03"; 
	private String cdTime 				= "2022000000";
	private String lang 				= "J"; 
	private String metaGetFlg 			= "Y";
	private String cntGetFlg 			= "N";
	private String explanationGetFlg	= "Y";
	private String annotationGetFlg 	= "Y";
	private String sectionHeaderFlg 	= "1";
	private String replaceSpChars 		= "0";    
	
	private MultiValueMap<String, String> initParams(){
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appId", appId);
		params.add("statsDataId", statsDataId);
		params.add("cdCat01", cdCat01);
		params.add("cdCat02", cdCat02);
		params.add("cdTime", cdTime);
		params.add("lang", lang);
		params.add("metaGetFlg", metaGetFlg);
		params.add("cntGetFlg", cntGetFlg);
		params.add("explanationGetFlg", explanationGetFlg);
		params.add("annotationGetFlg", annotationGetFlg);
		params.add("sectionHeaderFlg", sectionHeaderFlg);
		params.add("replaceSpChars", replaceSpChars);
		return params;
	}

	public MultiValueMap<String,String> addParams(String key, String value){
		MultiValueMap<String, String> params = initParams();
		params.add(key, value);
		return params;
	}

	public URL makeUrl(String foodQueryCode ) throws MalformedURLException{
		this.setCdCat01(foodQueryCode);
		MultiValueMap<String, String> params = initParams();

		URL url = UriComponentsBuilder
			.fromUriString(statDataApiBaseUrl)
			.queryParams(params)
			.build()
			.toUri()
			.toURL();
		return url;
	}

	public URL makeUrlFromSomeParams(String _apiUrl, String _appId, String _statsDataId) throws MalformedURLException{
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("appId", _appId);
		params.add("statsDataId", _statsDataId);
		params.add("cdCat01From", cdCat01From);
		params.add("cdCat01To", cdCat01To);
	
		URL url = UriComponentsBuilder
			.fromUriString(_apiUrl)
			.queryParams(params)
			.build()
			.toUri()
			.toURL();
		return url;
	}

	@Cacheable("jsonResponse")
	public String makeApiRequest(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString().replaceAll("@", "");
		} else {
			throw new IOException("API request failed with response code: " + responseCode);
		}
	}
}