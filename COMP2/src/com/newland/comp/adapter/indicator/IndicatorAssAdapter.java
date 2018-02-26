package com.newland.comp.adapter.indicator;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.adapter.indicator.IndicatorOperationAdapter.DemoJavaScriptInterface;
import com.newland.comp.bean.indicator.IndicatorAssData;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 指标关联适配器
 * 
 * @author H81
 * 
 */
public class IndicatorAssAdapter extends BaseAdapter {

	private Context context;
	private List<IndicatorAssData> list = new ArrayList<IndicatorAssData>();
	private LayoutInflater mInflater;

	public IndicatorAssAdapter(Context context, List<IndicatorAssData> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		if (list.size() % 2 == 0) {
			return list.size() / 2;
		} else {
			return (list.size() / 2) + 1;
		}

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.indicator_acc_row, null);
			holder = new ViewHolder();
			holder.left_key = (TextView) convertView.findViewById(R.id.left_key);
			holder.left_value = (TextView) convertView.findViewById(R.id.left_value);
			holder.right_key = (TextView) convertView.findViewById(R.id.right_key);
			holder.right_value = (TextView) convertView.findViewById(R.id.right_value);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		IndicatorAssData bean = list.get(position * 2);
		holder.left_key.setText(StringUtil.noNull(bean.ass_name));
		holder.left_value.setText(StringUtil.noNull(bean.ass_value));

		if (position == this.getCount() - 1 && list.size() % 2 == 1) {

			holder.right_key.setText("");
			holder.right_value.setText("");
		} else {
			IndicatorAssData bean2 = list.get(position * 2 + 1);
			holder.right_key.setText(StringUtil.noNull(bean2.ass_name));
			holder.right_value.setText(StringUtil.noNull(bean2.ass_value));
		}

		return convertView;
	}

	class ViewHolder {
		public TextView left_key;
		public TextView left_value;
		public TextView right_key;
		public TextView right_value;
	}

}
