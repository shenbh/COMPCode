package com.newland.comp.activity.indicator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.indicator.IndicatorAssNewAdapter;
import com.newland.comp.bean.indicator.IndicatorAssResultData;
import com.newland.comp.bean.indicator.IndicatorAssResultDataChart;
import com.newland.comp.bean.indicator.IndicatorAssResultDataRelation;
import com.newland.comp.bean.indicator.IndicatorTrendData;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.IndicatorPopupWindow;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.MyListView;
import com.newland.comp2.R;

/**
 * 指标详细页面
 * 
 * @author H81
 * 
 */
public class IndicatorDetailActivity extends BaseActivity {

	private WebView mWebview;
	private TextView mTime;
	private TextView mValue;
	private TextView mRange;
	private TextView mHb;
	private TextView mTb;
	private TextView mData_from;
	private TextView mCompare;
	private MyListView mList_data;
	private TextView mNo_ind;
	private LinearLayout mZbdy_layout;
	private TextView mZbdy;
	private TextView mKs;
	private TextView mRule;
	private TextView zbdy_btn;
	private TextView fsfqy;
	private TextView alpha; // 阴影遮罩
	private TextView mZb_flag_3_remark;// 当zb_flag=3时webview位置显示此文本内容

	ImageButton right_btn;
	ImageButton left_btn;
	TextView right_tv;
	LoadingDialog myDialog;
	LayoutInflater inflater;
	private View mMenuView;

	private String quarter_type; // day/week/mouth/quarte/
	private String time; // 2015-3-30/2015-03,1(2015年3月第一周)/2015-03/2015,1(2015
							// 年第1季度)
	private String zb_id; // 指标id
	private String org_id = ""; // 机构id
	private String zb_name = "";// 指标名
	private String org_type = "";// 机构类别
	private IndicatorAssNewAdapter adpAdapter;
	private IndicatorPopupWindow popwin; // 分时分区域弹出框
	private IndicatorAssResultData indicatorAssResultData; // 实体对象bean
	private List<IndicatorAssResultDataRelation> relationDataList; // 指标关联集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indicator_acc);
		initView();
		setTitle();
		initData();
		reflush(true);
	}

	private void initData() {
		quarter_type = StringUtil.noNull(getIntent().getStringExtra("quarter_type"));
		time = StringUtil.noNull(getIntent().getStringExtra("time"));
		zb_id = StringUtil.noNull(getIntent().getStringExtra("zb_id"));
		zb_name = StringUtil.noNull(getIntent().getStringExtra("zb_name"));
	}

	private void initView() {
		mWebview = (WebView) findViewById(R.id.webview);
		mTime = (TextView) findViewById(R.id.time);
		mValue = (TextView) findViewById(R.id.value);
		mRange = (TextView) findViewById(R.id.range);
		mHb = (TextView) findViewById(R.id.hb);
		mTb = (TextView) findViewById(R.id.tb);
		mData_from = (TextView) findViewById(R.id.data_from);
		mCompare = (TextView) findViewById(R.id.compare);
		mList_data = (MyListView) findViewById(R.id.list_data);
		mNo_ind = (TextView) findViewById(R.id.no_ind);
		mZbdy_layout = (LinearLayout) findViewById(R.id.zbdy_layout);
		mZbdy = (TextView) findViewById(R.id.zbdy);
		mKs = (TextView) findViewById(R.id.ks);
		mRule = (TextView) findViewById(R.id.rule);
		zbdy_btn = (TextView) findViewById(R.id.zbdy_btn);
		fsfqy = (TextView) findViewById(R.id.fsfqy); // 分室分区域对比
		alpha = (TextView) findViewById(R.id.alpha);
		mZb_flag_3_remark = (TextView) findViewById(R.id.zb_flag_3_remark);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.indicator_acc_popwin, null);
		zbdy_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (mZbdy_layout.getVisibility() == View.VISIBLE) {
					mZbdy_layout.setVisibility(View.GONE);

				} else {
					mZbdy_layout.setVisibility(View.VISIBLE);
				}

			}
		});
		fsfqy.setOnClickListener(new OnClickListener() {// 分室分区域对比

			public void onClick(View arg0) {
				alpha.setVisibility(View.VISIBLE);
				// TODO 当请求超时，indicatorAssResultData为空，会报错
				if (indicatorAssResultData != null) {
					popwin = new IndicatorPopupWindow(IndicatorDetailActivity.this, new OnClickListener() {

						public void onClick(View arg0) {
							popwin.dismiss();
						}
					}, indicatorAssResultData);
					popwin.setOnDismissListener(new OnDismissListener() {

						public void onDismiss() {
							alpha.setVisibility(View.GONE);
						}
					});
				}
				// 显示窗口
				if (popwin != null)// 当无数据的时候
				{
					popwin.showAtLocation(IndicatorDetailActivity.this.findViewById(R.id.indicator_acc_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				} else {
					alpha.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IndicatorDetailActivity.this.finish();

			}
		});
		if (center_tv != null)
			center_tv.setText("指标关联分析");

		if (right_btn != null) {
			right_btn.setVisibility(View.VISIBLE);
			right_btn.setBackgroundResource(R.drawable.ji_gou);
			right_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(IndicatorDetailActivity.this, IndicatorOrgActivity.class);
					intent.putExtra("zb_id", zb_id);
					intent.putExtra("quarter_type", quarter_type);
					startActivityForResult(intent, 100);
				}
			});
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);

		}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reflush(final boolean isReflushWebView) {

		myDialog = new LoadingDialog(this);
		myDialog.setTvMessage("正在查询..");
		if (!isFinishing()) {
			myDialog.show(true);
		}

		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "indicator_ass");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", quarter_type);
		params.put("time", time);
		params.put("org_id", org_id);
		params.put("org_type", org_type);
		params.put("zb_id", zb_id);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				if (IndicatorDetailActivity.this == null) {
					return;
				}
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (myDialog != null) {
					myDialog.dismiss();
				}
			}

			@Override
			public void onSuccess(Object t) {
				if (IndicatorDetailActivity.this == null) {
					return;
				}
				System.out.println("indicator_ass--" + t.toString());
				if (myDialog != null) {
					myDialog.dismiss();
				}
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfo != null) {
					if (JsonInfoV3.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据

						indicatorAssResultData = jsonInfo.getResultDataToClass(IndicatorAssResultData.class);
						relationDataList = indicatorAssResultData.relationDataList; // 指标关联
						if (indicatorAssResultData != null) { // 有数据
							mTime.setText(formateTime(time)); // 时间
							mRange.setText(StringUtil.noNull(indicatorAssResultData.rang)); // 合理区间
							String tValue = StringUtil.noNull(indicatorAssResultData.value);
							String tLoop = StringUtil.noNull(indicatorAssResultData.loop);
							String tTb = StringUtil.noNull(indicatorAssResultData.tb);
							if ("".equals(tValue)) {
								mValue.setText(""); // 当前值
							} else {
								if (indicatorAssResultData.data_type.equals("1")) {// 数据需要展示类型（1-小数
																					// 2-百分比
																					// 3-整数）
									mValue.setText(tValue);
								}
								if (indicatorAssResultData.data_type.equals("2")) {
									mValue.setText(mul(tValue, 100));
								}
								if (indicatorAssResultData.data_type.equals("3")) {
									tValue = getDecimalsInteger(tValue) + "";
									mValue.setText(tValue);
								}
							}
							mHb.setText(mul(tLoop, 100)); // 指标值环比
							mTb.setText(mul(tTb, 100));

							mData_from.setText(StringUtil.noNull(indicatorAssResultData.from));
							mCompare.setText(StringUtil.noNull(indicatorAssResultData.rang_explain));
							mZbdy.setText(StringUtil.noNull(indicatorAssResultData.custom_indicator));
							mKs.setText(StringUtil.noNull(indicatorAssResultData.division));
							mRule.setText(StringUtil.noNull(indicatorAssResultData.rule));
							indicatorAssResultData.zb_name = zb_name; // 设置指标名
							if (isReflushWebView) { // 需要熟悉指标关联
								loadWebview();
							}

						} else {
							Toast.makeText(getApplicationContext(), "获取无信息", 1200).show();//
							mTime.setText(""); // 时间
							mValue.setText(""); // 当前值
							mRange.setText(""); // 合理区间
							mHb.setText(""); // 指标值环比
							mTb.setText("");

							mData_from.setText("");
							mCompare.setText("");
							mZbdy.setText("");
							mKs.setText("");
							mRule.setText("");
						}

						if (relationDataList != null && relationDataList.size() > 0) // 有数据
						{
							mNo_ind.setVisibility(View.GONE);
							mList_data.setVisibility(View.VISIBLE);

							for (IndicatorAssResultDataRelation data : relationDataList) {
								if (!"".equals(data.getValue()) && "新工占比".equals(data.getZb_name())) {
									data.setValue(mul(data.getValue(), 100d));
								}
							}
							adpAdapter = new IndicatorAssNewAdapter(IndicatorDetailActivity.this, relationDataList);
							mList_data.setAdapter(adpAdapter);
						} else {
							mNo_ind.setVisibility(View.VISIBLE);
							mList_data.setVisibility(View.GONE);
						}

					} else {
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});

	}

	/**
	 * 加载图表
	 */
	private void loadWebview() {
		// 设置支持JavaScript等
		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		webSettings.setSupportMultipleWindows(true);
		mWebview.setWebChromeClient(new WebChromeClient());
//		mWebview.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		// 转换接口数据为 x,y数组
		List<IndicatorAssResultDataChart> trendDataList = indicatorAssResultData.trendDataList;// 指标趋势图数据
		StringBuilder linex = new StringBuilder();
		StringBuilder liney = new StringBuilder();
		String tempYData = "";
		for (int i = 0; i < trendDataList.size(); i++) {

			Object obj = trendDataList.get(i);
			if (obj.getClass() != IndicatorAssResultDataChart.class) {
				System.out.println("class:" + obj.getClass());
			} else {
				if (StringUtil.isNotEmpty(trendDataList.get(i).YData)) {
					if (StringUtil.noNull(indicatorAssResultData.data_type).equals("1")||StringUtil.noNull(indicatorAssResultData.data_type).equals("2")) {
						tempYData = trendDataList.get(i).YData;
					}
					if (StringUtil.noNull(indicatorAssResultData.data_type).equals("3")) {
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
		
//		for (int i = 0; i < trendDataList.size(); i++) {
//			linex.append(trendDataList.get(i).XData).append(",");
//			liney.append(trendDataList.get(i).YData).append(",");
//		}
//		String x = linex.toString().substring(0, linex.toString().length() - 1);
//		String y = liney.toString().substring(0, liney.toString().length() - 1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("line_x", x);
		map.put("line_y", y);
		map.put("zb_name", zb_name);
		final String data = JSON.toJSONString(map);

		mWebview.loadUrl("file:///android_asset/indicator_ass.htm");

		mWebview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				mWebview.loadUrl("javascript:init_data('" + data + "')");

			}
		});
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

		@JavascriptInterface
		public void getIndex(final int index, String xName) {
			System.out.println("接收js参数" + index + "   " + xName);
			formateWebDate(index, xName); // 格式化时间参数
			reflush(false);
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
	}

	/**
	 * 格式化webview图表索引回来的 数值拼接成服务端字符串
	 * 
	 * @param date
	 * @return
	 */
	private void formateWebDate(int index, String xName) {
		if (IndicatorActivity.currentDateType.equals(IndicatorActivity.DAY)) // 天为周期
		{
			String tempTime = time;
			// 2015-04-13
			String[] arr = tempTime.split("-");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append("-").append(arr[1].trim()).append("-").append(StringUtil.formateNum(xName));
			time = sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.WEEK)) // 周为周期
		{
			// 2015-03,1
			String tempTime = time;
			String[] arr = tempTime.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append(",").append(index + 1);
			time = sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.MONTH)) // 月为周期
		{
			// 2015-03
			String tempTime = time;
			String[] arr = tempTime.split("-");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append("-").append(StringUtil.formateNum(index + 1 + ""));
			time = sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.QUARTER)) // 季为周期
		{
			// 2015,1(2015 年第1季度)
			String tempTime = time;
			String[] arr = tempTime.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append(",").append(index + 1);
			time = sb.toString();
		}

		// return time;

	}

	/**
	 * 格式化日期字段
	 * 
	 * @param date
	 * @return
	 */
	private String formateTime(String time) {
		if (IndicatorActivity.currentDateType.equals(IndicatorActivity.DAY)) // 天为周期
		{
			String tempTime = time;
			// 2015-04-13
			String[] arr = tempTime.split("-");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append("年").append(arr[1].trim()).append("月").append(arr[2].trim()).append("日");
			return sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.WEEK)) // 周为周期
		{
			// 2015-03,1
			String tempTime = time;
			String[] arr = tempTime.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append(",").append("第" + arr[1].trim() + "周");
			return sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.MONTH)) // 月为周期
		{
			// 2015-03
			String tempTime = time;
			String[] arr = tempTime.split("-");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append("年").append(arr[1].trim() + "月");
			return sb.toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.QUARTER)) // 季为周期
		{
			// 2015,1(2015 年第1季度)
			String tempTime = time;
			String[] arr = tempTime.split(",");
			StringBuffer sb = new StringBuffer();
			sb.append(arr[0].trim()).append("年").append("第" + arr[1].trim() + "季度");
			return sb.toString();
		}
		return "";

		// return time;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 可以根据多个请求代码来作相应的操作
		if (20 == resultCode) {
			org_id = data.getExtras().getString("org_id");
			org_type = data.getExtras().getString("org_type");
			System.out.println("result_orgid" + org_id);
			reflush(true);
		}
	}

}
