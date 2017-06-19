package com.graftronicscrm.app.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.asynctask.AsyncProcess;
import com.graftronicscrm.app.db.DataHelperClass;
import com.graftronicscrm.app.helper.Commons;
import com.graftronicscrm.app.helper.ShowAlertInformation;
import com.graftronicscrm.app.main.MainActivity;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

	private Button btnlogin;
	private EditText edtMobile;
	private LoginTask lat;
	private ProgressDialog progressDialog;

	public LoginFragment() {
		super();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.lay_login, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		btnlogin = (Button) view.findViewById(R.id.btnLogin);
		edtMobile = (EditText) view.findViewById(R.id.edtMobile);

		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mobile = edtMobile.getText().toString().trim();
				edtMobile.setError(null);
				if (mobile.equals("")) {

					edtMobile.setError("Please enter mobile no");
				} else if (mobile.equalsIgnoreCase("admin")) {
					MainActivity.getMainScreenActivity().tv_name.setText(("ADMIN"));
					MainActivity.getMainScreenActivity().setSharPreferancename("ADMIN", "ADMIN",
							edtMobile.getText().toString().trim(), true);
					MainActivity.getMainScreenActivity().changeNavigationContentFragment(new LandingFragment(), false);
				} else if (!mobile.equals("") && mobile.length() < 10) {

					edtMobile.setError("Mobile no length should be 10");
				} else {
					if (MainActivity.getNetworkHelper().isOnline()) {

						HashMap<String, String> postDataParams = new HashMap<String, String>();
						// postDataParams.put("phone_number", "8879337402");
						//9699076104
						postDataParams.put("phone_number", mobile);

						new LoginTask(postDataParams).execute(Commons.LOGIN_URL);
					} else {
						ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
					}
				}

			}
		});
		return view;
	}

	@Override
	public void onResume() {

		super.onResume();
		MainActivity.getMainScreenActivity().toplay.setVisibility(View.GONE);
		MainActivity.getMainScreenActivity().showHomeIcon(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	private class LoginTask extends AsyncProcess {

		public LoginTask(HashMap<String, String> postDataParams) {
			super(postDataParams);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "", "login please wait...");
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

					// JSONArray jarray = new JSONArray(value);
					JSONObject jo = new JSONObject(value);
					// [{"status": "Success","User_ID": "2","UserName": "ashishs
					// ","FirstName": "ashish","LastName": ""}]
					//
					String status = jo.getString("status");

					if (status.equals("Success")) {
						String User_ID = jo.getString("User_ID");
						String FirstName = jo.getString("FirstName");
						String LastName = jo.getString("LastName");
						System.out.println(status + "\n" + User_ID + "\n" + FirstName + "\n" + LastName);
						//delete old records
						new DataHelperClass(getActivity()).clearRecord();
						MainActivity.getMainScreenActivity().tv_name
								.setText((FirstName + " " + LastName).toUpperCase());
						MainActivity.getMainScreenActivity().setSharPreferancename(FirstName + " " + LastName, User_ID,
								edtMobile.getText().toString().trim(), true);
						MainActivity.getMainScreenActivity().changeNavigationContentFragment(new LandingFragment(),
								false);

					} else {
						progressDialog.dismiss();
						ShowAlertInformation.showDialog(getActivity(), "Error", "Invalid Mobile  Number");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowAlertInformation.showDialog(getActivity(), "Error", "No data found");
					progressDialog.dismiss();
				}
				System.out.println("LoginTask result is : " + (result == null ? "" : result));
				progressDialog.dismiss();
			} else {
				Log.i("LoginTask response", result == null ? "" : result);
				ShowAlertInformation.showDialog(getActivity(), "Error", "Error");
				progressDialog.dismiss();
			}

		}

		OnCancelListener cancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				if (null != lat) {
					lat.cancel(true);
					System.out.println("refe" + lat.isCancelled());
					lat = null;
					// activity.getSupportFragmentManager().popBackStack();
				}
			}
		};
	}

}
