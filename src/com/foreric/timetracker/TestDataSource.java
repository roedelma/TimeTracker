package com.foreric.timetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestDataSource {
	private SQLiteDatabase database;
	private DBOpenHelper dbOpenHelper;
	
	public static TestDataSource testDataSource;
	public static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat weekFmt = new SimpleDateFormat("E");
	public static TestDataSource getInstance(Context context) {
		if (testDataSource == null){
			testDataSource = new TestDataSource(context);
		}
		return testDataSource;
	}
	private TestDataSource(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}
	
	public void open(boolean writeable) throws SQLException{
		if (writeable)
			database = dbOpenHelper.getWritableDatabase();
		else 
			database = dbOpenHelper.getReadableDatabase();
	}
	
	public void close() {
		dbOpenHelper.close();
	}
	
	public void insert(String appname, int interval){
		Date date = new Date();
		String dateStr = dateFmt.format(date);
		String weekStr = weekFmt.format(date);
		int todayTime = getTodayTime(dateStr, appname);
		int[] weekTimes = {0,0}; 
		getWeekTime(weekStr, appname, weekTimes);
		
		if (todayTime > 0){
			todayTime += interval;
			ContentValues values = new ContentValues();
			values.put(DBOpenHelper.TIME_TABLE_DAILY_TIME, todayTime);
			String whereClause = DBOpenHelper.TIME_TABLE_APPNAME + " = ? AND "
					+ DBOpenHelper.TIME_TABLE_APPNAME + " = ?";
			String[] whereArgs = {
					appname,
					dateStr
			};
			database.update(DBOpenHelper.TIME_TABLE_NAME, values, whereClause, whereArgs);
		} else {
			ContentValues values = new ContentValues();
			values.put(DBOpenHelper.TIME_TABLE_APPNAME, appname);
			values.put(DBOpenHelper.TIME_TABLE_DATE, dateStr);
			values.put(DBOpenHelper.TIME_TABLE_DAILY_TIME, interval);
			database.insert(DBOpenHelper.TIME_TABLE_NAME, null, values);
		}
		
		if (weekTimes[1] > 0) {
			weekTimes[0] += interval;
			weekTimes[1] += interval;
			ContentValues values = new ContentValues();
			values.put(DBOpenHelper.WEEK_TIME, weekTimes[0]);
			values.put(DBOpenHelper.WEEK_TOTAL_TIME, weekTimes[1]);
			String whereClause = DBOpenHelper.WEEK_APPNAME + " = ? AND "
					+ DBOpenHelper.WEEK_WEEK + " = ?";
			String[] whereArgs = {
					appname,
					weekStr
			};
			database.update(DBOpenHelper.WEEK_TABLE_NAME, values, whereClause, whereArgs);
			
		}else{
			ContentValues values = new ContentValues();
			values.put(DBOpenHelper.WEEK_APPNAME, appname);
			values.put(DBOpenHelper.WEEK_TIME, interval);
			values.put(DBOpenHelper.WEEK_TOTAL_TIME, interval);
            values.put(DBOpenHelper.WEEK_WEEK,weekStr);
			database.insert(DBOpenHelper.WEEK_TABLE_NAME, null, values);
			
		}
	}
	private void getWeekTime(String weekStr, String appname, int[] times) {
		// TODO Auto-generated method stub
		String[] projection = {
				DBOpenHelper.WEEK_TIME
		};
		String selection = DBOpenHelper.WEEK_APPNAME + " = ? AND " + DBOpenHelper.WEEK_WEEK + " =?";
		String[] selectionArgs = {
				weekStr,
				appname
		};
		
		Cursor c = database.query(DBOpenHelper.WEEK_TABLE_NAME, 
				projection, selection, selectionArgs, null, null, null);
		
		if (c.moveToFirst()){
			try {	
				times[0] = c.getInt(0);
				times[1] = c.getInt(0);
			} catch (Exception e) {
				Log.e("WEEK_TABLE query", e.getMessage());			
			}
		}
	}
	private int getTodayTime(String dateStr, String appname) {
		// TODO Auto-generated method stub
		String[] projection = {
				DBOpenHelper.TIME_TABLE_DAILY_TIME
		};
		
		String selection = DBOpenHelper.TIME_TABLE_APPNAME + " = ? AND " 
				+ DBOpenHelper.TIME_TABLE_DATE + " = ?";
		
		String[] selectionArgs = {
				appname,
				dateStr
		};
		
		Cursor c = database.query(DBOpenHelper.TIME_TABLE_NAME, 
				projection, selection, selectionArgs, null, null, null);
		
		int result = 0;
		
		if (c.moveToFirst()){
			result = c.getInt(0);
		}
		return result;
	}
	
	public Cursor getToday(int num) {
		Date date = new Date();
		String dateStr = dateFmt.format(date);
		String[] projection = {
				DBOpenHelper.TIME_TABLE_APPNAME,
				DBOpenHelper.TIME_TABLE_DAILY_TIME
		};
		
		String selection = DBOpenHelper.TIME_TABLE_DATE + " = ?";
		String[] selectionArgs = {
			dateStr
		};
		
		String limit = (num==0)?" desc":" desc limit " + num;
		Cursor c = database.query(DBOpenHelper.TIME_TABLE_NAME, 
				projection, selection, selectionArgs, null, null, 
				DBOpenHelper.TIME_TABLE_DAILY_TIME + limit);
		return c;
	}
}
