package com.newland.comp.adapter.my;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyMessageDetailActivity;
import com.newland.comp.adapter.my.MyAttendanceFilingAdapter.ViewHolder;
import com.newland.comp.bean.my.FilingData;
import com.newland.comp.bean.my.SysMsg;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**我的消息适配器
 * @author H81
 *
 * 2015年4月22日 下午4:45:07
 * @version 1.0.0
 */
public class MyMessageAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<SysMsg> list;
	Context context;
//	HashMap<String, Boolean> hashMap;
	private boolean flag = false;

	public MyMessageAdapter(BaseActivity activity, List<SysMsg> group_list) {
		// public MyMessageAdapter(BaseActivity activity, List<SysMsg>
		// group_list,HashMap<String, Boolean> hashMap) {
		this.context = activity;
		list = group_list;
		inflater = LayoutInflater.from(activity);
		// this.hashMap=hashMap;

//		hashMap = new HashMap<String, Boolean>();
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

	
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_message_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mIv_circle = (ImageView) convertView.findViewById(R.id.iv_circle);
			viewHolder.message_item = (LinearLayout) convertView.findViewById(R.id.message_item);
			viewHolder.mTv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.mTv_content = (TextView) convertView.findViewById(R.id.tv_content);
			viewHolder.mSender=(TextView) convertView.findViewById(R.id.sender);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		if (StringUtil.noNull(list.get(position).statue).equals("0")) {
//			viewHolder.mIv_circle.setVisibility(View.VISIBLE);
//		}else  {
//			viewHolder.mIv_circle.setVisibility(View.INVISIBLE);
//		}
		System.out.println(position+"----------"+list.get(position).statue);
		
		viewHolder.mSender.setText(StringUtil.noNull(list.get(position).senderName));
		viewHolder.mTv_time.setText(StringUtil.noNull(list.get(position).msg_time));

		viewHolder.mTv_content.setText(StringUtil.noNull(list.get(position).content));

		return convertView;
	}
	

	class ViewHolder {
		private ImageView mIv_circle;// 圆
		private LinearLayout message_item;// 点击显示详情
		private TextView mTv_time;// 时间
		private TextView mTv_content;// 内容
		private TextView mSender;//发送人
	}
}