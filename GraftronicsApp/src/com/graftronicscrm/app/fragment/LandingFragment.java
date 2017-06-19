
package com.graftronicscrm.app.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.adapters.LandingListAdapter;
import com.graftronicscrm.app.asynctask.AsyncProcess;
import com.graftronicscrm.app.db.DataHelperClass;
import com.graftronicscrm.app.helper.Commons;
import com.graftronicscrm.app.helper.ShowAlertInformation;
import com.graftronicscrm.app.main.MainActivity;
import com.graftronicscrm.app.pojos.ComplaintDetailDto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LandingFragment extends Fragment implements OnClickListener {

	private ListView listview;
	private LandingListAdapter adapter;
	private TextView tv_msg;
	private GetDetailTask gdt;
	private DataHelperClass DHC;
	private String type = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.lay_landing, container, false);
		listview = (ListView) view.findViewById(R.id.listview);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);

		DHC = new DataHelperClass(getActivity());
		update();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.getMainScreenActivity().showHomeIcon(false);
		MainActivity.getMainScreenActivity().tv_status_a.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_p.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_c.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_h.setOnClickListener(this);
		MainActivity.getMainScreenActivity().btn_search.setOnClickListener(this);
		MainActivity.getMainScreenActivity().btn_refresh.setOnClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	private void update() {

		Commons.detailListItems.clear();

		if (MainActivity.getNetworkHelper().isOnline()) {
			HashMap<String, String> postDataParams = new HashMap<String, String>();

			if (MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
				postDataParams.put("User_ID", "");
				gdt = new GetDetailTask(postDataParams);
				gdt.execute(Commons.GET_ADMIN_LIST_URL);
			} else {
				postDataParams.put("User_ID", MainActivity.getMainScreenActivity().getUSerID());
				// postDataParams.put("User_ID", "11");
				gdt = new GetDetailTask(postDataParams);
				gdt.execute(Commons.GET_DETAIL_URL);
			}
		} else {
			ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
			Commons.detailListItems = DHC.getUserData();
			int a = 0, p = 0, c = 0, h = 0;

			for (int i = 0; i < Commons.detailListItems.size(); i++) {

				String Status = Commons.detailListItems.get(i).getStatus();
				if (Status.toUpperCase().startsWith("A")) {
					++a;
				} else if (Status.toUpperCase().startsWith("P")) {
					++p;
				} else if (Status.toUpperCase().startsWith("C")) {
					++c;
				} else if (Status.toUpperCase().startsWith("H")) {
					++h;
				}
			}
			adapter = new LandingListAdapter(getActivity(), Commons.detailListItems);
			listview.setAdapter(adapter);
			MainActivity.getMainScreenActivity().tv_status_a.setText("A(" + Commons.detailListItems.size() + ")");
			MainActivity.getMainScreenActivity().tv_status_p.setText("P(" + p + ")");
			MainActivity.getMainScreenActivity().tv_status_c.setText("C(" + c + ")");
			MainActivity.getMainScreenActivity().tv_status_h.setText("H(" + h + ")");
			MainActivity.getMainScreenActivity().tv_status_a
					.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
			MainActivity.getMainScreenActivity().tv_status_p
					.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
			MainActivity.getMainScreenActivity().tv_status_c
					.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
			MainActivity.getMainScreenActivity().tv_status_h
					.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
			adapter.filter("P", tv_msg, listview);
			type = "P";
			MainActivity.getMainScreenActivity().tv_status_p
					.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));

		}
	}

	private class GetDetailTask extends AsyncProcess {

		ProgressDialog progressDialog;

		public GetDetailTask(HashMap<String, String> postDataParams) {
			super(postDataParams);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "Updating, Please wait...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(cancelListener);
		}

		@Override
		protected String doInBackground(String... params) {

			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (200 == responseCode) {

				String value = result.replace("\\", "");
				if (value.length() > 2)
					value = value.substring(1, value.length() - 1);
				try {

					JSONArray jarray = new JSONArray(value);
					int a = 0, p = 0, c = 0, h = 0;

					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jo = new JSONObject(jarray.getString(i));
						String CallId = jo.getString("CallId");
						String CustomerId = jo.getString("CustomerId");
						String CallType = jo.getString("CallType");
						String ComplaintDetails = jo.getString("ComplaintDetails");
						String CallLoginDate = jo.getString("CallLoginDate");
						String CallLoginTime = jo.getString("CallLoginTime");
						String Status = jo.getString("Status");
						String FeedBack = jo.getString("FeedBack");
						String ContactPerson = jo.getString("ContactPerson");
						String Description = jo.getString("Description");
						String CustomerName = "";
						try {
							CustomerName = jo.getString("CName");
						} catch (Exception e) {
							CustomerName = "";
						}
						String CustomerAddress = jo.getString("Address");
						String CustomerCellNo = jo.getString("CelNo");
						String empname = "";
						try {
							empname = jo.getString("empname");
						} catch (Exception e) {
							empname = "NA";
						}
						String CustomerLocation = "";
						try {
							CustomerLocation = jo.getString("locName");

						} catch (Exception e) {
							e.printStackTrace();
							CustomerLocation = "NA";
						}
						if (CustomerLocation.trim().equals(""))
							CustomerLocation = "NA";
						ComplaintDetailDto cpdetails = new ComplaintDetailDto();
						cpdetails.setCallID(CallId);
						cpdetails.setCustomerID(CustomerId);
						cpdetails.setCallType(CallType);
						cpdetails.setComplaintDetails(ComplaintDetails);
						cpdetails.setCallLoginDate(CallLoginDate);
						cpdetails.setCallLoginTime(CallLoginTime);
						cpdetails.setStatus(Status);
						cpdetails.setFeedBack(FeedBack);
						cpdetails.setContactPerson(ContactPerson);
						cpdetails.setDescription(Description);
						cpdetails.setCustomerName(CustomerName);
						cpdetails.setCustomerAddress(CustomerAddress);
						cpdetails.setCustomerCellNo(CustomerCellNo);
						cpdetails.setCustomerLocation(CustomerLocation);
						cpdetails.setEmpName(empname.equals("") ? "NA" : empname);

						DHC.Add_USER_DATA(cpdetails);
						if (Status.toUpperCase().startsWith("A")) {
							++a;
						} else if (Status.toUpperCase().startsWith("P")) {
							++p;
						} else if (Status.toUpperCase().startsWith("C")) {
							++c;
						} else if (Status.toUpperCase().startsWith("H")) {
							++h;
						}

					}
					Commons.detailListItems = DHC.getUserData();
					MainActivity.getMainScreenActivity().tv_status_a
							.setText("A(" + Commons.detailListItems.size() + ")");
					MainActivity.getMainScreenActivity().tv_status_p.setText("P(" + p + ")");
					MainActivity.getMainScreenActivity().tv_status_c.setText("C(" + c + ")");
					MainActivity.getMainScreenActivity().tv_status_h.setText("H(" + h + ")");
					adapter = new LandingListAdapter(getActivity(), Commons.detailListItems);
					listview.setAdapter(adapter);
					MainActivity.getMainScreenActivity().tv_status_a
							.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
					MainActivity.getMainScreenActivity().tv_status_p
							.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
					MainActivity.getMainScreenActivity().tv_status_c
							.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
					MainActivity.getMainScreenActivity().tv_status_h
							.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
					adapter.filter("P", tv_msg, listview);
					type = "P";
					MainActivity.getMainScreenActivity().tv_status_p
							.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));

					progressDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					progressDialog.dismiss();
					ShowAlertInformation.showDialog(getActivity(), "Error", "No data found");
				}
				System.out.println("GetDetailTask result is : " + (result == null ? "" : result));

			} else {
				progressDialog.dismiss();
				Log.i("GetDetailTask response", result == null ? "" : result);
				MainActivity.getMainScreenActivity().tv_status_a.setText("");
				MainActivity.getMainScreenActivity().tv_status_p.setText("");
				MainActivity.getMainScreenActivity().tv_status_c.setText("");
				MainActivity.getMainScreenActivity().tv_status_h.setText("");
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
			}
		}

		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				if (null != gdt) {
					gdt.cancel(true);
					gdt = null;

				}
			}
		};
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		MainActivity.getMainScreenActivity().tv_status_a
				.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
		MainActivity.getMainScreenActivity().tv_status_p
				.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
		MainActivity.getMainScreenActivity().tv_status_c
				.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
		MainActivity.getMainScreenActivity().tv_status_h
				.setBackgroundColor(getResources().getColor(R.color.blue_secondry));
		switch (id) {
		case R.id.tv_status_a:
			adapter.filter("", tv_msg, listview);
			type = "";
			MainActivity.getMainScreenActivity().tv_status_a
					.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));
			break;
		case R.id.tv_status_p:
			adapter.filter("P", tv_msg, listview);
			type = "P";
			MainActivity.getMainScreenActivity().tv_status_p
					.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));
			break;
		case R.id.tv_status_c:
			adapter.filter("C", tv_msg, listview);
			type = "C";
			MainActivity.getMainScreenActivity().tv_status_c
					.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));
			break;
		case R.id.tv_status_h:
			adapter.filter("H", tv_msg, listview);
			type = "H";
			MainActivity.getMainScreenActivity().tv_status_h
					.setBackgroundColor(getResources().getColor(R.color.blue_tertairy));
			break;

		case R.id.btn_refresh:
			update();
			break;
		case R.id.btn_search:
			SearchDialog(getActivity());
			break;
		default:
			break;
		}

	}

	private void SearchDialog(final Context mContext) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		LayoutInflater inflater = LayoutInflater.from(mContext);

		View view = inflater.inflate(R.layout.search_lay_main, null);
		final EditText edtsearch = (EditText) view.findViewById(R.id.edtsearch);
		final EditText edt_date1 = (EditText) view.findViewById(R.id.edt_date1);
		final EditText edt_date2 = (EditText) view.findViewById(R.id.edt_date2);
		final LinearLayout ll_date = (LinearLayout) view.findViewById(R.id.ll_date);
		Button btnfilter = (Button) view.findViewById(R.id.btnfilter);
		Button btncancel = (Button) view.findViewById(R.id.btncancel);

		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiofilter);
		final RadioButton radiotext = (RadioButton) view.findViewById(R.id.radiotext);
		final RadioButton radioengineer = (RadioButton) view.findViewById(R.id.radioengineer);
		final RadioButton radiodate = (RadioButton) view.findViewById(R.id.radiodate);
		ll_date.setVisibility(View.GONE);
		edtsearch.setVisibility(View.VISIBLE);
		if (!MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
			radioGroup.setVisibility(View.GONE);
		} else {
			radioGroup.setVisibility(View.VISIBLE);

		}

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				int selectedId = group.getCheckedRadioButtonId();

				if (selectedId == R.id.radiotext) {

					edtsearch.setVisibility(View.VISIBLE);
					ll_date.setVisibility(View.GONE);
					edtsearch.setHint(getString(R.string.enter_customer));
				} else if (selectedId == R.id.radioengineer) {

					edtsearch.setVisibility(View.VISIBLE);
					ll_date.setVisibility(View.GONE);
					edtsearch.setHint(getString(R.string.enter_engineer));

				} else if (selectedId == R.id.radiodate) {
					edtsearch.setVisibility(View.GONE);
					ll_date.setVisibility(View.VISIBLE);

				}

			}
		});

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		edt_date1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To show current date in the date picker
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker = new DatePickerDialog(mContext, new OnDateSetListener() {
					public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						Calendar c = Calendar.getInstance();
						c.set(selectedyear, selectedmonth, selectedday);
						edt_date1.setText(df.format(c.getTime()));
					}
				}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select date");
				mDatePicker.show();
			}
		});

		edt_date2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To show current date in the datepicker
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker = new DatePickerDialog(mContext, new OnDateSetListener() {
					public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						Calendar c = Calendar.getInstance();
						c.set(selectedyear, selectedmonth, selectedday);
						edt_date2.setText(df.format(c.getTime()));
					}
				}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select date");
				mDatePicker.show();
			}
		});
		alertDialog.setTitle("Filter Option");
		alertDialog.setView(view);

		// alertDialog.setNegativeButton("Cancel", new
		// DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		//
		// dialog.cancel();
		// }
		// });
		final AlertDialog alert = alertDialog.create();
		alert.setCanceledOnTouchOutside(false);
		btncancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.dismiss();

			}
		});

		btnfilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edtsearch.setError(null);
				edt_date1.setError(null);
				edt_date2.setError(null);
				if (radiotext.isChecked()) {
					if (edtsearch.getText().toString().equals(""))
						edtsearch.setError("blank text is not allowed");
					else{
						adapter.filterByName(type, tv_msg, listview, edtsearch.getText().toString().trim(), true);
						alert.dismiss();
					}
				} else if (radioengineer.isChecked()) {
					if (edtsearch.getText().toString().equals(""))
						edtsearch.setError("blank text is not allowed");
					else{
						adapter.filterByName(type, tv_msg, listview, edtsearch.getText().toString().trim(), false);
						alert.dismiss();
					}
				} else if (radiodate.isChecked()) {

					if (edt_date1.getText().toString().equals(""))
						edt_date1.setError("blank text is not allowed");
					if (edt_date2.getText().toString().equals(""))
						edt_date2.setError("blank text is not allowed");
					if (!edt_date1.getText().toString().equals("") && !edt_date2.getText().toString().equals("")) {

						if (MainActivity.getNetworkHelper().isOnline()) {
							LandingLiveFragment llf = new LandingLiveFragment();
							Bundle args = new Bundle();
							args.putString("str_fromdate", edt_date1.getText().toString().trim());
							args.putString("str_todate", edt_date2.getText().toString().trim());

							llf.setArguments(args);
							MainActivity.getMainScreenActivity().changeNavigationContentFragment(llf, true);
							alert.dismiss();
						} else {
							ShowAlertInformation.showDialog(getActivity(), "Network error",
									getString(R.string.offline));
						}
					}

				}
				
			}
		});

		alert.show();
	}
}
