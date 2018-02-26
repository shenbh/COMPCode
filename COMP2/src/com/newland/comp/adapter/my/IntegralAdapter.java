package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.IntegralData;
import com.newland.comp.bean.my.PayrollQos;
import com.newland.comp.bean.my.PayrollWorkload;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 我的积分适配器
 * 
 * @author H81
 * 
 */
public class IntegralAdapter extends BaseAdapter {

	private List<IntegralData> list = new ArrayList<IntegralData>();
	private LayoutInflater mInflater;

	public IntegralAdapter(Context context, List<IntegralData> list) {
		super();
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_integral_row, null);
			holder = new ViewHolder();
			holder.mPro_name = (TextView) convertView.findViewById(R.id.pro_name);
			holder.mPro_time = (TextView) convertView.findViewById(R.id.pro_time);
			holder.mPro_level = (TextView) convertView.findViewById(R.id.pro_level);
			holder.mPro_value = (TextView) convertView.findViewById(R.id.pro_value);
			holder.mPro_multiple = (TextView) convertView.findViewById(R.id.pro_multiple);
			holder.mPro_num = (TextView) convertView.findViewById(R.id.pro_num);
			holder.mPro_from = (TextView) convertView.findViewById(R.id.pro_from);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		IntegralData bean = list.get(position);
		holder.mPro_name.setText(StringUtil.noNull(bean.pro_name));
		holder.mPro_time.setText(StringUtil.noNull(bean.get_time));
		holder.mPro_level.setText(StringUtil.noNull(bean.pro_level));
		holder.mPro_value.setText(StringUtil.noNull(bean.pro_num));

		holder.mPro_multiple.setText(StringUtil.noNull(bean.multiple));
		holder.mPro_num.setText(StringUtil.noNull(bean.integral));
		holder.mPro_from.setText(StringUtil.noNull(bean.integral_from));
		return convertView;
	}

	class ViewHolder {

		public TextView mPro_name;
		public TextView mPro_time;
		public TextView mPro_level;
		public TextView mPro_value;
		public TextView mPro_multiple;
		public TextView mPro_num;
		public TextView mPro_from;
	}

}
