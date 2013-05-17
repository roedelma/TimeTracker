package com.foreric.timetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenDetector extends BroadcastReceiver {
	public static boolean onSrceen = true;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub:w
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
			onSrceen = false;
		}else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
			onSrceen = true;
		}
	}		
	
}
	