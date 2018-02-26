package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

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
import com.newland.comp.bean.my.MyStartData;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 我的星级轮岗
 * 
 * @author H81
 *
 */
public class MyStartTurnWorkAdapter extends BaseAdapter {

	private Context context;
	private List<MyStartData> list = new ArrayList<MyStartData>();
	private LayoutInflater mInflater;

	public MyStartTurnWorkAdapter(Context context, List<MyStartData> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_start_lg_cell, null);
			holder = new ViewHolder();
			holder.skill_line = (TextView) convertView.findViewById(R.id.skill_line);
			holder.standard = (TextView) convertView.findViewById(R.id.standard);
			holder.service = (TextView) convertView.findViewById(R.id.service);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyStartData bean = list.get(position);
		holder.skill_line.setText(StringUtil.noNull(bean.skill));
		holder.standard.setText("标准量(>=650)："+StringUtil.noNull(bean.standard));
		holder.service.setText("服务系数(>=1.4)："+StringUtil.noNull(bean.xs));
		return convertView;
	}

	class ViewHolder {
		public TextView skill_line;
		public TextView standard;
		public TextView service;
	}

}
