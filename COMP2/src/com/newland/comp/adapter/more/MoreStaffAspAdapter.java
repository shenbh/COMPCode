package com.newland.comp.adapter.more;

import java.util.List;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.more.PrombleData;
import com.newland.comp.bean.process.ProcessData;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoreStaffAspAdapter extends BaseAdapter {
	LayoutInflater inflater;

	List<PrombleData> list;
	Context context;

	public MoreStaffAspAdapter(BaseActivity baseActivity, List<PrombleData> group_list) {
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
			convertView = inflater.inflate(R.layout.more_staffasp_item, null);
			viewHolder = new ViewHolder();

			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mTv_huanjie = (TextView) convertView.findViewById(R.id.tv_huanjie);
			viewHolder.mTv_state = (TextView) convertView.findViewById(R.id.tv_state);
			viewHolder.mTv_time = (TextView) convertView.findViewById(R.id.tv_time);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTitle.setText(StringUtil.noNull(list.get(position).getTitle()));
		viewHolder.mTv_huanjie.setText("当前环节：" + StringUtil.noNull(list.get(position).getCurrent()));
		viewHolder.mTv_state.setText("问题状态：" + StringUtil.noNull(list.get(position).getStatue()));
		viewHolder.mTv_time.setText("提问时间："+StringUtil.noNull(list.get(position).getTime()));
		return convertView;
	}

	class ViewHolder {
		private TextView mTitle;// 标题
		private TextView mTv_huanjie;// 当前环节
		private TextView mTv_state;// 问题状态
		private TextView mTv_time;// 时间
	}
}
