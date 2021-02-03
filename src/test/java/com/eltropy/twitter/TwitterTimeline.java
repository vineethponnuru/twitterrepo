package com.eltropy.twitter;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

		/*
		 * System.out.println(TweetData[0]); System.out.println(TweetData[1]);
		 * System.out.println(TweetData[2]); System.out.println(TweetData[3]);
		 * System.out.println(TweetData[4]);
		 */

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
			System.out.println("invoking dtaa1");
			data = BasicMethods.readAllRowsDataFromExcelSheet("Tweettestdata.xlsx", "MostNuberOfTweets");
			System.out.println("invoking dta");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

}
