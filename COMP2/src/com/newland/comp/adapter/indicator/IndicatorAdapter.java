package com.newland.comp.adapter.indicator;

import java.util.ArrayList;

import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndicatorAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<Boolean> isOnClickList;
	String[] strName = { "运营指标", "营销指标", "服务指标" };
	int[] drawableN = { R.drawable.ic_salary_nor, R.drawable.ic_workload_nor, R.drawable.ic_service_nor };
	int[] drawableP = { R.drawable.ic_salary_pre, R.drawable.ic_workload_pre, R.drawable.ic_service_pre };

	public IndicatorAdapter(Context c, LayoutInflater mInflater, ArrayList<Boolean> isOnClickList) {
		this.mInflater = mInflater;
		this.isOnClickList = isOnClickList;
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return strName.length;
	}

	
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.sub_menu_gridview_cell, null);
			holder = new ViewHolder();
			holder.imageV = (ImageView) convertView.findViewById(R.id.gridv_child_Image);
			holder.textV = (TextView) convertView.findViewById(R.id.gridv_child_text);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.textV.setTextColor(0xffffffff);
		holder.textV.setText(strName[position]);
		if (isOnClickList.get(position) == true) {
			holder.imageV.setImageResource(drawableP[position]);

		} else {
			holder.imageV.setImageResource(drawableN[position]);

		}
		return convertView;

	}

}

class ViewHolder {
	ImageView imageV;
	TextView textV;
}