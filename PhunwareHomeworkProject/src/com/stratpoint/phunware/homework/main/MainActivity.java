package com.stratpoint.phunware.homework.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.stratpoint.phunware.homework.R;
import com.stratpoint.phunware.homework.base.BaseActivity;
import com.stratpoint.phunware.homework.common.model.Venue;

public class MainActivity extends BaseActivity implements VenueListFragment.OnVenueItemClickListner{
	private static final String TAG_LIST = "list";
	private static final String TAG_DETAIL = "detail";
	
	private VenueListFragment mListFragment;
	private VenueDetailFragment mDetailFragment;
	
	private boolean mIsTwoPane;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
        	mListFragment =  new VenueListFragment();
        	mDetailFragment = new VenueDetailFragment();
        	getSupportFragmentManager()
        		.beginTransaction()
        		.add(R.id.left_pane, mListFragment, TAG_LIST)
        		.add(R.id.right_pane, mDetailFragment, TAG_DETAIL)
        		.commit();
        } else {
        	mListFragment = (VenueListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST);
        	mDetailFragment = (VenueDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL);
        }
        addFragmentsToLayout();
    }
    
    private void addFragmentsToLayout(){
    	mIsTwoPane = getResources().getBoolean(R.bool.isTwoPane);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); 
    	if (mIsTwoPane) {
        	transaction
        		.show(mListFragment)
        		.show(mDetailFragment);
    	} else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
        	getSupportFragmentManager().popBackStackImmediate();
        	transaction
	    		.hide(mListFragment)
	    		.show(mDetailFragment)
	    		.addToBackStack(TAG_DETAIL);
        } else {
        	transaction
	    		.show(mListFragment)
	    		.hide(mDetailFragment);
        }
        transaction.commit();
    }

	@Override
	public void OnVenueItemClick(Venue venue) {
		showDetailFragment(venue);
	}
	
	private void showDetailFragment(Venue venue){
    	if (!mIsTwoPane)
    		getSupportFragmentManager()
    			.beginTransaction()
    			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    			.hide(mListFragment)
    			.show(mDetailFragment)
    			.addToBackStack(TAG_DETAIL)
    			.commit();
    	
    	mDetailFragment.setAndDisplayVenueDetails(venue);
    }
}
