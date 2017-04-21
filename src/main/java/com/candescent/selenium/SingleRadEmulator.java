package com.candescent.selenium;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.candescent.selenium.util.Output;

public class SingleRadEmulator {

	private HtmlUnitDriver driver;
	private Arguments arguments;
	private Integer index;

	public SingleRadEmulator(Arguments arguments, Integer index) {
		driver = new HtmlUnitDriver();
		this.arguments = arguments;
		this.index = index;
	}

	/*
	 * Main method for running a single radiologist in emulator mode.
	 */
	public void run() throws InterruptedException {
		// Wait a few seconds (randomly) so everything doesn't start at the same
		// time.
		Output.getInstance().write(index, "Random Delay Start");
		Integer randomDelay = ThreadLocalRandom.current().nextInt(1000, 60000);
		Thread.sleep(randomDelay);
		Output.getInstance().write(index, "Random Delay End");

		Output.getInstance().write(index, "Logging In");
		login(driver);

		// Runs in an infinite loop. Will get killed by the master process when
		// time is up.
		while (true) {
			// Gets the next case to view
			Integer studyId = Study.getInstance().getStudyId();
			Integer worklistWait = ThreadLocalRandom.current().nextInt(1000, Constants.MAX_WORKLIST_WAIT);
			Integer productWait = ThreadLocalRandom.current().nextInt(1000, Constants.MAX_ENCOUNTER_WAIT);
			Output.getInstance().write(index, "Run Worklist: " + studyId);
			runWorklistPage(driver, worklistWait, studyId);
			Output.getInstance().write(index, "Run Encounter: " + studyId);
			runEncounterPage(driver, productWait, studyId);
		}
	}

	/*
	 * Initial Login for the radiologist
	 */
	public void login(HtmlUnitDriver driver) {
		driver.get(Constants.LOGIN_URL);

		driver.findElement(By.id(Constants.USERNAME)).clear();
		driver.findElement(By.id(Constants.USERNAME)).sendKeys(arguments.getUsername());
		driver.findElement(By.id(Constants.PASSWORD)).clear();
		driver.findElement(By.id(Constants.PASSWORD)).sendKeys(arguments.getPassword());

		driver.findElement(By.cssSelector(Constants.LOGIN_SUBMIT)).click();
		Output.getInstance().write(index, "Login Successful.");
	}

	/*
	 * Run the encounter page
	 */
	public void runEncounterPage(HtmlUnitDriver driver, Integer waitTimeMs, Integer studyId)
			throws InterruptedException {
		String baseUrl = arguments.getEnvironment().getUrl();
		driver.get(baseUrl + "/api/v1/encounter/" + studyId);
		driver.get(baseUrl + "/case/studies.ajax.php?action=Query%20Study%20Information&cases_id=" + studyId);
		driver.get(baseUrl + "/api/v1/user/preferences");
		driver.get(baseUrl + "/api/v1/user/current");
		driver.get(baseUrl + "/counts.ajax.php?ms=1492701419757&_=1492701419758");
		driver.get(baseUrl + "/api/v1/comparisons/" + studyId);
		driver.get(baseUrl + "/api/v1/worklists/12216");
		driver.get(baseUrl + "/api/v1/resident/config");
		driver.get(baseUrl + "/api/v1/user/license/by-npi/0");
		driver.get(baseUrl + "/api/v1/user/12216/procedure-profile");
		driver.get(baseUrl + "/api/v1/user/privilege/by-npi/0");
		driver.get(baseUrl
				+ "/case/rad_auto_dictate.ajax.php?action=GET_NEXT&current_cases_id=2336814&worklist=personal&adqUserPref=off&type=");
		driver.get(baseUrl + "/api/v1/encounter/" + studyId);
		Output.getInstance().write(index, "Encounter Main Page Complete");

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				driver.get(baseUrl + "/case/rad_auto_dictate.ajax.php?action=GET_NEXT&current_cases_id=" + studyId
						+ "&worklist=personal&adqUserPref=off&type=");
				Output.getInstance().write(index, "Encounter Next Case Check");
			}
		}, 0, 15, TimeUnit.SECONDS);

		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				driver.get(baseUrl + "/counts.ajax.php?ms=1492701465416&_=1492701465417");
				Output.getInstance().write(index, "Encounter Worklist Check");
			}
		}, 0, 60, TimeUnit.SECONDS);

		Thread.sleep(waitTimeMs);

		executor.shutdown();
		Output.getInstance().write(index, "Encounter Complete");

	}

	/*
	 * Run the worklist page
	 */
	public void runWorklistPage(HtmlUnitDriver driver, Integer waitTimeMs, Integer studyId)
			throws InterruptedException {
		String baseUrl = arguments.getEnvironment().getUrl();
		driver.get(baseUrl + "/connect/index.php#/worklist/personal");
		driver.get(baseUrl + "/api/v1/user/current");
		driver.get(baseUrl + "/counts.ajax.php?ms=1492702903513&_=1492702903513");
		driver.get(baseUrl + "/api/v1/worklists/12216");
		driver.get(baseUrl + "/case/manage.ajax.php?action=count_plus_one&worklist_type=");
		Output.getInstance().write(index, "Worklist Main Page Complete");

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				driver.get(baseUrl + "/case/manage.ajax.php?action=count_plus_one&worklist_type=personal");
				Output.getInstance().write(index, "Worklist Get List");
			}
		}, 0, 30, TimeUnit.SECONDS);

		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				driver.get(baseUrl + "/counts.ajax.php?ms=1492701465416&_=1492701465417");
				Output.getInstance().write(index, "Worklist Get Counts");
			}
		}, 0, 60, TimeUnit.SECONDS);

		Thread.sleep(waitTimeMs);

		executor.shutdown();
		Output.getInstance().write(index, "Worklist Complete");

	}

}
