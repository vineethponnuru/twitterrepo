package com.eltropy.twitter;

import static io.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.LoadOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Test
public class Tweets extends BaseTest {

	static String resourcePath = System.getProperty("user.dir") + File.separator + "Excelfiles";

	public void userTweets() {

		System.out.println("UseCase 3");

		String token = getBarerToken();

		Map<String, Object> queryParamsMap = new HashMap<String, Object>();
		queryParamsMap.put("screen_name", "twitterapi");
		queryParamsMap.put("count", "10");

		Response response = given().auth().oauth2(token).queryParams(queryParamsMap).when()
				.get("1.1/statuses/user_timeline.json");

		String respObject = response.getBody().asString();

		HashSet tweetIdsWithOutDuplicate = new HashSet<Integer>();
		List<Long> tweetId = JsonPath.with(respObject).get("id");
		for (Long id : tweetId) {
			tweetIdsWithOutDuplicate.add(id);
		}
		System.out.println(tweetIdsWithOutDuplicate.size());
		System.out.println(tweetIdsWithOutDuplicate);

		Iterator<Integer> id = tweetIdsWithOutDuplicate.iterator();
		while (id.hasNext()) {
			Response getTweetsWithId = given().auth().oauth2(token).queryParam("id", id.next()).when()
					.get("1.1/statuses/show.json");

			System.out.println(getTweetsWithId.prettyPrint());
			System.out.println("===========");

		}

	}

	public void top10MostReTweetsCount() throws Exception {
		System.out.println("UseCase 4");

		String token = getBarerToken();
		Response response = given().auth().oauth2(token).queryParam("screen_name", "twitterapi")
				.queryParam("count", 100).when().get("1.1/statuses/user_timeline.json");

		String respObject = response.getBody().asString();

		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> data = mapper.readValue(respObject, new TypeReference<List<Map<String, Object>>>() {
		});

		List<Map<String, Object>> sortedResponse = ResponseSorter.getSortedList(data, 0, 99, "retweet_count");
		List<Map<String, Object>> tope10Records = sortedResponse.subList(0, 10);

		List<String[]> list = new ArrayList<>();
		String[] header = { "ID", "CreatedDate", "TweetText", "FavoriteCount", "ReTweetsCount" };
		list.add(header);
		for (int i = 0; i < tope10Records.size(); i++) {

			String[] result = { (tope10Records.get(i).get("id") + "-id").toString(),
					tope10Records.get(i).get("created_at").toString(), tope10Records.get(i).get("text").toString(),
					tope10Records.get(i).get("favorite_count").toString(),
					tope10Records.get(i).get("retweet_count").toString() };

			list.add(result);
		}

		try (CSVWriter writer = new CSVWriter(
				new FileWriter(resourcePath + File.separator + "MostNuberOfTweets.csv"))) {
			writer.writeAll(list);
		}

		// Opening CSV Files
		// Creating CSV LoadOptions object
		LoadOptions loadOptions = new LoadOptions(FileFormatType.CSV);
		// Creating an Workbook object with CSV file path and the loadOptions
		// object
		Workbook workbook = new Workbook(resourcePath + File.separator + "MostNuberOfTweets.csv", loadOptions);
		workbook.save(resourcePath + File.separator + "Tweettestdata.xlsx", SaveFormat.XLSX);

		File template = new File("template.html");
		String htmlTemplate = readFromInputStream(getClass().getClassLoader().getResourceAsStream("template.html"));
		htmlTemplate = htmlTemplate.replace("@@tablebody", buildTable(list));
		writeFile(htmlTemplate, resourcePath + File.separator + "top10MostReTweetsCount.html");
	}

	private void writeFile(String fileData, String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
		writer.append(' ');
		writer.append(fileData);
		writer.close();
	}

	private String buildTable(List<String[]> rows) {
		StringBuilder allRows = new StringBuilder(10000);
		for (String[] row : rows) {
			String htmlRow = String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", row[0],
					row[1], row[2], row[3], row[4]);
			allRows.append(htmlRow);
		}
		return allRows.toString();
	}

	private String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	private void top10FriendsWithMostNuberOfFollowers()
			throws JsonMappingException, JsonProcessingException, IOException {
		System.out.println("UseCase 6");

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

		ObjectMapper mapper = new ObjectMapper();
		// mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		List<Map<String, Object>> data = mapper.readValue(respObject, new TypeReference<List<Map<String, Object>>>() {
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
		try (CSVWriter writer = new CSVWriter(
				new FileWriter(resourcePath + "Top10FriendsWithMostNuberOfFollowers.csv"))) {
			writer.writeAll(list);
		}

	}
}
