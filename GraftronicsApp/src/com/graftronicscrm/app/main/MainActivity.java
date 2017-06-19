package com.graftronicscrm.app.main;

import java.util.HashMap;
import java.util.Stack;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.db.DBOpenHelperClass;
import com.graftronicscrm.app.fragment.LandingFragment;
import com.graftronicscrm.app.fragment.LoginFragment;
import com.graftronicscrm.app.helper.NetworkHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private Boolean exit = false;
	private static MainActivity mainActivity;
	private static NetworkHelper networkHelper;
	public static final String MyPREFERENCES = "AppPref";
	private static SharedPreferences sharedpreferences;

	public LinearLayout toplay;
	protected Fragment mFrag;
	protected Fragment cFrag, rootFragment;
	private HashMap<String, Stack<Fragment>> mFragmentsStack;
	private TextView actionBarTitle;
	public TextView tv_name, tv_status_a, tv_status_p, tv_status_c, tv_status_h;

	public Button btn_search, btn_refresh, btn_logout, btn_exit;
	private final String IS_LOGIN = "IsLoggedIn";
	private final String KEY_MOBILE = "mobileNo";
	private final String KEY_USER_NAME = "userName";
	private final String KEY_USER_ID = "userID";
	protected static final String CONTENT_TAG = "contenFragments";
	public DBOpenHelperClass dbHandler;

	public static MainActivity getMainScreenActivity() {
		return mainActivity;
	}

	public static NetworkHelper getNetworkHelper() {
		return networkHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivity = this;
		networkHelper = new NetworkHelper(this);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mFragmentsStack = new HashMap<String, Stack<Fragment>>();
		mFragmentsStack.put(CONTENT_TAG, new Stack<Fragment>());
		toplay = (LinearLayout) findViewById(R.id.toplay);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_refresh = (Button) findViewById(R.id.btn_refresh);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(logoClik);
		btn_logout.setOnClickListener(logoClik);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_status_a = (TextView) findViewById(R.id.tv_status_a);
		tv_status_p = (TextView) findViewById(R.id.tv_status_p);
		tv_status_c = (TextView) findViewById(R.id.tv_status_c);
		tv_status_h = (TextView) findViewById(R.id.tv_status_h);
		tv_name.setText(getUserName().toUpperCase());
		ActionBarSetup();
		dbHandler = DBOpenHelperClass.getSharedObject(this);
		if (isLoggedIn())
			changeNavigationContentFragment(new LandingFragment(), false);
		else
			changeNavigationContentFragment(new LoginFragment(), false);

		onNewIntent(getIntent());

	}

	private void ActionBarSetup() {
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar_view);
		getSupportActionBar().setElevation(0);
		View v = getSupportActionBar().getCustomView();

		actionBarTitle = (TextView) v.findViewById(R.id.title);

		Toolbar parent = (Toolbar) v.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		actionBarTitle.setText(getString(R.string.app_name));

	}

	public void hideHomeIcon() {
		btn_search.setVisibility(View.INVISIBLE);
		btn_refresh.setVisibility(View.INVISIBLE);
		btn_logout.setVisibility(View.VISIBLE);
		btn_exit.setVisibility(View.VISIBLE);
		toplay.setVisibility(View.VISIBLE);
	}

	public void showHomeIcon(boolean isLogin) {
		if(isLogin){
			btn_search.setVisibility(View.INVISIBLE);
			btn_refresh.setVisibility(View.INVISIBLE);
			btn_logout.setVisibility(View.INVISIBLE);
			toplay.setVisibility(View.GONE);
		}else{
		btn_search.setVisibility(View.VISIBLE);
		btn_refresh.setVisibility(View.VISIBLE);
		btn_logout.setVisibility(View.VISIBLE);
		toplay.setVisibility(View.VISIBLE);
		}
		btn_exit.setVisibility(View.VISIBLE);
	}

	OnClickListener logoClik = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			// case R.id.iv_home:
			// changeNavigationContentFragment(new LandingFragment(), false);
			// break;
			// case R.id.backBtn:
			// removeFragment();
			// break;
			case R.id.btn_exit:
				new AlertDialog.Builder(mainActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
	            .setMessage("Are you sure you want to exit?")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	            }).setNegativeButton("No", null).show();
				break;
			case R.id.btn_logout:
				SharedPreferences.Editor editor = MainActivity.getSharePreferance().edit();
				editor.clear();
				editor.commit();
				restartActivity();
				break;
			default:
				break;
			}
		}
	};

	public void changeNavigationContentFragment(Fragment frgm, boolean shouldAdd) {

		if (shouldAdd) {

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			if (null != fm.findFragmentById(R.id.main_fragment))
				ft.hide(fm.findFragmentById(R.id.main_fragment));
			ft.add(R.id.main_fragment, frgm, frgm.getClass().getSimpleName());
			ft.addToBackStack(null);
			// ft.commitAllowingStateLoss();
			ft.commit();
			mFragmentsStack.get(CONTENT_TAG).push(frgm);
		} else {
			mFragmentsStack.get(CONTENT_TAG).clear();
			FragmentManager manager = getSupportFragmentManager();
			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			manager.beginTransaction().replace(R.id.main_fragment, frgm).commitAllowingStateLoss();
			rootFragment = frgm;
		}

		cFrag = frgm;

	}

	// Checking exitFlag on Back press, if exitFlag is true removing all
	// fragments from back stack and exiting app.
	// if exitFlag is false then allowing default behavior. That is removing
	// current Fragment and loading previous
	// Fragment.
	@Override
	public void onBackPressed() {
		removeFragment();
	}

	public void removeFragment() {
		int statckSize = mFragmentsStack.get(CONTENT_TAG).size();
		if (statckSize == 0) {
			 if (exit) {
		            finish(); // finish activity
		        } else {
		            Toast.makeText(this, "Press Back again to Exit.",
		                    Toast.LENGTH_SHORT).show();
		            exit = true;
		            new Handler().postDelayed(new Runnable() {
		                @Override
		                public void run() {
		                    exit = false;
		                }
		            }, 3 * 1000);

		        }
			
		} else {
			Fragment fragment;
			if (statckSize > 1)
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 2);
			else
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 1);
			mFragmentsStack.get(CONTENT_TAG).pop();
			FragmentManager fm = this.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.detach(cFrag);
			ft.commitAllowingStateLoss();
			if (statckSize > 1) {
				cFrag = fragment;
				fragment.onResume();
			} else {
				cFrag = rootFragment;
				rootFragment.onResume();
			}
			super.onBackPressed();
		}

	}

	public static SharedPreferences getSharePreferance() {
		return sharedpreferences;
	}

	public String getUserName() {
		return sharedpreferences.getString(KEY_USER_NAME, "");
	}

	public String getUSerID() {
		return sharedpreferences.getString(KEY_USER_ID, "");
	}

	public boolean isLoggedIn() {
		return sharedpreferences.getBoolean(IS_LOGIN, false);
	}

	public void setSharPreferancename(String userName, String userID, String mobileNo, boolean isLogin) {
		Editor editor = sharedpreferences.edit();
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userID);
		editor.putString(KEY_MOBILE, mobileNo);
		editor.putBoolean(IS_LOGIN, isLogin);
		editor.commit();
	}

	public static void redirectToFragment(Fragment fragment) {
		Fragment VF = fragment;
		MainActivity.getMainScreenActivity().changeNavigationContentFragment(VF, true);

	}

	private void restartActivity() {
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
}
