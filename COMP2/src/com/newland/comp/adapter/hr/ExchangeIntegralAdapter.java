package com.newland.comp.adapter.hr;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.my.IntegralExchangeData;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.Options;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 积分兑换适配器 frgment
 * 
 * @author H81
 * 
 */
public class ExchangeIntegralAdapter extends BaseAdapter {
	LayoutInflater inflater;

	List<IntegralExchangeData> list;
	Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = Options.getListOptions();

	public ExchangeIntegralAdapter(Context context, List<IntegralExchangeData> group_list) {
		this.context = context;
		list = group_list;
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.fragment_exchange_rows, null);
			viewHolder = new ViewHolder();

			viewHolder.prize_img = (ImageView) convertView.findViewById(R.id.prize_img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.intergral = (TextView) convertView.findViewById(R.id.intergral);
			viewHolder.less_num = (TextView) convertView.findViewById(R.id.less_num);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		IntegralExchangeData bean = list.get(position);
		imageLoader.displayImage(HttpUtils.URL_PIC_ROOT + StringUtil.noNull(bean.prize_img, ""), viewHolder.prize_img, options);
		viewHolder.title.setText(StringUtil.noNull(bean.prize_name));
		viewHolder.intergral.setText("所需积分：" + StringUtil.noNull(bean.intergral));
		viewHolder.less_num.setText("剩余数量：" + StringUtil.noNull(bean.less_num));
		return convertView;
	}

	class ViewHolder {
		public ImageView prize_img; // 图片
		public TextView title; // 标题
		public TextView intergral; // 所需积分
		public TextView less_num; // 剩余数量
	}
}
