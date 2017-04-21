package com.candescent.selenium;

public class Constants {

	public static final String LOGIN_URL = "https://cc-prod.candescenthealth.com/user/login";

	// UI Markers
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String LOGIN_SUBMIT = "input[type=\"submit\"]";

	// 2 Minutes Max
	public static final Integer MAX_WORKLIST_WAIT = 120000;
	// 6 Minutes Max
	public static final Integer MAX_ENCOUNTER_WAIT = 360000;

	public static final String TEST_BASE_URL = "http://cc-staging.candescenthealth.com";
	public static final String PROD_BASE_URL = "http://cc-prod.candescenthealth.com";

	public static final String OUTPUT_LOCATION = "c:\\Selenium\\output.csv";
	public static final String STUDIES_LOCATION = "c:\\Selenium\\studies.csv";

}
