package com.newland.comp.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.indicator.IndicatorAssResultData;
import com.newland.comp.bean.indicator.IndicatorAssResultDataChart;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

public class IndicatorPopupWindow extends PopupWindow {

	private Button close;
	private View mMenuView;
	public List<IndicatorAssResultDataChart> childDataList;// 分科室区域数据
	private RelativeLayout mTop_layout;
	private TextView mPop_title;
	private WebView mWebview;
	private LinearLayout ks_layout;
	private Context context;
	private TextView service;// 省客户服务中心
	IndicatorAssResultData bean;// 指标关联所有返回的数据

	public IndicatorPopupWindow(final Activity context, OnClickListener itemsOnClick, IndicatorAssResultData bean) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bean = bean;
		this.childDataList = bean.childDataList;
		this.context = context;
		mMenuView = inflater.inflate(R.layout.indicator_acc_popwin, null);
		close = (Button) mMenuView.findViewById(R.id.close);
		// 取消按钮
		close.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				context.findViewById(R.id.alpha).setVisibility(View.GONE);
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setTouchable(true);
		this.setOutsideTouchable(false);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.top_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						context.findViewById(R.id.alpha).setVisibility(View.GONE);
						dismiss();
					}
				}
				return true;
			}
		});

		bindViews();
		initData();
	}

	private void initData() {
		// 每3个一行 对比数据
		LinearLayout mainLayout = new LinearLayout(context);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp1.leftMargin = 10;
		lp1.weight = 1;
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp2.weight = 1;
		for (int i = 0; i < childDataList.size(); i++) {

			String tempYData = "";

			if (this.bean.data_type.equals("1")) {// 数据需要展示类型（1-小数 2-百分比 3-整数）
			}
			if (this.bean.data_type.equals("2")) {
				if (StringUtil.isEmpty(childDataList.get(i).YData)) {
					tempYData = "0";
				} else {
					tempYData = mul(Double.parseDouble(childDataList.get(i).YData), 100d) + "%";
				}
			}
			if (this.bean.data_type.equals("3")) {

			}

			if (!childDataList.get(i).XData.equals("省客户服务中心")) {// 请假原因另起一行显示
				if (mainLayout.getChildCount() < 2) { // 显示一行
					TextView tx = new TextView(context);
					tx.setText(childDataList.get(i).XData + ":  " + tempYData);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(48, 48, 48));
					tx.setGravity(Gravity.LEFT);
					// tx.setPadding(10, 0, 10, 0);
					mainLayout.addView(tx, lp1);
					if (mainLayout.getChildCount() == 2 || i + 1 == childDataList.size()) {
						ks_layout.addView(mainLayout, mainlp);
					}
				} else {
					mainLayout = new LinearLayout(context);
					mainLayout.setOrientation(LinearLayout.HORIZONTAL);
					TextView tx = new TextView(context);
					tx.setText(childDataList.get(i).XData + ":  " + tempYData);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(48, 48, 48));
					tx.setGravity(Gravity.LEFT);
					mainLayout.addView(tx, lp1);
					if (mainLayout.getChildCount() == 2 || i + 1 == childDataList.size()) {
						ks_layout.addView(mainLayout, mainlp);
					}
				}
			} else {
				service.setText(childDataList.get(i).XData + ":  " + tempYData);
			}

		}
		if (ks_layout.getChildCount() == 0) // 说明不满一行
		{
			ks_layout.addView(mainLayout);
		}

		// 把有子节点的找出来
		List<IndicatorAssResultDataChart> dist = new ArrayList<IndicatorAssResultDataChart>();
		for (IndicatorAssResultDataChart indicatorAssResultDataChart : childDataList) {
			if (indicatorAssResultDataChart.childList != null && indicatorAssResultDataChart.childList.size() > 0) { // 说明有子节点
				indicatorAssResultDataChart.setYData("" + Double.parseDouble(StringUtil.noNull(indicatorAssResultDataChart.getYData(), "0")) * 100);
				for (IndicatorAssResultDataChart child : indicatorAssResultDataChart.childList) {
					child.setYData("" + Double.parseDouble(StringUtil.noNull(child.getYData(), "0")) * 100);
				}
				dist.add(indicatorAssResultDataChart);
			}
		}
		loadWebview(dist);
	}

	/**
	 * 加载图表
	 * 
	 * @param tempks
	 */
	private void loadWebview(List<IndicatorAssResultDataChart> dist) {
		// 设置支持JavaScript等
		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		webSettings.setSupportMultipleWindows(true);
		mWebview.setWebChromeClient(new WebChromeClient());
		final String data = JSON.toJSONString(dist);
		mWebview.loadUrl("file:///android_asset/indicator_com3.htm");

		mWebview.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				// mWebView.loadUrl("javascript:test()");

				mWebview.loadUrl("javascript:init_data('" + data + "')");

			}
		});
	}

	private void bindViews() {

		mTop_layout = (RelativeLayout) mMenuView.findViewById(R.id.top_layout);
		mPop_title = (TextView) mMenuView.findViewById(R.id.pop_title);
		mWebview = (WebView) mMenuView.findViewById(R.id.webview);
		ks_layout = (LinearLayout) mMenuView.findViewById(R.id.ks_layout);
		service = (TextView) mMenuView.findViewById(R.id.service);
	}

	/**
	 * 俩double型数据相乘
	 */
	public double mul(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.multiply(bd2).doubleValue();
	}

}
