
package com.graftronicscrm.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.adapters.LandingListAdapter;
import com.graftronicscrm.app.asynctask.AsyncProcess;
import com.graftronicscrm.app.helper.Commons;
import com.graftronicscrm.app.helper.ShowAlertInformation;
import com.graftronicscrm.app.main.MainActivity;
import com.graftronicscrm.app.pojos.ComplaintDetailDto;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class LandingLiveFragment extends Fragment implements OnClickListener {

	private ListView listview;
	private LandingListAdapter adapter;
	private TextView tv_msg;
	private GetDetailTask gdt;

	private String str_todate;
	private String str_fromdate;
	private String type = "";
	private ArrayList<ComplaintDetailDto> listItems = new ArrayList<ComplaintDetailDto>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.lay_landing, container, false);
		listview = (ListView) view.findViewById(R.id.listview);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);

		setblank();
		Bundle b = getArguments();
		str_todate = b.getString("str_todate");
		str_fromdate = b.getString("str_fromdate");
		update();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.getMainScreenActivity().hideHomeIcon();
		MainActivity.getMainScreenActivity().tv_status_a.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_p.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_c.setOnClickListener(this);
		MainActivity.getMainScreenActivity().tv_status_h.setOnClickListener(this);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	private void update() {

		listItems.clear();

		if (MainActivity.getNetworkHelper().isOnline()) {
			HashMap<String, String> postDataParams = new HashMap<String, String>();

			if (MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
				String sd[] = str_fromdate.split("/");
				String ed[] = str_todate.split("/");

				postDataParams.put("str_fromdate", sd[1] + "/" + sd[0] + "/" + sd[2]);
				postDataParams.put("str_todate", ed[1] + "/" + ed[0] + "/" + ed[2]);
				gdt = new GetDetailTask(postDataParams);
				gdt.execute(Commons.GET_ADMIN_LIVE_LIST_URL);
			}
		} else {
			ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));

			adapter = new LandingListAdapter(getActivity(), listItems);
			listview.setAdapter(adapter);
			setblank();
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

						if (Status.toUpperCase().startsWith("A")) {
							++a;
						} else if (Status.toUpperCase().startsWith("P")) {
							++p;
						} else if (Status.toUpperCase().startsWith("C")) {
							++c;
						} else if (Status.toUpperCase().startsWith("H")) {
							++h;
						}
						listItems.add(cpdetails);
					}

					MainActivity.getMainScreenActivity().tv_status_a.setText("A(" + listItems.size() + ")");
					MainActivity.getMainScreenActivity().tv_status_p.setText("P(" + p + ")");
					MainActivity.getMainScreenActivity().tv_status_c.setText("C(" + c + ")");
					MainActivity.getMainScreenActivity().tv_status_h.setText("H(" + h + ")");
					adapter = new LandingListAdapter(getActivity(), listItems);
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

		default:
			break;
		}

	}

	private void setblank() {
		int a = 0, p = 0, c = 0, h = 0;
		MainActivity.getMainScreenActivity().tv_status_a.setText("A(" + listItems.size() + ")");
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
	}

}
