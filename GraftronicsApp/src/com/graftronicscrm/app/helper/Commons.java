package com.graftronicscrm.app.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.graftronicscrm.app.pojos.ComplaintDetailDto;

public class Commons {

	private static final String BASE_URL = "http://www.graftronicscrm.com/HelloWebservice/Webservice1.asmx/";
	public static final String LOGIN_URL = BASE_URL + "GetUserInfo";
	public static final String GET_DETAIL_URL = BASE_URL + "Gettblexcustcallregister";
	public static final String GET_BARCODE_DETAIL = BASE_URL + "Gettblamcdetails";
	public static final String SUBMIT_DETAILS = BASE_URL + "Updatetblexcustcallregister";
	public static final String GET_ADMIN_LIST_URL = BASE_URL + "Gettblexcustcallregister_allusers";
	public static final String GET_ADMIN_LIVE_LIST_URL = BASE_URL +  "Gettblexcustcallregister_date";
	public static ArrayList<ComplaintDetailDto> detailListItems = new ArrayList<ComplaintDetailDto>();

	public static int getDifferenceDays(String givendate) {
		int daysdiff = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			Calendar cal = Calendar.getInstance();

			String strDate = sdf.format(cal.getTime());
			Date currentdate = sdf.parse(strDate);
			Date toDate = sdf.parse(givendate);
			long diff = currentdate.getTime() - toDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
			daysdiff = (int) diffDays;
			System.out.println("diffrence is :" + daysdiff + " currentdate :" + currentdate + "toDate :" + toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return daysdiff;
	}

	public static boolean checkValidDate(String oeStartDateStr, String oeEndDateStr,String validatedate) {

		try {
			if (oeStartDateStr.equals("") || oeEndDateStr.equals("")) {
				return false;
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String sd[]=oeStartDateStr.split("/");
				String ed[]=oeEndDateStr.split("/");
				Date min = sdf.parse(sd[1]+"/"+sd[0]+"/"+sd[2]);
				Date max = sdf.parse(ed[1]+"/"+ed[0]+"/"+ed[2]);	
				Date currDt = sdf.parse(validatedate);
				return (currDt.after(min) && currDt.before(max)|| (currDt.equals(max) ||currDt.equals(min)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static String calculateYear(String to, String from) {
		int years = 0;
		int months = 0;
		int days = 0;

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			Date toDate = sdf.parse(to);
			Date fromdate = sdf.parse(from);
			// create calendar object for birth day
			Calendar birthDay = Calendar.getInstance();
			birthDay.setTimeInMillis(fromdate.getTime());
			// create calendar object for current day
			Calendar now = Calendar.getInstance();
			now.setTimeInMillis(toDate.getTime());
			// Get difference between years
			years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
			int currMonth = now.get(Calendar.MONTH) + 1;
			int birthMonth = birthDay.get(Calendar.MONTH) + 1;
			// Get difference between months
			months = currMonth - birthMonth;
			// if month difference is in negative then reduce years by one and
			// calculate the number of months.
			if (months < 0) {
				years--;
				months = 12 - birthMonth + currMonth;
				if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
					months--;
			} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
				years--;
				months = 11;
			}
			// Calculate the days
			if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
				days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
			else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
				int today = now.get(Calendar.DAY_OF_MONTH);
				now.add(Calendar.MONTH, -1);
				days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
			} else {
				days = 0;
				if (months == 12) {
					years++;
					months = 0;
				}
			}
			// Create new Age object

			// +" Years, "+months +" Months, "+days+" days";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(Math.abs(years));
	}
}
