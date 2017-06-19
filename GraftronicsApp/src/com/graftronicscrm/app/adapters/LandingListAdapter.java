package com.graftronicscrm.app.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.graftronicscrm.app.R;
import com.graftronicscrm.app.fragment.ScanFragment;
import com.graftronicscrm.app.helper.Commons;
import com.graftronicscrm.app.main.MainActivity;
import com.graftronicscrm.app.pojos.ComplaintDetailDto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LandingListAdapter extends BaseAdapter {

	private Context context;
	private List<ComplaintDetailDto> items;
	private ArrayList<ComplaintDetailDto> arraylist;
	private boolean isPending = true;

	public LandingListAdapter(Context context, List<ComplaintDetailDto> items) {
		this.context = context;
		this.items = items;
		this.arraylist = new ArrayList<ComplaintDetailDto>();
		this.arraylist.addAll(items);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.landing_list_item, null);
			holder = new ViewHolder();
			holder.tvtitle = (TextView) convertView.findViewById(R.id.tvtitle);
			holder.tvlocation = (TextView) convertView.findViewById(R.id.tvlocation);
			holder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
			holder.ivnext = (ImageView) convertView.findViewById(R.id.ivnext);
			holder.tvstatus = (TextView) convertView.findViewById(R.id.tvstatus);
			holder.tvempname = (TextView) convertView.findViewById(R.id.tvempname);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvtitle.setText(items.get(position).getComplaintDetails());
		holder.tvlocation.setText(items.get(position).getCustomerLocation());
		String date = items.get(position).getCallLoginDate().trim().split(" ")[0];
		String sd[] = date.split("/");
		holder.tvdate.setText(sd[1] + "/" + sd[0] + "/" + sd[2]);
		holder.tvstatus.setText("Status : " + items.get(position).getStatus());

		if (isPending && Commons.getDifferenceDays(items.get(position).getCallLoginDate()) <= 15) {
			holder.tvstatus.setTextColor(Color.RED);
			holder.tvstatus.setTypeface(null, Typeface.BOLD);
		} else {
			holder.tvstatus.setTextColor(Color.parseColor("#0B5399"));
			holder.tvstatus.setTypeface(null, Typeface.NORMAL);
		}
		if (MainActivity.getMainScreenActivity().getUSerID().equals("ADMIN")) {
			holder.tvempname.setText("Engineer Name : " + items.get(position).getEmpName());
			holder.tvempname.setVisibility(View.VISIBLE);
		} else {
			holder.tvempname.setVisibility(View.GONE);
		}
		holder.ivnext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putString("status", items.get(position).getStatus());
				args.putString("name", items.get(position).getCustomerName());
				args.putString("address", items.get(position).getCustomerAddress());
				args.putString("cellno", items.get(position).getCustomerCellNo());
				args.putString("callid", items.get(position).getCallID());
				args.putString("complaintdetails", items.get(position).getComplaintDetails());
				args.putString("complaint_status", items.get(position).getStatus());
				ScanFragment newFragment = new ScanFragment();
				newFragment.setArguments(args);
				MainActivity.getMainScreenActivity().changeNavigationContentFragment(newFragment, true);
			}
		});

		return convertView;
	}

	public static class ViewHolder {

		TextView tvtitle;
		TextView tvstatus;
		TextView tvlocation;
		TextView tvdate;
		TextView tvempname;
		ImageView ivnext;

	}

	// Filter Class
	public void filter(String charText, TextView tv_msg, ListView listview) {
		charText = charText.toLowerCase(Locale.getDefault());
		items.clear();
		if (charText.equalsIgnoreCase("P"))
			isPending = true;
		else
			isPending = false;
		if (charText.length() == 0) {
			items.addAll(arraylist);
		} else {
			for (ComplaintDetailDto wp : arraylist) {

				if (wp.getStatus().toLowerCase(Locale.getDefault()).startsWith(charText)) {
					if (charText.equalsIgnoreCase("C")) {
						if (Commons.getDifferenceDays(wp.getCallLoginDate()) <= 30)
							items.add(wp);
					} else {
						items.add(wp);
					}

				}
			}
		}
		if (items.size() == 0) {
			tv_msg.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		} else {
			tv_msg.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		notifyDataSetChanged();
	}

	public void filterByName(String charText, TextView tv_msg, ListView listview, String tosearch, boolean isCustomer) {
		charText = charText.toLowerCase(Locale.getDefault());
		items.clear();
		if (charText.equalsIgnoreCase("P"))
			isPending = true;
		else
			isPending = false;
		for (ComplaintDetailDto wp : arraylist) {
			if (isCustomer) {
				if (wp.getCustomerName().toLowerCase(Locale.getDefault())
						.contains(tosearch.toLowerCase(Locale.getDefault())))
					items.add(wp);
			} else {
				if (wp.getEmpName().toLowerCase(Locale.getDefault())
						.contains(tosearch.toLowerCase(Locale.getDefault())))
					items.add(wp);
			}

		}

		if (items.size() == 0) {
			tv_msg.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		} else {
			tv_msg.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		notifyDataSetChanged();
	}

	public void filterByDate(String charText, TextView tv_msg, ListView listview, String fromdate, String todate) {
		charText = charText.toLowerCase(Locale.getDefault());
		items.clear();
		if (charText.equalsIgnoreCase("P"))
			isPending = true;
		else
			isPending = false;

		for (ComplaintDetailDto wp : arraylist) {
			if (Commons.checkValidDate(fromdate, todate, wp.getCallLoginDate()))
				items.add(wp);
		}

		if (items.size() == 0) {
			tv_msg.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		} else {
			tv_msg.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		notifyDataSetChanged();
	}
}
