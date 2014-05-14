package com.stratpoint.phunware.homework;

import org.holoeverywhere.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;

public class App extends Application{
	public static Context context;
	public static ObjectMapper mapper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		mapper = new ObjectMapper();
	}

}
