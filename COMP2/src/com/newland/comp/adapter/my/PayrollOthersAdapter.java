package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.Select;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.Confirm;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.indicator.Marking;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.Fault;
import com.newland.comp.bean.my.PayrollNight;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 我的考勤适配器
 * 
 * @author H81
 * 
 */
public class PayrollOthersAdapter extends BaseAdapter {

	private Context context;
	private List<Fault> faulstList = new ArrayList<Fault>();
	private List<Confirm> confirmList = new ArrayList<Confirm>();
	private LayoutInflater mInflater;

	public PayrollOthersAdapter(Context context, List<Fault> list, List<Confirm> list2) {
		super();
		this.context = context;
		this.faulstList = list;
		this.confirmList = list2;
		this.mInflater = LayoutInflater.from(context);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return faulstList.size() + confirmList.size() + 2;
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("aaaaa" + getCount());
		if (position == 0) {
			final HeadViewHolder holder1;
			convertView = mInflater.inflate(R.layout.other_listview_head_cell, null);
			holder1 = new HeadViewHolder();
			holder1.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder1.titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder1);
			holder1.imageView.setBackgroundResource(R.drawable.fwzl_pre);
			holder1.titleTextView.setText("过失扣款记录");
		} else if (position > 0 && position <= faulstList.size()) {
			final FaultViewHolder holder2;
			convertView = mInflater.inflate(R.layout.other_fault_listview_cell, null);
			holder2 = new FaultViewHolder();
			holder2.tv_type = (TextView) convertView.findViewById(R.id.tv_title);
			holder2.tv_level = (TextView) convertView.findViewById(R.id.tv_level_value);
			holder2.tv_num = (TextView) convertView.findViewById(R.id.tv_number_value);
			convertView.setTag(holder2);
			Fault temp = faulstList.get(position - 1);
			holder2.tv_type.setText(StringUtil.noNull(temp.getFault_type()));
			holder2.tv_level.setText(StringUtil.noNull(temp.getFault_level()));
			holder2.tv_num.setText(StringUtil.noNull(temp.getFault_num()));
		} else if (position == faulstList.size() + 1) {
			final HeadViewHolder holder3;
			convertView = mInflater.inflate(R.layout.other_listview_head_cell, null);
			holder3 = new HeadViewHolder();
			holder3.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder3.titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder3);
			holder3.imageView.setBackgroundResource(R.drawable.fwzl_pre);
			holder3.titleTextView.setText("确认差扣罚记录");
		} else {
			final ConfirmViewHolder holder4;
			convertView = mInflater.inflate(R.layout.other_confirm_listview_cell, null);
			holder4 = new ConfirmViewHolder();
			holder4.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder4.tv_original = (TextView) convertView.findViewById(R.id.tv_original_number);
			holder4.tv_redundance = (TextView) convertView.findViewById(R.id.tv_redundance_number);
			holder4.tv_examine = (TextView) convertView.findViewById(R.id.tv_examine_value);
			convertView.setTag(holder4);
			Confirm temp = confirmList.get(position - faulstList.size() - 2);
			holder4.tv_title.setText(StringUtil.noNull(temp.getConfirm_type()));
			holder4.tv_original.setText(StringUtil.noNull(temp.getOrigin_num()));
			holder4.tv_redundance.setText(StringUtil.noNull(temp.getRedun_num()));
			holder4.tv_examine.setText(StringUtil.noNull(temp.getCheck_num()));
		}

		return convertView;
	}

	class HeadViewHolder {
		public ImageView imageView;
		public TextView titleTextView;
	}

	class FaultViewHolder {
		public TextView tv_type;
		public TextView tv_level;
		public TextView tv_num;
	}

	class ConfirmViewHolder {
		public TextView tv_title;
		public TextView tv_original;
		public TextView tv_redundance;
		public TextView tv_examine;
	}
}
