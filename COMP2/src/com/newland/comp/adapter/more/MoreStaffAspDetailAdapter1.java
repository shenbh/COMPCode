package com.newland.comp.adapter.more;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.more.MoreStaffAspAdapter.ViewHolder;
import com.newland.comp.bean.more.PrombleData;
import com.newland.comp.bean.more.PrombleDetailData;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 更多--问题详情之评价回复
 * 
 * @author H81
 *
 *         2015年5月7日 下午12:10:49
 * @version 1.0.0
 */
public class MoreStaffAspDetailAdapter1 extends BaseAdapter {
	LayoutInflater inflater;

	List<PrombleDetailData> list;
	Context context;

	public MoreStaffAspDetailAdapter1(BaseActivity baseActivity, List<PrombleDetailData> group_list) {
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
			convertView = inflater.inflate(R.layout.more_staffasp_detail_item1, null);
			viewHolder = new ViewHolder();

			viewHolder.mType = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.mTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mType.setText(StringUtil.noNull(list.get(position).getAssess_type()+"   "+StringUtil.noNull(list.get(position).reason)));
		viewHolder.mTime.setText(StringUtil.noNull(list.get(position).getAssess_time()));
		return convertView;
	}

	class ViewHolder {
		private TextView mType;// 类型
//		private TextView mReason;// 不满意理由
		private TextView mTime;// 时间
	}
}
