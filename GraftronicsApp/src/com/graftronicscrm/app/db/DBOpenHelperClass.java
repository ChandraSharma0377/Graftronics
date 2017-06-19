package com.graftronicscrm.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelperClass extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "graftonics";
	private static final int DATABASE_VERSION = 1;
	private final String TEXT_TYPE = " TEXT";
	private final String COMMA_SEP = ", ";
	private static DBOpenHelperClass instance;
	public Context context;

	// Table
	public final String TABLE_USER_DETAIL = "USER_DETAILS";

	// Column
	public final String COLUMN_CALL_ID = "CALL_ID";  //PRIMARY KEY
	public final String COLUMN_CUSTOMER_ID = "CUSTOMER_ID";
	public final String COLUMN_CALL_TYPE = "CALL_TYPE";
	public final String COLUMN_COMPLAINT_DETAILS = "COMPLAINT_DETAILS";
	public final String COLUMN_CALL_LOGIN_TIME = "CALL_LOGIN_TIME";

	public final String COLUMN_CALL_LOGIN_DATE = "CALL_LOGIN_DATE";
	public final String COLUMN_STATUS = "STATUS";
	public final String COLUMN_FEEDBACK = "FEEDBACK";
	public final String COLUMN_CONTACT_PERSON = "CONTACT_PERSON";
	public final String COLUMN_DESCRIPTION = "DESCRIPTION";

	public final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
	public final String COLUMN_CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
	public final String COLUMN_CUSTOMER_CELLNO = "CUSTOMER_CELLNO";
	public final String COLUMN_CUSTOMER_LOCATION = "CUSTOMER_LOCATION";
	public final String COLUMN_EMP_NAME = "EMP_NAME";

	public final String COLUMN_ADDITIONAL_1 = "ADDITIONAL_1";
	public final String COLUMN_ADDITIONAL_2 = "ADDITIONAL_2";
	public final String COLUMN_ADDITIONAL_3 = "ADDITIONAL_3";

	private final String CREATE_TABLE_USER_DETAIL = "CREATE TABLE " + TABLE_USER_DETAIL + " ( "
			+ COLUMN_CALL_ID + " TEXT PRIMARY KEY NOT NULL, " 
			+ COLUMN_CUSTOMER_ID+ TEXT_TYPE + COMMA_SEP
			+ COLUMN_CALL_TYPE + TEXT_TYPE + COMMA_SEP 
			+ COLUMN_COMPLAINT_DETAILS + TEXT_TYPE + COMMA_SEP
			
			+ COLUMN_CALL_LOGIN_TIME + TEXT_TYPE + COMMA_SEP
			+ COLUMN_CALL_LOGIN_DATE + TEXT_TYPE + COMMA_SEP
			+ COLUMN_STATUS + TEXT_TYPE + COMMA_SEP
			+ COLUMN_FEEDBACK + TEXT_TYPE + COMMA_SEP
			+ COLUMN_CONTACT_PERSON + TEXT_TYPE + COMMA_SEP
			+ COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP
			
			+ COLUMN_CUSTOMER_NAME + TEXT_TYPE + COMMA_SEP
			+ COLUMN_CUSTOMER_ADDRESS + TEXT_TYPE + COMMA_SEP
			+ COLUMN_CUSTOMER_CELLNO + TEXT_TYPE + COMMA_SEP
			+ COLUMN_CUSTOMER_LOCATION + TEXT_TYPE + COMMA_SEP
			+ COLUMN_EMP_NAME + TEXT_TYPE + COMMA_SEP
			+ COLUMN_ADDITIONAL_1 + TEXT_TYPE + COMMA_SEP
			+ COLUMN_ADDITIONAL_2 + TEXT_TYPE + COMMA_SEP
			+ COLUMN_ADDITIONAL_3 + TEXT_TYPE + " );";

	public static DBOpenHelperClass getSharedObject(Context context) {
		if (instance == null) {
			instance = new DBOpenHelperClass(context);
		}
		instance.context = context;
		return instance;
	}

	public DBOpenHelperClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase sqdb = super.getWritableDatabase();
		return sqdb;
	}

	public SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TABLE_USER_DETAIL);
		} catch (Exception ex) {
			Log.d("DBException", ex.getMessage());
		}

	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
