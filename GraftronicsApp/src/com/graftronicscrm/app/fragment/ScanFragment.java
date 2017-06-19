package com.graftronicscrm.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.asynctask.AsyncProcess;
import com.graftronicscrm.app.helper.Commons;
import com.graftronicscrm.app.helper.ShowAlertInformation;
import com.graftronicscrm.app.main.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScanFragment extends Fragment implements OnClickListener {

	private TextView tv_purchase, tv_waranty, tv_status, tv_name, tv_address, tv_mobile, tv_complaint,
			tv_complaint_status;
	private Button btnpending, btnremark, btnupdate, btnscan, btnedtscan;
	private BarcodeTask bat;
	private ProgressDialog progressDialog;
	private Spinner spstatus;
	private String status, callid;
	private EditText edtremark;
	private LinearLayout firstll, secondll;
	private View seprator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.lay_scan, container, false);
		tv_purchase = (TextView) rootView.findViewById(R.id.tv_purchase);
		tv_waranty = (TextView) rootView.findViewById(R.id.tv_waranty);
		tv_status = (TextView) rootView.findViewById(R.id.tv_status);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);
		tv_address = (TextView) rootView.findViewById(R.id.tv_address);
		tv_mobile = (TextView) rootView.findViewById(R.id.tv_mobile);
		tv_complaint = (TextView) rootView.findViewById(R.id.tv_complaint);
		tv_complaint_status = (TextView) rootView.findViewById(R.id.tv_complaint_status);
		firstll = (LinearLayout) rootView.findViewById(R.id.firstll);
		secondll = (LinearLayout) rootView.findViewById(R.id.secondll);
		seprator = (View) rootView.findViewById(R.id.seprator);
		btnpending = (Button) rootView.findViewById(R.id.btnpending);
		btnremark = (Button) rootView.findViewById(R.id.btnremark);
		btnupdate = (Button) rootView.findViewById(R.id.btnupdate);
		btnscan = (Button) rootView.findViewById(R.id.btnscan);
		btnedtscan = (Button) rootView.findViewById(R.id.btnedtscan);
		spstatus = (Spinner) rootView.findViewById(R.id.spstatus);
		edtremark = (EditText) rootView.findViewById(R.id.edtremark);
		edtremark.setMovementMethod(new ScrollingMovementMethod());
		edtremark.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		btnedtscan.setOnClickListener(this);
		btnpending.setOnClickListener(this);
		btnremark.setOnClickListener(this);
		btnupdate.setOnClickListener(this);
		btnscan.setOnClickListener(this);
		Bundle b = getArguments();
		status = b.getString("status");
		String name = b.getString("name");
		String address = b.getString("address");
		String cellno = b.getString("cellno");
		callid = b.getString("callid");
		String complaintdetails = b.getString("complaintdetails");
		String complaint_status = b.getString("complaint_status");
		tv_complaint.setText(complaintdetails);
		tv_name.setText(name);
		tv_address.setText(address);
		tv_mobile.setText(cellno);
		tv_complaint_status.setText("Status : " + complaint_status);
		ArrayList<String> countryList = new ArrayList<>();
		countryList.add("Select");
		countryList.add("Complete");
		countryList.add("Pending");
		if (MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
			btnedtscan.setVisibility(View.INVISIBLE);
			btnscan.setVisibility(View.INVISIBLE);
		} else {
			btnedtscan.setVisibility(View.VISIBLE);
			btnscan.setVisibility(View.VISIBLE);
		}
		ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, countryList);
		spstatus.setAdapter(adapter_country);
		spstatus.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		if (status.equalsIgnoreCase("Complete"))
			spstatus.setSelection(1);
		else
			spstatus.setSelection(2);
		String purchase_text = " Date of Purchase/AMC:  ";

		tv_purchase.setText(Html.fromHtml(purchase_text));
		String warranty_text = " Warranty: ";

		tv_waranty.setText(Html.fromHtml(warranty_text));
		String status_text = " Status: ";

		tv_status.setText(Html.fromHtml(status_text));
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		MainActivity.getMainScreenActivity().hideHomeIcon();
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.btnpending:

			break;
		case R.id.btnedtscan:
			createBarcodeDia(getActivity());
			break;
		case R.id.btnupdate:
			if (spstatus.getSelectedItemPosition() == 0) {
				ShowAlertInformation.showDialog(getActivity(), "Error", "Please select status.");
			} else if (edtremark.getText().toString().trim().equals("")) {
				ShowAlertInformation.showDialog(getActivity(), "Error", "Please enter remark.");
			} else {
				HashMap<String, String> postDataParams = new HashMap<String, String>();
				// "Bar_CodeID":"1101031617"

				// CallId : String value for particular call
				// Status : "Complete" or "Pending"
				// FeedBack : "text value"

				postDataParams.put("CallId", callid);
				postDataParams.put("Status", spstatus.getSelectedItem().toString());
				postDataParams.put("FeedBack", edtremark.getText().toString());
				new BarcodeTask(postDataParams, 2).execute(Commons.SUBMIT_DETAILS);
			}
			break;

		case R.id.btnscan:
			String packageString = "com.graftronicscrm.app";
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.setPackage(packageString);
			intent.putExtra("SCAN_MODE", "SCAN_MODE");
			startActivityForResult(intent, 123);
			break;
		default:
			break;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 123) {
			if (resultCode == Activity.RESULT_OK) {

				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Toast.makeText(getActivity(), " Format is: " + format +
				// "\ncontents :" + contents,
				// Toast.LENGTH_LONG).show();
				Log.d("Scaning ", "Scan successful " + "\nformat :" + format + "\ncontents :" + contents);
				HashMap<String, String> postDataParams = new HashMap<String, String>();
				// "Bar_CodeID":"1101031617"
				postDataParams.put("Bar_CodeID", contents);
				new BarcodeTask(postDataParams, 1).execute(Commons.GET_BARCODE_DETAIL);

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// To Handle cancel
				Log.i("Scaning ", "Scan unsuccessful");
			}
		}
	}

	private class BarcodeTask extends AsyncProcess {
		private boolean bCancelled = false;
		int wstype = 0;

		public BarcodeTask(HashMap<String, String> postDataParams, int wstype) {
			super(postDataParams);
			this.wstype = wstype;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...");
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
					if (wstype == 1) {

						try {
							JSONArray jarray = new JSONArray(value);
							JSONObject jo = new JSONObject(jarray.getString(0));
							String AMCDID = jo.getString("AMCDID");
							String AMCID = jo.getString("AMCID");
							String CategoryID = jo.getString("CategoryID");
							String ModelID = jo.getString("ModelID");
							String CreateDate = jo.getString("CreateDate");
							String CreatedBy = jo.getString("CreatedBy");
							String SubCategoryID = jo.getString("SubCategoryID");
							String Serialno = jo.getString("Serialno");
							String Productno = jo.getString("Productno");
							String Date_Amc = jo.getString("Date_Amc"); // date
																		// of
																		// purchase
							String AMCStatus = jo.getString("AMCStatus");// status
							String AMCType = jo.getString("AMCType");
							String AMCService = jo.getString("AMCService");
							String Type = jo.getString("Type");
							String FYear = jo.getString("FYear");
							String Remark = jo.getString("Remark");
							String CreatedDate = jo.getString("CreatedDate");

							String FromDate = jo.getString("FromDate");// -
							String ToDate = jo.getString("ToDate");// status
							String Configuration = jo.getString("Configuration");
							System.out.println("response" + "\n" + jo);
							String purchase_text = " Date of Purchase/AMC: <font  color='#ED1C24'><b>  " + Date_Amc
									+ "</b></font> ";
							tv_purchase.setText(Html.fromHtml(purchase_text));
							String status_text = " Status: <font  color='#ED1C24'><b> " + AMCStatus + " </b></font> ";
							tv_status.setText(Html.fromHtml(status_text));
							String warranty_text = " Warranty: <font  color='#ED1C24'><b>  "
									+ Commons.calculateYear(FromDate, ToDate) + " year</b></font> ";
							tv_waranty.setText(Html.fromHtml(warranty_text));
							seprator.setVisibility(View.VISIBLE);
							firstll.setVisibility(View.VISIBLE);
							secondll.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							e.printStackTrace();
							ShowAlertInformation.showDialog(getActivity(), "Error", "Invalid barcode");
						}

					} else if (wstype == 2) {
						try {
							JSONObject jo = new JSONObject(value);
							String status = jo.getString("status");
							if (status.equals("Success")) {

								Toast.makeText(MainActivity.getMainScreenActivity(), "Data updated successfully",
										Toast.LENGTH_LONG).show();
								MainActivity.getMainScreenActivity().getSupportFragmentManager().popBackStack();
							} else {
								ShowAlertInformation.showDialog(getActivity(), "Error", "Record not updated.");
							}
							progressDialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
						System.out.println("BarcodeTask result is : " + (result == null ? "" : result));
						progressDialog.dismiss();

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("BarcodeTask result is : " + (result == null ? "" : result));

				progressDialog.dismiss();
			} else {
				Log.i("BarcodeTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Invalid barcode");
				progressDialog.dismiss();
			}

		}

		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				bCancelled = true;
				if (null != bat) {
					bat.cancel(true);
					System.out.println("refe" + bat.isCancelled());
					bat = null;
					// activity.getSupportFragmentManager().popBackStack();
				}
			}
		};
	}

	private void createBarcodeDia(Context mContext) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Barcode");
		View customDialogView = inflater.inflate(R.layout.dialog_product, null, false);
		final EditText edtbox = (EditText) customDialogView.findViewById(R.id.edtNoofBox);
		builder.setView(customDialogView);
		final AlertDialog mAlertDialog = builder.create();
		mAlertDialog.setCancelable(false);
		Button btncancel = (Button) customDialogView.findViewById(R.id.btncancel);
		Button btnok = (Button) customDialogView.findViewById(R.id.btnok);
		btnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edtbox.setError(null);
				if (edtbox.getText().toString().equals("")) {
					edtbox.setError("Enter barcode");
				} else {
					HashMap<String, String> postDataParams = new HashMap<String, String>();
					// "Bar_CodeID":"1101031617"
					postDataParams.put("Bar_CodeID", edtbox.getText().toString());
					new BarcodeTask(postDataParams, 1).execute(Commons.GET_BARCODE_DETAIL);
					mAlertDialog.dismiss();
				}
			}
		});
		btncancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();

			}
		});

		mAlertDialog.show();

	}
}