package com.foreric.timetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class TrackDialogFragment extends DialogFragment{
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final SharedPreferences.Editor editor = getActivity().getPreferences(0).edit();
		
		builder.setMessage(R.string.track_dialog_msg);
		builder.setPositiveButton(R.string.track_dialog_btn_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d("Dialog","Positive Button Clicked");
						editor.putBoolean(TimeTrackerActivity.TRACK_MODE, true);
						editor.commit();
					}
				});
		builder.setNegativeButton(R.string.track_dialog_btn_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
					Log.d("Dialog", "Negtive Button Clicked");
					editor.putBoolean(TimeTrackerActivity.TRACK_MODE, false);
					editor.commit();
			}
		});
		
		return builder.create();
	}
}
