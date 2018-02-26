package com.newland.comp.adapter.my.minitest;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.newland.comp.activity.MainActivity;
import com.newland.comp.bean.my.minitest.MiniTest;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

public class MiniTestListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<MiniTest> list;
	Context context;

	public MiniTestListAdapter(FragmentActivity fragementActivity, List<MiniTest> group_list) {
		this.context = fragementActivity;
		list = group_list;
		inflater = LayoutInflater.from(fragementActivity);
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
			convertView = inflater.inflate(R.layout.minitestlist_item_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mDurtime = (TextView) convertView.findViewById(R.id.durtime);
			viewHolder.mCreatetime = (TextView) convertView.findViewById(R.id.createtime);
			viewHolder.mOuttime = (TextView) convertView.findViewById(R.id.outtime);
			viewHolder.mQualified = (TextView) convertView.findViewById(R.id.qualified);
			viewHolder.mTestnum = (TextView) convertView.findViewById(R.id.testnum);
			viewHolder.mTestednum = (TextView) convertView.findViewById(R.id.testednum);
			viewHolder.mStarttest = (Button) convertView.findViewById(R.id.starttest);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTitle.setText(StringUtil.noNull(list.get(position).paperName));
		viewHolder.mDurtime.setText(StringUtil.noNull(list.get(position).answerTime));
		viewHolder.mCreatetime.setText(StringUtil.noNull(list.get(position).updateTime));
		viewHolder.mOuttime.setText(StringUtil.noNull(list.get(position).expireTime));
		viewHolder.mQualified.setText(StringUtil.noNull(list.get(position).passPoint));
		viewHolder.mTestnum.setText(StringUtil.noNull(list.get(position).joinTimes));
		viewHolder.mTestednum.setText(StringUtil.noNull(list.get(position).checkTimes));
		viewHolder.mStarttest.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//TODO 开始考试

			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView mTitle;
		private TextView mDurtime;
		private TextView mCreatetime;
		private TextView mOuttime;
		private TextView mQualified;
		private TextView mTestnum;
		private TextView mTestednum;
		private Button mStarttest;// 开始考试
	}
}
