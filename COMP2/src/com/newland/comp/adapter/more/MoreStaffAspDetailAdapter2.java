package com.newland.comp.adapter.more;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.more.MoreStaffAspAdapter.ViewHolder;
import com.newland.comp.bean.more.PrombleData;
import com.newland.comp.bean.more.PrombleDetailData2;
import com.newland.comp.bean.process.ProcessFlow;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 更多--问题详情之处理流程
 * 
 * @author H81
 *
 *         2015年5月7日 下午12:10:07
 * @version 1.0.0
 */
public class MoreStaffAspDetailAdapter2 extends BaseAdapter {
	LayoutInflater inflater;

	List<PrombleDetailData2> list;
	Context context;

	public MoreStaffAspDetailAdapter2(BaseActivity baseActivity, List<PrombleDetailData2> group_list) {
		this.context = baseActivity;
		list = group_list;
		inflater = LayoutInflater.from(baseActivity);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.more_staffasp_detail_item2, null);
			viewHolder = new ViewHolder();
			viewHolder.mCire = (ImageView) convertView.findViewById(R.id.cire);
			viewHolder.mTv_person = (TextView) convertView.findViewById(R.id.tv_person);
			viewHolder.mTv_content = (TextView) convertView.findViewById(R.id.tv_content);
			viewHolder.mTv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PrombleDetailData2 bean = list.get(position);
		if (position == 0) {// 说明是第一条
			viewHolder.mCire.setBackgroundResource(R.drawable.diandian1);
		} 
		else {
			viewHolder.mCire.setBackgroundResource(R.drawable.diandian2);
		}
		viewHolder.mTv_content.setText("回复内容：" + StringUtil.noNull(list.get(position).reply_content));
		viewHolder.mTv_person.setText(StringUtil.noNull("处理人：" + list.get(position).handle_person + "        处理科室：" + list.get(position).handle_dep));
		viewHolder.mTv_time.setText("处理完成时间：" + StringUtil.noNull(list.get(position).handle_time));
		return convertView;
	}

	class ViewHolder {
		private ImageView mCire;
		private TextView mTv_content;
		private TextView mTv_person;
		private TextView mTv_time;

	}
}
