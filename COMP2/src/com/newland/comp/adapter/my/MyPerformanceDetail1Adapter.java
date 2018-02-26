package com.newland.comp.adapter.my;

import java.util.List;

import com.newland.comp.activity.my.MyPerformanceDetailActivity;
import com.newland.comp.bean.my.EffictData2;
import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 绩效明细-班组管理适配器
 * 
 * @author H81
 *
 */
public class MyPerformanceDetail1Adapter extends BaseAdapter {
	LayoutInflater inflater;
	List<EffictData2> list;
	Context context;

	public MyPerformanceDetail1Adapter(MyPerformanceDetailActivity myPerformanceDetailActivity, List<EffictData2> group_list) {
		this.context = myPerformanceDetailActivity;
		list = group_list;
		inflater = LayoutInflater.from(myPerformanceDetailActivity);
	}

	
	public int getCount() {
		return list.size();
	}

	
	public Object getItem(int position) {
		return list.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_performance_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mMyperformancedetailitem_title = (TextView) convertView.findViewById(R.id.myperformancedetailitem_title);
			viewHolder.mMyper_item_basedata = (TextView) convertView.findViewById(R.id.myper_item_basedata);
			viewHolder.mMyper_item_score = (TextView) convertView.findViewById(R.id.myper_item_score);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mMyperformancedetailitem_title.setText(list.get(position).getClass_indicator_name());
		viewHolder.mMyper_item_basedata.setText(list.get(position).getClass_indicator_score());
		viewHolder.mMyper_item_score.setText(list.get(position).getClass_indicator_value());
		return convertView;
	}

	class ViewHolder {
		public TextView mMyperformancedetailitem_title;// 标题
		public TextView mMyper_item_basedata;// 基础数据
		public TextView mMyper_item_score;// 得分
	}

}
