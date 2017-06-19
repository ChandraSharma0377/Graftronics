package com.graftronicscrm.app.db;

import java.util.ArrayList;

import com.graftronicscrm.app.pojos.ComplaintDetailDto;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelperClass {
	private Context mContext;

	public DataHelperClass(Context con) {
		mContext = con;
	}

	public void Add_USER_DATA(ComplaintDetailDto cpdetails) {
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase SQDB = DBOHC.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBOHC.COLUMN_CALL_ID, cpdetails.getCallID());
		values.put(DBOHC.COLUMN_CUSTOMER_ID, cpdetails.getCustomerID());
		values.put(DBOHC.COLUMN_CALL_TYPE, cpdetails.getCallType());
		values.put(DBOHC.COLUMN_COMPLAINT_DETAILS, cpdetails.getComplaintDetails());
		values.put(DBOHC.COLUMN_CALL_LOGIN_TIME, cpdetails.getCallLoginTime());
		values.put(DBOHC.COLUMN_CALL_LOGIN_DATE, cpdetails.getCallLoginDate());
		values.put(DBOHC.COLUMN_STATUS, cpdetails.getStatus());
		values.put(DBOHC.COLUMN_FEEDBACK, cpdetails.getFeedBack());
		values.put(DBOHC.COLUMN_CONTACT_PERSON, cpdetails.getContactPerson());
		values.put(DBOHC.COLUMN_DESCRIPTION, cpdetails.getDescription());
		values.put(DBOHC.COLUMN_CUSTOMER_NAME, cpdetails.getCustomerName());
		values.put(DBOHC.COLUMN_CUSTOMER_ADDRESS, cpdetails.getCustomerAddress());
		values.put(DBOHC.COLUMN_CUSTOMER_CELLNO, cpdetails.getCustomerCellNo());
		values.put(DBOHC.COLUMN_CUSTOMER_LOCATION, cpdetails.getCustomerLocation());
		values.put(DBOHC.COLUMN_EMP_NAME, cpdetails.getEmpName());
		values.put(DBOHC.COLUMN_ADDITIONAL_1, "");
		values.put(DBOHC.COLUMN_ADDITIONAL_2, "");
		values.put(DBOHC.COLUMN_ADDITIONAL_3, "");

		SQDB.beginTransaction();
		try {
			SQDB.insert(DBOHC.TABLE_USER_DETAIL, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SQDB.setTransactionSuccessful();
		SQDB.endTransaction();

	}

	
	public ArrayList<ComplaintDetailDto> getUserData() {

		{
			DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
			SQLiteDatabase SQDB = DBOHC.getWritableDatabase();
			ArrayList<ComplaintDetailDto> userList = new ArrayList<ComplaintDetailDto>();
			String myQuery = "SELECT  * FROM "+DBOHC.TABLE_USER_DETAIL;
					//+ " ORDER BY NOTIFICATION_ID DESC";
			try {
				Cursor cursor = SQDB.rawQuery(myQuery, null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						do {
							try {
								ComplaintDetailDto cpdetails = new ComplaintDetailDto();
								cpdetails.setCallID(cursor.getString(0));
								cpdetails.setCustomerID(cursor.getString(1));
								cpdetails.setCallType(cursor.getString(2));
								cpdetails.setComplaintDetails(cursor.getString(3));
								cpdetails.setCallLoginTime(cursor.getString(4));
								cpdetails.setCallLoginDate(cursor.getString(5));
								cpdetails.setStatus(cursor.getString(6));
								cpdetails.setFeedBack(cursor.getString(7));
								cpdetails.setContactPerson(cursor.getString(8));
								cpdetails.setDescription(cursor.getString(9));
								cpdetails.setCustomerName(cursor.getString(10));
								cpdetails.setCustomerAddress(cursor.getString(11));
								cpdetails.setCustomerCellNo(cursor.getString(12));
								cpdetails.setCustomerLocation(cursor.getString(13));
								cpdetails.setEmpName(cursor.getString(14));
								userList.add(cpdetails);
							} catch (Exception e) {
								Log.d("DB_EXCEPTION" + "OBJ_NOT : ", e.getMessage());
							}
						} while (cursor.moveToNext());
					}
				}
				cursor.close();
				// SQDB.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return userList;
		}

	}
	public boolean isRecordExist(String CALLID) {
		boolean isexit = false;
		String selectQuery = "SELECT * FROM OBJ_ART WHERE CALL_ID = " + CALLID;
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		isexit = cursor.getCount() > 0 ? true : false;
		if (null != cursor)
			cursor.close();

		return isexit;

	}
	
	public void clearRecord(){
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();
		db.delete(DBOHC.TABLE_USER_DETAIL,null,null);
	    db.close();
	}
}
