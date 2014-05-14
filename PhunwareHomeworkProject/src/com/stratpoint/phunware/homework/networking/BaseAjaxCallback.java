package com.stratpoint.phunware.homework.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.stratpoint.phunware.homework.Build;
import com.stratpoint.phunware.homework.Constants;

/*
 * Extension of Ajax Callback that provides automatic logging of
 * relevant data (params, URL) and support for adding get params
 * independent of the URL. The params are kept in a hashmap and
 * are appended when a URL is set 
 */
public class BaseAjaxCallback<T> extends AjaxCallback<T> {
	protected boolean isConnected;
	protected HashMap<String, String> getParams;
	protected String baseUrl;
	
	protected void clearParam() {
		if (params != null) {
			params.clear();
		}
	}

	protected void clearGetParams() {
		if (getParams != null) {
			getParams.clear();
		}
	}

	public BaseAjaxCallback(Class<T> type) {
		super();
		TAG = this.getClass().getSimpleName();
		type(type);
	}

	public void paramGet(String name, String value) {
		if (getParams == null)
			getParams = new HashMap<String, String>();
		getParams.put(name, value);
		if (baseUrl != null)
			url(baseUrl);
	}

	public void removeParamGet(String name) {
		if (getParams != null)
			getParams.remove(name);
		if (baseUrl != null)
			url(baseUrl);
	}

	@Override
	public AjaxCallback<T> url(String url) {
		this.baseUrl = url;
		if (getParams != null) {
			Iterator<String> iterator = getParams.keySet().iterator();

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			while (iterator.hasNext()) {
				String name = iterator.next();
				String value = getParams.get(name);
				params.add(new BasicNameValuePair(name, value));
			}
			url = addParametersToUrl(url, params);
		}
		return super.url(url);
	}

	private static String addParametersToUrl(String url, List<NameValuePair> params) {
		if (!url.endsWith("?"))
			url += "?";

		String paramString = URLEncodedUtils.format(params, "utf-8");

		url += paramString;
		return url;
	}

	@Override
	public AjaxCallback<T> param(String name, Object value) {
		if (DEBUG_DATA) {
			if (params == null) {
				params = new HashMap<String, Object>();
			}
			params.put(name, value);
		}
		return super.param(name, value);
	}
	
	public AjaxCallback<T> removeParam(String name){
		super.params.remove(name);
		return this;
	}

	@Override
	public void callback(String url, T object, AjaxStatus status) {
		log("=================");
		procedure("Callback recieved");
		log(url);
		log("[code]:" + status.getCode() + "[message]:" + status.getMessage() + "[error]:" + status.getError());
		if (object != null)
			data("object", object.toString());
		log("=================");

		onFinish();

		// Unable to transform to JSONObject. Response is not as expected
		if (status.getCode() == AjaxStatus.TRANSFORM_ERROR) {
			onTransformError(object, status);
			onFailure(status);
		}
		// Something is wrong with the connection
		else if (status.getCode() == AjaxStatus.NETWORK_ERROR || status.getCode() != 200) {
			// Connected but error still
			if (isConnected) {
				onNetworkError(status);
			}
			// No internet connection found
			else {
				onNetworkErrorNoConnection(status);
			}
			onFailure(status);
		}
		else
			onSuccess(object, status);
	}

	/**
	 * Callback called when request is finished, regardless of success or
	 * failure. Called before any processing is done on the data
	 */
	public void onFinish() {

	}

	/**
	 * Callback called when request is successful, that is, a response
	 * has been retrieved from the server and
	 * it has been transformed properly to it's intended state.
	 */
	public void onSuccess(T result, AjaxStatus status) {

	}

	/**
	 * Callback called when request has failed. Is called after
	 * onNetworkError/onNetworkErrorNoConnection. Being the more
	 * general failure callback, override this method if it
	 * doesn't matter to you what caused the error to occur. If you
	 * want to handdle different causes of failure, try overriding
	 * onTransformError(), onNetworkError() and onNetworkErrorNoConnection()
	 */
	public void onFailure(AjaxStatus status) {

	}

	/**
	 * Callback called when request was successful (that is, a response
	 * code of 200 was returned) but the response was not as intended, leading
	 * to a transform error. This can be caused by network login pages,
	 * for example, where you expect a JSON-formatted string but are instead
	 * responded by an html page prompting the user to log in to the network.
	 */
	public void onTransformError(T response, AjaxStatus status) {

	}

	/**
	 * Callback called when, in an attempt to access the network,
	 * the device is found to be not connected to a network.
	 */
	public void onNetworkErrorNoConnection(AjaxStatus status) {

	}

	/**
	 * Callback called when network errors such as timeouts occur.
	 */
	public void onNetworkError(AjaxStatus status) {

	}

	@Override
	public void async(Context context) {
		log("=================");
		procedure("ASync (Context)");
		printData();
		log("=================");
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		isConnected = manager.getActiveNetworkInfo() == null ? false : manager.getActiveNetworkInfo().isConnected();
		super.async(context);
	}

	public void printData() {
		boolean tempDebug = DEBUG;
		boolean tempDebugData = DEBUG_DATA;

		DEBUG = DEBUG_DATA = true;

		data("URL", getUrl());
		if (params != null) {
			log("Params");
			Iterator<String> iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				data(key, params.get(key));
			}
		}

		DEBUG = tempDebug;
		DEBUG_DATA = tempDebugData;
	}

	protected static String TAG;

	protected static boolean DEBUG = Constants.BUILD == Build.DEV;
	protected static boolean DEBUG_DATA = DEBUG && Constants.BUILD.LOG_DATA;
	protected static boolean DEBUG_PROCEDURE = DEBUG && Constants.BUILD.LOG_PROCEDURE;

	// Log
	/**
	 * Error log. Will always be shown.
	 */
	protected static void error(String message) {
		Log.e(TAG, message);
	}

	/**
	 * Verbose log.
	 */
	protected static void log(String message) {
		if (DEBUG)
			Log.v(TAG, message);
	}

	/**
	 * Procedural log. Monitor unusual procedures by printing out what your
	 * classes are doing.
	 * 
	 * Can be turned on/off by setting DEBUG_PROCEDURE to true/false
	 */
	protected static void procedure(String message) {
		if (DEBUG_PROCEDURE)
			Log.i(TAG, message);
	}

	/**
	 * Data log. Keep track of the validity of the data your classes are
	 * handling by printing out their values.
	 * 
	 * Can be turned on/off by setting DEBUG_DATA to true/false
	 */
	protected static void data(String var, Object value) {
		if (DEBUG_DATA)
			Log.d(TAG, "  " + var + ": " + value);
	}
}