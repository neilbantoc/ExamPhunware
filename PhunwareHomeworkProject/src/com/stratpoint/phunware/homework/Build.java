package com.stratpoint.phunware.homework;

/*
 * Build enum for easy switching between environment variables like Base URL's,
 * Social Networking OAuth settings, logging settings, crash and analytics settings, etc.
 */
public enum Build {
	DEV("https://s3.amazonaws.com/jon-hancock-phunware/", true, true, true),
	PROD("https://s3.amazonaws.com/jon-hancock-phunware/", false, false, false);
	
	public String PW_API_BASE_URL;
	public boolean LOG_PROCEDURE;
	public boolean LOG_DATA;
	public boolean LOG_ERROR;
	
	private Build(String apiUrl, boolean logProcedure, boolean logData, boolean logError) {
		PW_API_BASE_URL = apiUrl;
		LOG_PROCEDURE = logProcedure;
		LOG_DATA = logData;
		LOG_ERROR = logError;
	}
}
