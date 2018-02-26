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
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.indicator.WarningDetail;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 话务预警详细信息
 * 
 * @author H81
 *
 */
public class IndicatorWarningAdapter extends BaseAdapter {

	private Context context;
	private List<WarningDetail> list = new ArrayList<WarningDetail>();
	private LayoutInflater mInflater;
	

	public IndicatorWarningAdapter(Context context, List<WarningDetail> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.indicator_warning_item, null);
			holder = new ViewHolder();
			holder.mTime = (TextView) convertView.findViewById(R.id.time);
			holder. mType = (TextView) convertView.findViewById(R.id.type);
			holder.mContent = (TextView) convertView.findViewById(R.id.content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		WarningDetail bean = list.get(position);
		holder.mTime.setText(StringUtil.noNull(bean.warn_time));
		holder.mType.setText(StringUtil.noNull(bean.warn_type));
		holder.mContent.setText(StringUtil.noNull(bean.warn_content));

		return convertView;
	}

	class ViewHolder {
		   public TextView mTime;
		   public TextView mType;
		   public TextView mContent;
	}

}
