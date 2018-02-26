package com.newland.comp.adapter.my;



import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.bean.my.FilingData;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 考勤报备
 * 
 * @author H81
 * 
 */
public class MyAttendanceFilingAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<FilingData> list;
	Context context;

	public MyAttendanceFilingAdapter(FragmentActivity myAttendanceLeaveActivity, List<FilingData> group_list) {
		this.context = myAttendanceLeaveActivity;
		list = group_list;
		inflater = LayoutInflater.from(myAttendanceLeaveActivity);
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
			convertView = inflater.inflate(R.layout.my_attendance_filing_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.mType = (TextView) convertView.findViewById(R.id.type);
			viewHolder.mState = (TextView) convertView.findViewById(R.id.state);
			viewHolder.mStarttime = (TextView) convertView.findViewById(R.id.starttime);
			viewHolder.mEndtime = (TextView) convertView.findViewById(R.id.endtime);
			viewHolder.mReason = (TextView) convertView.findViewById(R.id.reason);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mStarttime.setText(StringUtil.noNull(list.get(position).getStart_time()));
		viewHolder.mEndtime.setText(StringUtil.noNull(list.get(position).getEnd_time()));
		viewHolder.mType.setText(StringUtil.noNull(list.get(position).getType()));
		viewHolder.mState.setText(StringUtil.noNull(list.get(position).getSta()));
		viewHolder.mReason.setText(StringUtil.noNull(list.get(position).getReason()));
		return convertView;
	}

	class ViewHolder {
		private TextView mType;// 申请类型
		private TextView mState;// 审批状态
		private TextView mStarttime;// 开始时间
		private TextView mEndtime;// 结束时间
		private TextView mReason;// 原因
	}
}
