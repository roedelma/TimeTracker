package com.foreric.timetracker;

import java.util.Locale;

import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.*;

public class TimeTrackerActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	
	public final static String TRACK_MODE = "TRACKING_MODE";
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_tracker);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
			
	}

	/*
	private void checkTrackable(){
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		settings.edit().putBoolean(TRACK_MODE, false);
		if (!settings.getBoolean(TRACK_MODE, false)){
			Log.d("MainActivity","pop!");
			if (!popUpTrackDialog()){
				TimeTrackerActivity.this.finish();
			}
		}	
	}
	
	public boolean popUpTrackDialog(){
		DialogFragment dialog = new TrackDialogFragment();
		dialog.show(getSupportFragmentManager(), "notice_track");
		return getPreferences(MODE_PRIVATE).getBoolean(TRACK_MODE, false);
	} 
	*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_tracker, menu);
		return true;
	}

	@Override 
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.action_startservice:
			Intent startIntent = new Intent(TimeTrackerActivity.this, GetTimeService.class);
			startService(startIntent);				
			return true;
		case R.id.action_stopservice:
			Intent stopIntent = new Intent(TimeTrackerActivity.this, GetTimeService.class);
			stopService(stopIntent);
			return true;
		case R.id.action_settings:
			
			return true;
		
		}	
		
		return true;
		
	}
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * Including three fragments:
	 * 		Daily Section
	 * 		Monthly Section
	 * 		Notes Section
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private int pos; 
		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView;
			
			Bundle bundle = getArguments();
			pos = bundle.getInt(ARG_SECTION_NUMBER);
			switch (pos) {
			case 2:
				rootView = inflater.inflate(R.layout.monthly_layout, container, false);
				TextView monthly_header_txtView = (TextView)rootView.findViewById(R.id.monthly_header);
				monthly_header_txtView.setText("Monthly Header");
				break;
			case 3:
				rootView = inflater.inflate(R.layout.notes_layout, container, false);
				rootView.findViewById(R.id.notes_button).setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EditText editview = (EditText) ((RelativeLayout)v.getParent()).findViewById(R.id.notes_editview);
						editview.setText("Oops! You clicked the button!");
					}
				});
				break;
			default:
				rootView = inflater.inflate(R.layout.daily_main_layout, container, false);
				TextView daily_header_txtView = (TextView)rootView.findViewById(R.id.daily_header);
				daily_header_txtView.setText("Daily Header");
                ListView listView = (ListView)rootView.findViewById(R.id.daily_listview);
                break;
			}
			return rootView;
		}
	}

}
