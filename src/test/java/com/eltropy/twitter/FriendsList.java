package com.eltropy.twitter;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class FriendsList {
	
	private String barerToken;

	public List<String[]> top10FriendsWithMaxNuberOfFollowers()
			throws JsonMappingException, JsonProcessingException, IOException, JSONException {
		System.out.println("UseCase 6");
		generateToken();
		Map<String, Object> queryParamsMap = new HashMap<String, Object>();
		queryParamsMap.put("screen_name", "twitterdev");
		queryParamsMap.put("skip_status", "false");
		queryParamsMap.put("include_user_entities", "false");
		queryParamsMap.put("count", "100");
		queryParamsMap.put("cursor", -1);
		String token = getBarerToken();
		Response response = given().auth().oauth2(token).queryParams(queryParamsMap).when()
				.get("1.1/followers/list.json");
		String respObject = response.getBody().asString();
		JSONObject jsnobject = new JSONObject(respObject);
		org.json.JSONArray jsonArray = jsnobject.getJSONArray("users");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> data = mapper.readValue(jsonArray.toString(),
				new TypeReference<List<Map<String, Object>>>() {
				});
		List<Map<String, Object>> sortedResponse = ResponseSorter.getSortedList(data, 0, 99, "followers_count");
		List<Map<String, Object>> tope10Records = sortedResponse.subList(0, 10);
		List<String[]> list = new ArrayList<>();
		String[] header = { "ScreenName", "Friends_count", "followers_count" };
		list.add(header);
		for (int i = 0; i < tope10Records.size(); i++) {
			String[] result = { tope10Records.get(i).get("screen_name").toString(),
					tope10Records.get(i).get("friends_count").toString(),
					tope10Records.get(i).get("followers_count").toString() };
			list.add(result);
		}
		return list;
		
		

	}
	
	public void generateToken() {

		RestAssured.baseURI = "https://api.twitter.com/";
		// RestAssured.basePath="1.1/";

		String userName = "YrfBeTHrMUCuSD4kdAMBF69WX";
		String password = "10Xbelb6ftIsnqNVvukPI1BwbL9nn2QnoX0QheetSYo0t32hJ8";

		Map<String, String> listOfParams = new HashMap<String, String>();
		listOfParams.put("grant_type", "client_credentials");

		Response response = given().auth().preemptive().basic(userName, password).when()
				.formParam("grant_type", "client_credentials").post("oauth2/token");
		response.prettyPrint();

		String tokenResponse = response.getBody().asString();

		String access_token = JsonPath.with(tokenResponse).get("access_token");
		System.out.println(access_token);

		setBarerToken(access_token);

	}

	public String getBarerToken() {
		return barerToken;
	}

	public void setBarerToken(String barerToken) {
		this.barerToken = barerToken;
	}


}
