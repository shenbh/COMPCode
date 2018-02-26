package com.newland.comp.adapter.my;

import java.util.List;

import com.newland.comp.activity.my.MyAttendanceOverTimeActivity;
import com.newland.comp.adapter.my.MyPerformanceDetail1Adapter.ViewHolder;
import com.newland.comp.bean.my.EffictData2;
import com.newland.comp.bean.my.OverTimeCountData;
import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**加班统计--加班明细
 * @author H81
 *
 */
public class MyAttendanceOverTimeAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<OverTimeCountData> list;
	Context context;

	public MyAttendanceOverTimeAdapter(MyAttendanceOverTimeActivity myAttendanceOverTimeActivity, List<OverTimeCountData> group_list) {
		this.context = myAttendanceOverTimeActivity;
		list = group_list;
		inflater = LayoutInflater.from(myAttendanceOverTimeActivity);
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
			convertView = inflater.inflate(R.layout.my_attendance_overtime_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mOvertime = (TextView) convertView.findViewById(R.id.overtime);
			viewHolder.mOvertime_type = (TextView) convertView.findViewById(R.id.overtime_type);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTitle.setText(list.get(position).getKq_time());
		viewHolder.mOvertime.setText(list.get(position).getWork_time()+"小时");
		viewHolder.mOvertime_type.setText(list.get(position).getWork_type());
		return convertView;
	}

	class ViewHolder {
	    private TextView mTitle;//标题
	    private TextView mOvertime;//加班时长
	    private TextView mOvertime_type;//加班类型
	}

}
