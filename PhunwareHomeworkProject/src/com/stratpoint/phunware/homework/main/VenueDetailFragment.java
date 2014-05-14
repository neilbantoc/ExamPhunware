package com.stratpoint.phunware.homework.main;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.androidquery.AQuery;
import com.stratpoint.phunware.homework.R;
import com.stratpoint.phunware.homework.common.model.ScheduleItem;
import com.stratpoint.phunware.homework.common.model.Venue;

public class VenueDetailFragment extends Fragment{
	
	private Venue mVenue;
	private AQuery aq;
	
	private ShareActionProvider mShareActionProvider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_venue_details, container, false);
	}
	
	@Override
	public void onViewCreated(View view) {
		super.onViewCreated(view);
		aq = new AQuery(view);
		displayVenueDetails();
		updateShareIntent();
	}
	
	public void setAndDisplayVenueDetails(Venue venue){
		setVenue(venue);
		displayVenueDetails();
	}
	
	public void setVenue(Venue venue) {
		mVenue = venue;
		setHasOptionsMenu(true);
		updateShareIntent();
	}
	
	private void displayVenueDetails() {
		if (getView() == null || mVenue == null)
			return;
		
		aq.id(R.id.cover_photo).image(mVenue.getImageUrl()).visibility(mVenue.hasImageUrl() ? View.VISIBLE : View.GONE);
		aq.id(R.id.name).text(mVenue.getName());
		aq.id(R.id.address).text(mVenue.getAddress());
		
		StringBuilder mBuilder = new StringBuilder();
		for (ScheduleItem mItem : mVenue.getSchedule())
			mBuilder.append(mItem.getStartToEndDateFormattedString() + "\n");
		
		aq.id(R.id.schedules).text(mBuilder.toString().trim());
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.fragment_venue_detail, menu);
		
	    MenuItem item = menu.findItem(R.id.action_share);

	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	}
	
	private void updateShareIntent(){
		if (mShareActionProvider != null && getActivity() != null && mVenue != null) {
			mShareActionProvider.setShareIntent(createShareIntentBasedOnCurrentVenue());
		}
	}
	
	private Intent createShareIntentBasedOnCurrentVenue(){
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, mVenue.getName() + " " + mVenue.getAddress());
		sendIntent.setType("text/plain");
		
		return sendIntent;
	}

}
