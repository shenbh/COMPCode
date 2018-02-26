package com.newland.comp.adapter.my;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.activity.my.MyAttendanceLeaveActivity;
import com.newland.comp.activity.my.MyAttendanceOverTimeActivity;
import com.newland.comp.adapter.my.MyAttendanceOverTimeAdapter.ViewHolder;
import com.newland.comp.bean.my.LeaveCountData;
import com.newland.comp.bean.my.OverTimeCountData;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**请假统计--请假明细
 * @author H81
 *
 */
public class MyAttendanceLeaveAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<LeaveCountData> list;
	Context context;

	public MyAttendanceLeaveAdapter(MyAttendanceLeaveActivity myAttendanceLeaveActivity, List<LeaveCountData> group_list) {
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
			convertView = inflater.inflate(R.layout.my_attendance_leave_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mStarttime = (TextView) convertView.findViewById(R.id.starttime);
			viewHolder.mEndtime = (TextView) convertView.findViewById(R.id.endtime);
			viewHolder.mDur_time = (TextView) convertView.findViewById(R.id.dur_time);
			viewHolder.mTime_type = (TextView) convertView.findViewById(R.id.time_type);
			viewHolder.mTime_unit = (TextView) convertView.findViewById(R.id.time_unit);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mStarttime.setText(StringUtil.noNull(list.get(position).getStart_time()));
		viewHolder.mEndtime.setText(StringUtil.noNull(list.get(position).getEnd_time()));
		viewHolder.mDur_time.setText(StringUtil.noNull(list.get(position).getDur_time()));
		viewHolder.mTime_type.setText(StringUtil.noNull(list.get(position).getQj_type()));
		viewHolder.mTime_unit.setText(StringUtil.noNull(list.get(position).getLength_unit()));
		return convertView;
	}

	class ViewHolder {
		private TextView mStarttime;// 请假开始时间
		private TextView mEndtime;// 结束时间
		private TextView mDur_time;// 请假时长
		private TextView mTime_type;// 请假类型
		private TextView mTime_unit;// 时长单位
	}

}
