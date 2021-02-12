package com.eltropy.twitter;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Test
public class TwitterTimeline extends BaseClass {

	static WebDriver driver;

	@BeforeMethod
	public void init() {
		driver = BaseClass.driverSetup();

	}

	 @Test(dataProvider = "Tweetdata")
	public void test1(String TweetData[]) throws IOException, InterruptedException {

		BasicMethods basicMethods = new BasicMethods();

		/*
		 * List<String> al = new ArrayList<String>(); for (int i = 0; i > al.size() - 1;
		 * i++) {
		 */
		// String url = "https://twitter.com/" + al.get(i);

		driver.get("https://twitter.com/TwitterAPI");
		driver.manage().window().maximize();
		Thread.sleep(10000);

		basicMethods.screenshot(driver);

		String[] tweetid = TweetData[0].split("-");
		String tweetiduri = tweetid[0];

		String navigateTweeturl = "https://twitter.com/TwitterAPI/status/" + tweetiduri;
		driver.get(navigateTweeturl);
		basicMethods.screenshot(driver);
		Thread.sleep(10000);
		Thread.sleep(10000);
		Thread.sleep(10000);

		String tweetdata = driver.findElement(By.xpath("//div[@lang='en']")).getText();
		String retweetcount = driver.findElement(By.xpath(
				"/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/section[1]/div[1]/div[1]/div[1]/div[1]/div[1]/article[1]/div[1]/div[1]/div[1]/div[3]/div[4]/div[1]/div[1]/div[1]/a[1]/div[1]/span[1]/span[1]"))
				.getText();
		String favcount = driver.findElement(By.xpath(
				"/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/section[1]/div[1]/div[1]/div[1]/div[1]/div[1]/article[1]/div[1]/div[1]/div[1]/div[3]/div[4]/div[1]/div[3]/div[1]/a[1]/div[1]/span[1]/span[1]"))
				.getText();

		if (TweetData[4].equalsIgnoreCase(retweetcount)) {
			System.out.println("Retweet count same in UI and API cal");
		} else {
			System.out.println("Having diff count in UI and API cal");
		}

		if (TweetData[3].equalsIgnoreCase(favcount)) {
			System.out.println("Favourite count same in UI and API cal");
		} else {
			System.out.println("Having diff favourite count in UI and API cal");
		}

		if (TweetData[1].equalsIgnoreCase(tweetdata)) {
			System.out.println("Tweet data same in UI and API cal");
		} else {
			System.out.println("Having diff Tweet data in UI and API cal");
		}

		driver.quit();
	}

	@DataProvider(name = "Tweetdata")
	public static Object[][] tweetData() {
		// The number of times data is repeated, test will be executed the same no. of
		// times
		Object[][] data = null;
		try {
			data = BasicMethods.readAllRowsDataFromExcelSheet("Tweettestdata.xlsx", "Sheet1");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	@Test
	public void test3()
			throws JsonMappingException, JsonProcessingException, IOException, JSONException, InterruptedException {


		FriendsList fl = new FriendsList();
		List<String[]> s = fl.top10FriendsWithMaxNuberOfFollowers();

		System.out.println(s.size());

		int rn = randomNumber(s.size());

		String[] t = null;

		if (s.size() > 0) {

			t = s.get(rn);

			System.out.println(t[0]);
		}

		driver.get("https://twitter.com/login");

		Thread.sleep(10000);
		driver.findElement(By.name("session[username_or_email]")).sendKeys("vineeth22528039");
		driver.findElement(By.name("session[password]")).sendKeys("vineeth@123");

		driver.findElement(By.xpath("//span[@class='css-901oao css-16my406 css-bfa6kz r-poiln3 r-bcqeeo r-qvutc0']"))
				.click();
		driver.manage().window().maximize();
		Thread.sleep(10000);

		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(t[0]);

		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(Keys.ENTER);

		Thread.sleep(5000);

		driver.findElement(By.partialLinkText(t[0])).click();

		Thread.sleep(5000);

		driver.findElement(By.xpath("//span[contains(text(),'Following')]")).click();

		Thread.sleep(5000);

		List<WebElement> link = driver.findElements(By.xpath(
				"//div[@class='css-901oao r-18jsvk2 r-18u37iz r-1q142lx r-1qd0xha r-a023e6 r-16dba41 r-ad9z0x r-bcqeeo r-qvutc0']"));

		System.out.println("Count of verified accounts: " + link.size());

		
	}

	public int randomNumber(int number) {
		Random rand = new Random();
		return rand.nextInt(number);

	}

	@DataProvider(name = "friendsData")
	public static Object[][] friendsData() {
		// The number of times data is repeated, test will be executed the same no. of
		// times
		Object[][] data = null;
		try {
			data = BasicMethods.readAllRowsDataFromExcelSheet("Friendslistdata.xlsx",
					"Top10FriendsWithMaxNuberOfFollo");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

}
