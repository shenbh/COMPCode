package com.newland.comp.adapter.process;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.my.ClassSearchInfo;
import com.newland.comp.bean.process.ProcessData;
import com.newland.comp.bean.process.ProcessDataComplete;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;
import com.newland.comp2.R.color;

/**
 * 流程-待办流程适配器
 * 
 * @author H81
 * 
 *         2015年4月28日 上午11:31:24
 * @version 1.0.0
 */
public class PrTodoAdapter extends BaseAdapter {
	LayoutInflater inflater;

	List<ProcessDataComplete> list;
	Context context;

	public PrTodoAdapter(BaseActivity baseActivity, List<ProcessDataComplete> group_list) {
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
		ViewHolder viewHolder = null;
		ViewHolder1 viewHolder1 = null;
		boolean isGreen=(list.get(position).proType.equals(BaseActivity.PROCESS_TYPE_VACATE) || list.get(position).proType.equals(BaseActivity.PROCESS_TYPE_LEAVE))||list.get(position).flowType.equals("1");
		if (convertView == null || viewHolder == null && isGreen) {
			convertView = inflater.inflate(R.layout.prtodo_item, null);
			viewHolder = new ViewHolder();

			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.mTv_huanjie = (TextView) convertView.findViewById(R.id.tv_huanjie);
			viewHolder.mTv_chuangjianren = (TextView) convertView.findViewById(R.id.tv_chuangjianren);
			viewHolder.mTv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.mCreateTime = (TextView) convertView.findViewById(R.id.createtime);
			viewHolder.mArriveTv = (TextView) convertView.findViewById(R.id.arrive_tv);
			viewHolder.mCreateTv = (TextView) convertView.findViewById(R.id.create_tv);
			convertView.setTag(viewHolder);

		} else if (isGreen) {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (convertView == null || viewHolder1 == null && !(isGreen)) {
			convertView = inflater.inflate(R.layout.prtodo_item1, null);
			viewHolder1 = new ViewHolder1();

			viewHolder1.mTitle1 = (TextView) convertView.findViewById(R.id.title1);
			viewHolder1.mTv_huanjie1 = (TextView) convertView.findViewById(R.id.tv_huanjie1);
			viewHolder1.mTv_chuangjianren1 = (TextView) convertView.findViewById(R.id.tv_chuangjianren1);
			viewHolder1.mTv_time1 = (TextView) convertView.findViewById(R.id.tv_time1);
			viewHolder1.mCreateTime1 = (TextView) convertView.findViewById(R.id.createtime1);
			viewHolder1.mArriveTv1 = (TextView) convertView.findViewById(R.id.arrive_tv1);
			viewHolder1.mCreateTv1 = (TextView) convertView.findViewById(R.id.create_tv1);
			convertView.setTag(viewHolder1);
		}else if (!(isGreen)) {
			viewHolder1 = (ViewHolder1) convertView.getTag();

		}
		if (viewHolder != null) {

			viewHolder.mTitle.setText(StringUtil.noNull(list.get(position).flowTitle));
			viewHolder.mTv_huanjie.setText("当前环节：" + StringUtil.noNull(list.get(position).arrivePro));
			viewHolder.mTv_chuangjianren.setText("创建人：" + StringUtil.noNull(list.get(position).proPerson));
			if (StringUtil.isNotEmpty(list.get(position).arriveTime)) {
				String[] arriveTimes = list.get(position).arriveTime.split(" ");
				String arriveTime = arriveTimes[0];
				viewHolder.mTv_time.setText(arriveTime);
			}else {
				viewHolder.mTv_time.setText(StringUtil.noNull(list.get(position).arriveTime));
			}
			if (StringUtil.isNotEmpty(list.get(position).createTime)) {
				String[] createTimes = list.get(position).createTime.split(" ");
				String createTime = createTimes[0];
				viewHolder.mCreateTime.setText(StringUtil.noNull(createTime));
			} else {
				viewHolder.mCreateTime.setText(StringUtil.noNull(list.get(position).createTime));
			}
		}
		if (viewHolder1 != null) {
			viewHolder1.mTitle1.setText(StringUtil.noNull(list.get(position).flowTitle));
			viewHolder1.mTv_huanjie1.setText("当前环节：" + StringUtil.noNull(list.get(position).arrivePro));
			viewHolder1.mTv_chuangjianren1.setText("创建人：" + StringUtil.noNull(list.get(position).proPerson));
			if (StringUtil.isNotEmpty(list.get(position).arriveTime)) {
				String[] arriveTimes = list.get(position).arriveTime.split(" ");
				String arriveTime = arriveTimes[0];
				viewHolder1.mTv_time1.setText(arriveTime);
			}else {
				viewHolder1.mTv_time1.setText(StringUtil.noNull(list.get(position).arriveTime));
			}
			if (StringUtil.isNotEmpty(list.get(position).createTime)) {
				String[] createTimes = list.get(position).createTime.split(" ");
				String createTime = createTimes[0];
				viewHolder1.mCreateTime1.setText(StringUtil.noNull(createTime));
			} else {
				viewHolder1.mCreateTime1.setText(StringUtil.noNull(list.get(position).createTime));
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mTitle;// 标题
		private TextView mTv_huanjie;// 当前环节（要加上“当前环节”）
		private TextView mTv_chuangjianren;// 创建人（要加上“创建人”）
		private TextView mTv_time;// 到达时间
		private TextView mCreateTime;// 创建时间
		private TextView mArriveTv;
		private TextView mCreateTv;
	}

	class ViewHolder1 {
		private TextView mTitle1;// 标题
		private TextView mTv_huanjie1;// 当前环节（要加上“当前环节”）
		private TextView mTv_chuangjianren1;// 创建人（要加上“创建人”）
		private TextView mTv_time1;// 到达时间
		private TextView mCreateTime1;// 创建时间
		private TextView mArriveTv1;
		private TextView mCreateTv1;

	}
}
