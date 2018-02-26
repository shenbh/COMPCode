package com.newland.comp.adapter.my;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.my.ClassSearchInfo;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**班表查询适配器
 * @author H81
 *
 * 2015-4-20 下午1:49:33
 * @version 1.0.0
 */
public class MyScheduleAdapter extends BaseAdapter {
	LayoutInflater inflater;

	List<ClassSearchInfo> list;
	Context context;

	public MyScheduleAdapter(BaseActivity baseActivity, List<ClassSearchInfo> group_list) {
		this.context = baseActivity;
		list = group_list;
		inflater = LayoutInflater.from(baseActivity);
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
			convertView = inflater.inflate(R.layout.my_schedule_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mUsername = (TextView) convertView.findViewById(R.id.username);
			viewHolder.mUserid = (TextView) convertView.findViewById(R.id.userid);
			viewHolder.mKs = (TextView) convertView.findViewById(R.id.ks);
			viewHolder.mZq = (TextView) convertView.findViewById(R.id.zq);
			viewHolder.mSb_date = (TextView) convertView.findViewById(R.id.sb_date);
			viewHolder.mSb_time = (TextView) convertView.findViewById(R.id.sb_time);
			viewHolder.bz = (TextView) convertView.findViewById(R.id.bz);
			viewHolder.seat = (TextView) convertView.findViewById(R.id.seat);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mUsername.setText(StringUtil.noNull(list.get(position).getUsername()));
		viewHolder.mUserid.setText(StringUtil.noNull(list.get(position).getUserid()));
		if (StringUtil.isNotEmpty(list.get(position).getZq())) {
			viewHolder.mKs.setText(StringUtil.noNull(list.get(position).getKs()+">"));
		}else {
			viewHolder.mKs.setText(StringUtil.noNull(list.get(position).getKs()));
		}
		if (StringUtil.isNotEmpty(list.get(position).bz)) {
			viewHolder.mZq.setText(StringUtil.noNull(list.get(position).getZq()+">"));
		}else {
			viewHolder.mZq.setText(StringUtil.noNull(list.get(position).getZq()));
		}
		viewHolder.mSb_date.setText(StringUtil.noNull(list.get(position).getSb_date()));
		viewHolder.mSb_time.setText(StringUtil.noNull(list.get(position).getSb_time())+"--"+StringUtil.noNull(list.get(position).getXb_time()));
		viewHolder.seat.setText(StringUtil.noNull(list.get(position).seat));
		viewHolder.bz.setText(StringUtil.noNull(list.get(position).bz));
		
		return convertView;
	}

	class ViewHolder {
		private TextView mUsername;// 名字
		private TextView mUserid;// id
		private TextView mKs;// 科室
		private TextView mZq;// 话务区
		private TextView mSb_date;// 上班日期
		private TextView mSb_time;// 上班时间
		private TextView bz;//班组
		private TextView seat;//座位号
	}
}
