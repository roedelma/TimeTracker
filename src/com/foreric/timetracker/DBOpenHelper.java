package com.foreric.timetracker;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper{
//	private static SQLiteDatabase mDatabase;
	public static DBOpenHelper instanceDbOpenHelper;
	public static final String DB_NAME = "TIME_DB";
	public static final String TIME_TABLE_NAME = "TIME_TABLE";
	public static final String TIME_TABLE_DATE = "DATE";
	public static final String TIME_TABLE_APPNAME = "NAME";
	public static final String TIME_TABLE_DAILY_TIME = "DAILYTIME";
	//public static final String TIME_TABLE_WEEKLY_TIME = "MONTHTIME";
	//public static final String TIME_TABLE_TOTAL_TIME = "TOTALTIME";
	
	public static final String NOTE_TABLE_NAME = "NOTE_TABLE";
	public static final String NOTE_DATE = "NOTEDATE";
	public static final String NOTE_NOTE = "NOTE";
	public static final String NOTE_DAY_TOTAL_TIME = "DAYTOTALTIME";
	public static final String NOTE_DAY_TOP_APP = "DAYTOPAPP";
	public static final String NOTE_DAY_TOP_APP_TIME = "TOPAPPTIME";
/*
	public static final String UPDATE_DATE_TABLE_NAME = "UPDATE_INFO";
	public static final String UPDATE_APP_NAME = "APPNAME";
	public static final String LAST_UPDATE_DATE = "DATE";
*/
	public static final String WEEK_TABLE_NAME = "WEEKTABLE";
	public static final String WEEK_APPNAME = "APPNAME";
	public static final String WEEK_TIME = "WEEKTIME";
	public static final String WEEK_WEEK = "WEEK";
	public static final String WEEK_TOTAL_TIME = "TOTALTIME"; // TOTAL TIME on APP(not only WEEKLY!)
	
	private static final int VERSION = 1;
	
	private static final String TIME_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + TIME_TABLE_NAME + " (" 
			+ TIME_TABLE_APPNAME + " TEXT NOT NULL, " 
			+ TIME_TABLE_DATE + " TEXT NOT NULL, " 
			+ TIME_TABLE_DAILY_TIME + " INTEGER NOT NULL)";
	public static final int TIME_TABLE_APPNAME_COLUMN = 0;
	public static final int TIME_TABLE_DATE_COLUMN = 1;
	public static final int TIME_TABLE_TIME_COLUMN = 2;
	
	private static final String NOTE_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + TIME_TABLE_NAME + " (" 
			+ NOTE_DATE + " TEXT NOT NULL, "
			+ NOTE_NOTE + " TEXT NOT NULL, " 
			+ NOTE_DAY_TOTAL_TIME + " INTEGER NOT NULL, "
			+ NOTE_DAY_TOP_APP + " TEXT NOT NULL, "
			+ NOTE_DAY_TOP_APP_TIME + " INTEGER NOT NULL)";
	public static final int NOTE_DATE_COLUMN = 0;
	public static final int NOTE_NOTE_COLUMN = 1;
	public static final int NOTE_DAY_TIME_COLUMN = 2;
	public static final int NOTE_DAY_TOP_APP_COLUMN = 3;
	public static final int NOTE_DAY_TOP_APP_TIME_COLUMN = 4;
	
	private static final String WEEK_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + WEEK_TABLE_NAME + " ("
			+ WEEK_APPNAME + " TEXT NOT NULL, "
			+ WEEK_WEEK + " TEXT NOT NULL, "
			+ WEEK_TIME + " INTEGER NOT NULL," 
			+ WEEK_TOTAL_TIME + " TEXT NOT NULL)";
	public static final int WEEK_APPNAME_COLUMN = 0;
	public static final int WEEK_WEEK_COLUMN = 1;
	public static final int WEEK_TIME_COLUMN = 2;
	public static final int WEEK_TOTAL_TIME_COLUMN = 3;
	/*
	private static final String UPDATE_TABLE_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + UPDATE_DATE_TABLE_NAME + " (" 
			+ UPDATE_APP_NAME + " TEXT NOT NULL, " 
			+ UPDATE_DATE_TABLE_NAME + " TEXT NOT NULL)";
	*/
	private final Context mCtx;
	
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mCtx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//mDatabase = getReadableDatabase();	
		try {
			db.execSQL(TIME_TABLE_CREATE);
			db.execSQL(NOTE_TABLE_CREATE);
			db.execSQL(WEEK_TABLE_CREATE);
		//	db.execSQL(UPDATE_TABLE_CREATE);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(DB_NAME, e.getMessage());
		}
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + WEEK_TABLE_NAME);
		onCreate(db);
	}

}
