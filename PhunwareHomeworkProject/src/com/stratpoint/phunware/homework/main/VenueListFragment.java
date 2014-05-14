package com.stratpoint.phunware.homework.main;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.holoeverywhere.LayoutInflater;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.androidquery.AQuery;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;
import com.github.johnpersano.supertoasts.util.Style;
import com.stratpoint.phunware.homework.App;
import com.stratpoint.phunware.homework.R;
import com.stratpoint.phunware.homework.base.BaseListFragment;
import com.stratpoint.phunware.homework.common.model.Venue;
import com.stratpoint.phunware.homework.networking.GetVenuesCallback;

public class VenueListFragment extends BaseListFragment implements OnItemClickListener, OnScrollListener{
	
	private AQuery aq;
	
	private VenueAdapter mAdapter;
	private OnVenueItemClickListner mListener;
	private int mTopChildIndex = 0, mTopChildOffset = 0;
	
	//Super Card Toast
	private String mRetryString;
	private int mRetryColor;
	private SuperCardToast mLoadingToast;
	
	@Override
	public void onCreate(Bundle sSavedInstanceState) {
		super.onCreate(sSavedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity() instanceof OnVenueItemClickListner)
			mListener = (OnVenueItemClickListner) getActivity();
		
		if (mAdapter != null)
			return;
		
		procedure("Creating adapter and retrieving venues");
		mAdapter = new VenueAdapter(getActivity());
		aq = new AQuery(getActivity());
		
        getVenues();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_venue_list, container, false);
	}
	
	@Override
	public void onViewCreated(View view) {
		super.onViewCreated(view);
		procedure("View created");
		setAdapter();
		getListView().setOnItemClickListener(this);
		getListView().setSelectionFromTop(mTopChildIndex, mTopChildOffset);
		getListView().setOnScrollListener(this);
		showNetworkingStatusToast();
	}
	
	/* NETWORKING */
	private void getVenues(){
		aq.ajax(mGetBarVenuesCallback);
		showLoadingToast();
		mRetryString = null;
		mRetryColor = 0;
	}
	
	private GetVenuesCallback mGetBarVenuesCallback = new GetVenuesCallback(){
		public void onFinish() {
			mLoadingToast.dismiss();
			mLoadingToast = null;
		};
		
    	public void onSuccess(String result, com.androidquery.callback.AjaxStatus status) {
    		Collection<Venue> mVenueItems = getVenueItems(result);
    		setItems(mVenueItems);
    	};
    	
    	public void onNetworkError(com.androidquery.callback.AjaxStatus status) {
    		showRetryCardToast("Network error.", SuperToast.Background.GRAY);
    	};
    	
    	public void onNetworkErrorNoConnection(com.androidquery.callback.AjaxStatus status) {
    		showRetryCardToast("Not connected to the internet.", SuperToast.Background.RED);
    	};
    	
    	private void showRetryCardToast(String retryString, int retryColor){
    		mRetryString = retryString;
    		mRetryColor = retryColor;
    		
    		VenueListFragment.this.showRetryCardToast();
    	}
    };
    
    /*
     * Notifies the user of the current network activity:
     * if mRetryString is not null, then previously there
     * has been a failed networking attempt, so show retry card.
     * if mLoadingToast is not null, then the fragment is currently
     * waiting for a response from the network.
     */
    private void showNetworkingStatusToast(){
    	if (mRetryString != null)
    		showRetryCardToast();
    	else if (mLoadingToast != null)
    		showLoadingToast();
    }
    
    private void showLoadingToast(){
		mLoadingToast = new SuperCardToast(getActivity(), SuperToast.Type.PROGRESS, Style.getStyle(Style.GREEN, SuperToast.Animations.FADE));
		mLoadingToast.setTextSize(SuperToast.TextSize.LARGE);
		mLoadingToast.setText("Loading");
		mLoadingToast.setIndeterminate(true);
		mLoadingToast.show();
	}
    
    private void showRetryCardToast(){
    	if (getActivity() == null || mRetryString == null)
    		return;
    	
    	procedure("Showing Retry Toast");
    	SuperCardToast mToast = new SuperCardToast(getActivity(), SuperToast.Type.BUTTON);
    	mToast.setText(mRetryString);
    	mToast.setAnimations(SuperToast.Animations.FADE);
    	mToast.setBackground(mRetryColor);
    	mToast.setButtonIcon(SuperToast.Icon.Dark.REFRESH, "Retry");
    	mToast.setSwipeToDismiss(true);
    	mToast.setOnClickWrapper(onClickWrapper);
    	mToast.setIndeterminate(true);
    	mToast.show();
    }
    
    private OnClickWrapper onClickWrapper = new OnClickWrapper("supercardtoast", new SuperToast.OnClickListener() {

        @Override
        public void onClick(View view, Parcelable token) {
        	getVenues();
        }

    });
    
    /* DATA PROCESSING */
    private Collection<Venue> getVenueItems(String mJsonString){
    	try {
			return App.mapper.readValue(mJsonString, new TypeReference<List<Venue>>(){});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	public void setItems(Collection<Venue> mItems){
		if (mItems == null)
			return;
		
		procedure("Adding items to adapter");
		mAdapter.clear();
		mAdapter.addAll(mItems);
		mAdapter.notifyDataSetChanged();
		setAdapter();
	}
	
	/*
	 * Set adapter if the following conditions have been met:
	 * 1. Adapter has been created
	 * 2. View has been created
	 * 3. Adapter has been populated with items
	 */
	private void setAdapter(){
		if (mAdapter!= null && mAdapter.getCount() > 0 && getView() != null) {
			procedure("Setting adapter");
			setListAdapter(mAdapter);
		}
	}
	
	/* LIST INTERACTION */
	public static interface OnVenueItemClickListner{
		public void OnVenueItemClick(Venue venue);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		if (mListener != null)
			mListener.OnVenueItemClick(mAdapter.getItem(position));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != OnScrollListener.SCROLL_STATE_IDLE)
			return;
		
		// Keep track of scroll position when user scrolls so that we
		// can restore it even when this fragment gets detached
		mTopChildIndex = getListView().getFirstVisiblePosition();
		View v = getListView().getChildAt(0);
		mTopChildOffset = v == null ? 0 : v.getTop();
	}
}
