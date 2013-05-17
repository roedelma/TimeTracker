package com.foreric.timetracker;


import java.util.List;


import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class GetTimeService extends IntentService{
	Handler handler;
	ActivityManager mActivityManager;
	PackageManager packageManager;
	TestDataSource dataSource;
	PowerManager pManager;
	static int INTERVAL;
	public GetTimeService() {
		super("GetTimeService");
        INTERVAL = 5;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		//dataSource = TestDataSource.getInstance(this.getApplicationContext());
		//dataSource.open(true);
		return START_STICKY;

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		handler = new Handler(Looper.getMainLooper());
		pManager = (PowerManager)GetTimeService.this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
		dataSource = TestDataSource.getInstance(GetTimeService.this);
		dataSource.open(true);
		
		mActivityManager = (ActivityManager)GetTimeService.this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		packageManager = GetTimeService.this.getApplicationContext().getPackageManager();
		while (true){

			synchronized (this) {

				try {
					//if (ScreenDetector.onSrceen == true)
					if (pManager.isScreenOn())
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub

							ComponentName cn1 = mActivityManager.getRunningTasks(1).get(0).topActivity;
							try {
								ApplicationInfo info = packageManager.getApplicationInfo(cn1.getPackageName(), 0);
								if (info!=null){
									CharSequence appname = packageManager.getApplicationLabel(info);
									Toast.makeText(GetTimeService.this.getApplicationContext(), appname, Toast.LENGTH_SHORT).show();
									//dataSource.Insert("1", appname.toString(), 5);
									GetTimeService.this.updateDB(appname, INTERVAL);
								}
							} catch (NameNotFoundException e) {
									// TODO Auto-generated catch block
									Log.e("GetTimeService",e.getMessage());
							}

						}
					});
				} catch (Exception e) {
					Log.e("GetTimeService",e.getMessage());

				}
                try {
                    wait(INTERVAL*1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.e("GetTimeService", e.getMessage());
                }
			}

		}
	}
	
	
	protected void updateDB(final CharSequence appName, final int interval){
		new Thread((new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				dataSource.insert(appName.toString(), interval);
			}
		})).start();
	}
}