package com.stratpoint.phunware.homework.main;

import org.holoeverywhere.ArrayAdapter;
import org.holoeverywhere.widget.TextView;

import com.stratpoint.phunware.homework.R;
import com.stratpoint.phunware.homework.common.model.Venue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class VenueAdapter extends ArrayAdapter<Venue>{

	public VenueAdapter(Context context) {
		super(context, R.layout.simple_list_item_2);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView = super.getView(position, convertView, parent);
		Venue mVenue = getItem(position);
		((TextView)mView.findViewById(android.R.id.text1)).setText(mVenue.getName());
		((TextView)mView.findViewById(android.R.id.text2)).setText(mVenue.getAddress());
		return mView;
	}

}
