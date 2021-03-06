package com.stratpoint.phunware.homework.base;

import java.util.Locale;

import org.holoeverywhere.app.ListFragment;
import org.holoeverywhere.widget.Toast;

import android.os.Bundle;
import android.util.Log;

import com.stratpoint.phunware.homework.Constants;

public class BaseListFragment extends ListFragment{
	private static String TAG;
	
	@Override
	public void onCreate(Bundle sSavedInstanceState) {
		super.onCreate(sSavedInstanceState);
		TAG = this.getClass().getSimpleName();
	}
	
	/*
	 * Procedural log, for printing sequential flow of program
	 */
	protected void procedure(String message) {
		if (Constants.BUILD.LOG_PROCEDURE && message != null) {
			Log.i(TAG, message);
		}
	}
	
	/*
	 * Data log, for printing values of variables
	 */
	protected void data(String var, String value) {
		if (Constants.BUILD.LOG_DATA)
			Log.e(TAG, "\t" + var.toUpperCase(Locale.US) + ": " + value);
	}
	
	/*
	 * Error log, for printing error and warning messages
	 */
	protected void error(String message) {
		if (Constants.BUILD.LOG_ERROR && message != null) {
			Log.e(TAG, message);
		}
	}
	
	/*
	 * Convenience method for showing toasts (short)
	 */
	protected void toast(String message) {
		if (getActivity() != null)
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * Convenience method for showing toasts (long)
	 */
	protected void toastLong(String message) {
		if (getActivity() != null)
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}
}
