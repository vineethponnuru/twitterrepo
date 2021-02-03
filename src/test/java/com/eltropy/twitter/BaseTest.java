package com.eltropy.twitter;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BaseTest {

	private String barerToken;

	@BeforeTest
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
