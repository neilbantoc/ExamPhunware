package com.stratpoint.phunware.homework.networking;

import com.stratpoint.phunware.homework.Constants;

public class GetVenuesCallback extends BaseAjaxCallback<String>{
	private static final String END_POINT = "nflapi-static.json";
	
	public GetVenuesCallback() {
		super(String.class);
		url(Constants.BUILD.PW_API_BASE_URL + END_POINT);
	}

}