package com.newland.comp.activity.my;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.EffictData;
import com.newland.comp.bean.my.EffictData2;
import com.newland.comp.bean.my.EffictDataExp;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的绩效
 * 
 * @author H81
 * 
 */
public class MyPerformanceActivity extends BaseActivity {
	private UserInfo userInfo;

	private String nowdate = StringUtil.getNowTime(StringUtil.MONTH_TIME);// 当前日期

	List<EffictData> list;// EffictData
	List<EffictData2> list2;// EffictData2

	private int page_index = 1;// 当前页
	private int page_rows = 5;// 每页显示几条

	private TextView mMyperformance_date;// 日期
	private TextView mMyperformance_totalscore;// 总得分
	private TextView mMyperformance_comevaluation;// 综合考评
	private TextView mMyperformance_performancegrade;// 绩效等级
	private TextView mMyperformance_evaluatecontent;// 评价内容
	private ImageView mMyperformance_evaluatecontent_line;// 评价内容下划线
	private WebView mMyperformance_webview;// webview
	// private TextView myperformance_tv_content;
	private Button dissent;// 我有异议

	private TextView right_tv;// 标题栏的日期显示
	// private View load_msg;
	String month;

	private void bindViews() {
		mMyperformance_date = (TextView) findViewById(R.id.myperformance_date);
		mMyperformance_totalscore = (TextView) findViewById(R.id.myperformance_totalscore);
		mMyperformance_comevaluation = (TextView) findViewById(R.id.myperformance_comevaluation);
		mMyperformance_performancegrade = (TextView) findViewById(R.id.myperformance_performancegrade);
		mMyperformance_evaluatecontent = (TextView) findViewById(R.id.myperformance_evaluatecontent);
		mMyperformance_evaluatecontent_line = (ImageView) findViewById(R.id.myperformance_evaluatecontent_line);
		mMyperformance_webview = (WebView) findViewById(R.id.myperformance_webview);
		// load_msg = findViewById(R.id.load_msg);
		dissent = (Button) findViewById(R.id.dissent);
		dissent.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (isClickable) {
					Intent dissentintent = new Intent(MyPerformanceActivity.this, MysalaryDissentActivity.class);
					dissentintent.putExtra("type", "my_effect");
					dissentintent.putExtra("time", right_tv.getText().toString());
					startActivity(dissentintent);
				} else {
					Toast.makeText(MyPerformanceActivity.this, "不在异议期内", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_perfor);

		setTitle();
		userInfo = (UserInfo) getIntent().getSerializableExtra("userinfo");
		reflush();// 网络请求数据
		CURRENTACTIVITY = "MyPerformanceActivity";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CURRENTACTIVITY = "";
	}

	/**
	 * 设置webview内容
	 * 
	 * @param mMyperformance_webview2
	 * @param effictDataExp
	 */
	private void initWebView(final WebView mMyperformance_webview2, EffictDataExp effictDataExp) {
		WebSettings webSettings = mMyperformance_webview2.getSettings();
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		mMyperformance_webview2.getSettings().setUseWideViewPort(true);
		mMyperformance_webview2.getSettings().setLoadWithOverviewMode(true);

		webSettings.setJavaScriptEnabled(true);

		webSettings.setSupportMultipleWindows(true);
		mMyperformance_webview2.setWebChromeClient(new WebChromeClient());
//		mMyperformance_webview2.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		mMyperformance_webview2.loadUrl("file:///android_asset/column-performance.htm");

		final String data = JSON.toJSONString(effictDataExp);
		mMyperformance_webview2.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				// mWebView.loadUrl("javascript:test()");
				mMyperformance_webview2.loadUrl("javascript:init_data('" + data + "')");
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
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		bindViews();
		if (center_tv != null)
			center_tv.setText("我的绩效");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.VISIBLE);
			// 判断textview里面是否有时间内容
			right_tv.setText(nowdate);
			right_tv.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					new DatePickDialog(MyPerformanceActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(right_tv, MyPerformanceActivity.this);
				}
			});
		}
	}

	/**
	 * 网络请求数据
	 */
	@Override
	public void reflush() {
		initDialog(this);

		month = right_tv.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("userid", userInfo.getUserid());
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userInfo.getUserid()));
		params.put("method", "my_effect");
		params.put("page_index", page_index + "");// 当前页
		params.put("page_row", page_rows + "");// 每页显示几条
		params.put("month", month);// 当前日期
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(getApplicationContext(), "连接超时，请重试", Toast.LENGTH_SHORT).show();
				if (MyPerformanceActivity.this == null) {
					return;
				}
				// load_msg.setVisibility(View.GONE);
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				if (MyPerformanceActivity.this == null) {
					return;
				}
				if (StringUtil.isNotEmpty(month)) {
					isClickAble(MyPerformanceActivity.this, "month", month, "my_effect");
				}
				System.out.println(t.toString());
				// load_msg.setVisibility(View.GONE);
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", Toast.LENGTH_SHORT).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						list = jsonInfo.getDataList(EffictData.class);
						list2 = jsonInfo.getDataList2(EffictData2.class);
						EffictDataExp dataExp = jsonInfo.getData_exp(EffictDataExp.class);
						if (dataExp != null) {
							dataExp.setDate(right_tv.getText().toString());// 当前月份
							mMyperformance_date.setText(StringUtil.noNull(dataExp.getDate()));
							initWebView(mMyperformance_webview, dataExp);// 设置webview
							mMyperformance_totalscore.setText(StringUtil.noNull(dataExp.getKpi_score(), "0"));// 总得分
							mMyperformance_comevaluation.setText(StringUtil.noNull(dataExp.getKpi_com(), "0"));// 综合考评
							mMyperformance_performancegrade.setText(StringUtil.noNull(dataExp.getKpi_level(), "0"));// 绩效等级
							if (StringUtil.isEmpty(dataExp.getKpi_comment())) {// 评价内容为空就隐藏评价控件
								// mMyperformance_evaluatecontent.setVisibility(View.GONE);
								mMyperformance_evaluatecontent.setText("未评价");
								mMyperformance_evaluatecontent_line.setVisibility(View.GONE);
							} else {
								mMyperformance_evaluatecontent.setVisibility(View.VISIBLE);
								mMyperformance_evaluatecontent_line.setVisibility(View.VISIBLE);
								mMyperformance_evaluatecontent.setText(dataExp.getKpi_comment());// 评价内容
							}
						} else {
							mMyperformance_totalscore.setText("0");// 总得分
							mMyperformance_comevaluation.setText("0");// 综合考评
							mMyperformance_performancegrade.setText("0");// 绩效等级
							Toast.makeText(getApplicationContext(), "当月无数据", Toast.LENGTH_SHORT).show();// 显示当月无数据提示
						}

					} else {
						// 访问服务失败，隐藏评论控件和它的下划线
						mMyperformance_evaluatecontent.setVisibility(View.GONE);
						mMyperformance_evaluatecontent_line.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), Toast.LENGTH_SHORT).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	public void onClick(View arg0) {
		if (arg0.getId() == R.id.myperformance_rl_detail) {// 绩效明细布局点击事件
			Bundle bundle = new Bundle();
			bundle.putString("nowdate", right_tv.getText().toString());
			bundle.putSerializable("userinfo", userInfo);
			Intent intent = new Intent(getApplicationContext(), MyPerformanceDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		if (arg0.getId() == R.id.head_left_btn) {// 标题栏左侧“返回”按钮
			this.finish();
		}
	}

	@Override
	public void setDissentBtnColor() {
		if (isClickable) {
			dissent.setTextColor(dissentColor);
		} else {
			dissent.setTextColor(dissentColor);
		}
	}

}
