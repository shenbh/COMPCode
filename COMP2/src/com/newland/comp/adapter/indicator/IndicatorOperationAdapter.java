package com.newland.comp.adapter.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.indicator.IndicatorActivity;
import com.newland.comp.activity.indicator.IndicatorDetailActivity;
import com.newland.comp.bean.indicator.IndicatorAssResultDataChart;
import com.newland.comp.bean.indicator.IndicatorData;
import com.newland.comp.bean.indicator.IndicatorTrendData;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.MyWebView;
import com.newland.comp2.R;

/**
 * 运营指标列表界面适配器
 * 
 * @author H81
 * 
 */
public class IndicatorOperationAdapter extends BaseAdapter {

	private Context context;
	private List<IndicatorData> list = new ArrayList<IndicatorData>();
	private LayoutInflater mInflater;
	private String time; // 时间

	public IndicatorOperationAdapter(Context context, List<IndicatorData> list, String time) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		this.time = time;
	}

	public int getCount() {
		return list.size();
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
			convertView = mInflater.inflate(R.layout.fragment_indicator_operation_item, null);
			holder = new ViewHolder();
			holder.webView = (MyWebView) convertView.findViewById(R.id.webview);
			holder.mZb_name = (TextView) convertView.findViewById(R.id.zb_name);
			holder.mZb_value = (TextView) convertView.findViewById(R.id.zb_value);
			holder.mZb_cire = (TextView) convertView.findViewById(R.id.zb_cire);
			holder.mZb_tb = (TextView) convertView.findViewById(R.id.zb_tb);
			holder.mZb_hl = (TextView) convertView.findViewById(R.id.zb_hl);
			holder.root_layout = convertView.findViewById(R.id.root_layout);
			holder.webview_layout = convertView.findViewById(R.id.webview_layout);
			holder.mZb_flag_3_remark = (TextView) convertView.findViewById(R.id.zb_flag_3_remark);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final IndicatorData bean = list.get(position);
		String tempValue = "--";
		String tempLoop = "--";
		String tempTb = "--";
		if (StringUtil.noNull(bean.data_type).equals("1")) {// 数据需要展示类型（1-小数
			if (StringUtil.isNotEmpty(bean.loop)) {
				tempLoop = mul(bean.loop, 100);
			} else {
				tempLoop = "--";
			}
			if (StringUtil.isNotEmpty(bean.tb)) {
				tempTb = mul(bean.tb, 100);
			} else {
				tempTb = "--";
			}
		}
		if (StringUtil.noNull(bean.data_type).equals("2")) {// 数据需要展示类型2-百分比
			if (StringUtil.isNotEmpty(bean.loop)) {
				tempLoop = mul(bean.loop, 100);
			} else {
				tempLoop = "--";
			}
			if (StringUtil.isNotEmpty(bean.tb)) {
				tempTb = mul(bean.tb, 100);
			} else {
				tempTb = "--";
			}
			if (StringUtil.isNotEmpty(bean.value)) {
				tempValue = Double.parseDouble(bean.value) * 100 / 100.0 + "";
				BigDecimal b = new BigDecimal(Double.parseDouble(bean.value));
				double f = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();// 保留四位小数
				tempValue = mul(f + "", 100d);
			} else {
				tempValue = "--";
			}
		}
		if (StringUtil.noNull(bean.data_type).equals("3")) {// 数据需要展示类型 3-整数
			if (StringUtil.isNotEmpty(bean.value)) {
				if (isNumber(bean.value))
					tempValue = getDecimalsInteger(bean.value) + "";
			} else {
				tempValue = "--";
			}
			if (StringUtil.isNotEmpty(bean.tb)) {
				tempTb = mul(bean.tb, 100);
			} else {
				tempTb = "--";
			}
			if (StringUtil.isNotEmpty(bean.loop)) {
				tempLoop = mul(bean.loop, 100);
			} else {
				tempLoop = "--";
			}
		}

		if (StringUtil.noNull(bean.zb_flag).equals("1")) {// 1:展示指标名称、同比、环比、当前值，图表
			holder.mZb_value.setText(StringUtil.noNull(tempValue));// 当前值
		}
		if (StringUtil.noNull(bean.zb_flag).equals("2")) {// 2：展示指标名称、备注(显示在当前值位置)
			holder.mZb_value.setText(StringUtil.noNull(bean.remark));// 备注
		}
		// if (StringUtil.noNull(bean.zb_flag).equals("3")) {//
		// 3：展示指标名称、备注(显示在图表位置)
		// holder.webView.setVisibility(View.GONE);
		// holder.mZb_flag_3_remark.setText(bean.remark);
		// }

		holder.mZb_cire.setText(StringUtil.noNull(tempLoop));
		holder.mZb_tb.setText( StringUtil.noNull(tempTb));

		holder.mZb_name.setText(StringUtil.noNull(bean.zb_name));// 展示指标名称
		holder.mZb_hl.setText(StringUtil.noNull(bean.rang));
		// 设置支持JavaScript等
		WebSettings webSettings = holder.webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		holder.webView.setWebChromeClient(new MyWebChromeClient());
//		holder.webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		holder.webView.setVerticalScrollBarEnabled(false); // 垂直不显示
		// 转换接口数据为 x,y数组
		List<IndicatorTrendData> trendDataList = bean.trendDataList;
		StringBuilder linex = new StringBuilder();
		StringBuilder liney = new StringBuilder();
		String tempYData = "";
		for (int i = 0; i < trendDataList.size(); i++) {

			Object obj = trendDataList.get(i);
			if (obj.getClass() != IndicatorTrendData.class) {
				System.out.println("class:" + obj.getClass());
			} else {
				if (StringUtil.isNotEmpty(trendDataList.get(i).YData)) {
					if (StringUtil.noNull(bean.data_type).equals("1")||StringUtil.noNull(bean.data_type).equals("2")) {
						tempYData = trendDataList.get(i).YData;
					}
					if (StringUtil.noNull(bean.data_type).equals("3")) {
						if (isNumber(trendDataList.get(i).YData)) {
							tempYData = getDecimalsInteger(trendDataList.get(i).YData);
						} else {
							tempYData = trendDataList.get(i).YData;
						}
					}
				}
				linex.append(trendDataList.get(i).XData).append(",");
				liney.append(tempYData).append(",");
			}
		}
		// 用于接收XData,YData。并且去掉最后的一个","
		String x, y;
		if (trendDataList.size() == 0) // 如果返回数据trendDataList为空
		{
			x = linex.toString().substring(0, linex.toString().length());
			y = liney.toString().substring(0, liney.toString().length());
		} else {
			x = linex.toString().substring(0, linex.toString().length() - 1);
			y = liney.toString().substring(0, liney.toString().length() - 1);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("line_x", x);
		map.put("line_y", y);
		map.put("zb_name", bean.zb_name);
		final String data = JSON.toJSONString(map);

		holder.webView.loadUrl("file:///android_asset/line-labels.htm");

		holder.webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				// mWebView.loadUrl("javascript:test()");

				holder.webView.loadUrl("javascript:init_data('" + data + "')");

			}

		});
		// 检查是否显示图表
		boolean isShow = SharedPreferencesUtils.getConfigBoolean(context, SharedPreferencesUtils.Name, "showChart");
		if (isShow) { // 说明要显示图表
			holder.webview_layout.setVisibility(View.VISIBLE);
		} else {
			holder.webview_layout.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		public MyWebView webView; // 数据图
		public TextView mZb_name;
		public TextView mZb_value;
		public TextView mZb_cire;
		public TextView mZb_tb;
		public TextView mZb_hl;
		public View root_layout;
		public View webview_layout;
		public TextView mZb_flag_3_remark;
	}

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {
		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * loadUrl on the UI thread.
		 */
		@JavascriptInterface
		public void clickOnAndroid() {
			System.out.println("接收js参数");
		}

	}

	/**
	 * Provides a hook for calling "alert" from javascript. Useful for debugging
	 * your javascript.
	 */
	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			result.confirm();
			return true;
		}

		@Override
		public void onRequestFocus(WebView view) {
			// TODO Auto-generated method stub
			view.setFocusable(false);
			view.setFocusableInTouchMode(false);
			super.onRequestFocus(view);
		}
	}

	/**
	 * 判断是否是整数
	 */
	public boolean isInteger(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否是小数
	 */
	public boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断是否是数字
	 */
	public boolean isNumber(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 转成百分比
	 */
	public String mul(String d, double d2) {
		double d1;
		if (isNumber(d)) {
			d1 = Double.parseDouble(d);
			BigDecimal bd1 = new BigDecimal(Double.toString(d1));
			BigDecimal bd2 = new BigDecimal(Double.toString(d2));
			return bd1.multiply(bd2).doubleValue() + "%";
		} else {
			return d;
		}
	}

	/**
	 * 取小数的整数部分
	 */
	public String getDecimalsInteger(String s) {
		int index = s.indexOf(".");
		if (index != -1) {
			s = s.substring(0, index);
		}
		return s;
	}
}
