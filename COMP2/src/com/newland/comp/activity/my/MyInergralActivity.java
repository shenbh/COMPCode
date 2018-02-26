package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.hr.HrIntegralShopActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.IntegralData;
import com.newland.comp.bean.my.IntegralExp;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 我的积分
 * 
 * @author H81
 * 
 */
public class MyInergralActivity extends BaseActivity {

	private Spinner spinner_period; // 时间周期
	private String[] quarter = { "第一季度", "第二季度", "第三季度", "第四季度" };
	private WebView webView;
	private TextView mSpinner_year;
	private Spinner mQuarter;
	private Button mBtn_user_selected;
	private TextView mTotalscore;
	private TextView seaon_integral;
	private TextView mLevel_score;
	private TextView work_age_sum; // 工龄积分
	private RelativeLayout mJf_detail;
	private TextView current_activity; // 当前活动
	private View current_activity_layout;// 当前活动view
	int page_index = 1;// 当前页码
	// int page_row = 15;//每页显示几条数据
	private ArrayList<IntegralData> list;
	private IntegralExp integralExp;
	TextView head_center_title;
	ImageButton head_left_btn;
	private Button dissent;// 我有异议
	String quarterStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_integral);
		initView();
		initData();
	}

	private void initView() {
		spinner_period = (Spinner) findViewById(R.id.quarter);
		ActivityUtil.showDropDown(this, spinner_period, quarter, R.layout.simple_spinner_item);
		webView = (WebView) findViewById(R.id.webview);
		mSpinner_year = (TextView) findViewById(R.id.spinner_year);
		mQuarter = (Spinner) findViewById(R.id.quarter);
		mBtn_user_selected = (Button) findViewById(R.id.btn_user_selected);
		mTotalscore = (TextView) findViewById(R.id.totalscore);
		seaon_integral = (TextView) findViewById(R.id.seaon_integral);
		mLevel_score = (TextView) findViewById(R.id.level_score);
		work_age_sum = (TextView) findViewById(R.id.work_age_sum);
		mJf_detail = (RelativeLayout) findViewById(R.id.jf_detail);
		head_center_title = (TextView) findViewById(R.id.head_center_title);
		current_activity = (TextView) findViewById(R.id.current_activity);// 当前活动
		current_activity_layout = findViewById(R.id.current_activity_layout);
		head_center_title.setText("我的积分");
		head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				MyInergralActivity.this.finish();

			}
		});
		mSpinner_year.setText(StringUtil.getNowTime(StringUtil.YEAR_TIME));
		mSpinner_year.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				new DatePickDialog(MyInergralActivity.this, DatePickDialog.YEAR).datePicKDialog(mSpinner_year);

			}
		});
		setDefaultQuarter();
		dissent = (Button) findViewById(R.id.dissent);
		dissent.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.e("newland:flag", isClickable + "");
				if (isClickable) {
					Intent dissentintent = new Intent(MyInergralActivity.this, MysalaryDissentActivity.class);
					dissentintent.putExtra("type", "my_integral");
					dissentintent.putExtra("time", mSpinner_year.getText().toString() + "," + (mQuarter.getSelectedItemPosition() + 1));
					startActivity(dissentintent);
				} else {
					Toast.makeText(MyInergralActivity.this, "不在异议期内", 1000).show();
				}
			}
		});
		// dissent.setClickable(flag);
		// 当前活动链接
		current_activity_layout.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(MyInergralActivity.this, HrIntegralShopActivity.class);
				startActivity(intent);

			}
		});

	}

	/**
	 * 设置默认季度
	 */
	private void setDefaultQuarter() {
		String nows = StringUtil.getNowTime(StringUtil.MONTH_TIME);
		String[] arr = nows.split("-"); // 把月份切割
		if (arr[1].equals("01") || arr[1].equals("02") || arr[1].equals("03")) {
			String year = (Integer.parseInt(mSpinner_year.getText().toString()) - 1) + "";
			mSpinner_year.setText(year); // 如果是 2015-3 默认调整 上一年 第四季度
			mQuarter.setSelection(3); // 第四季度
		} else if (arr[1].equals("04") || arr[1].equals("05") || arr[1].equals("06")) {
			mQuarter.setSelection(0);
		} else if (arr[1].equals("07") || arr[1].equals("08") || arr[1].equals("09")) {
			mQuarter.setSelection(1);
		} else if (arr[1].equals("10") || arr[1].equals("11") || arr[1].equals("12")) {
			mQuarter.setSelection(2);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initData() {
		initDialog(this);

		quarterStr = mSpinner_year.getText().toString() + "," + (mQuarter.getSelectedItemPosition() + 1);

		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "my_integral");
		// quarter: 查询季度 2015,1 第一季度 2015,2第二季度
		params.put("quarter", quarterStr);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", page_index + "");
		params.put("page_row", ActivityUtil.pageRow);

		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(60 * 1000);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				Toast.makeText(getApplicationContext(), strMsg, 1500).show();
				if (MyInergralActivity.this == null) {
					return;
				}
			}

			@Override
			public void onSuccess(Object t) {
				if (MyInergralActivity.this == null) {
					return;
				}
				if (StringUtil.isNotEmpty(quarterStr)) {
					isClickAble(MyInergralActivity.this, "quarte", quarterStr, "my_integral");
				}
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						integralExp = jsonInfo.getData_exp(IntegralExp.class);
						list = (ArrayList<IntegralData>) jsonInfo.getDataList(IntegralData.class); // 内部有做非空处理，无数据返回空集合
						if (integralExp != null) {
							mTotalscore.setText(StringUtil.noNull(integralExp.getCom_integral_sum(), "0"));
							seaon_integral.setText(StringUtil.noNull(integralExp.quarte_indicator_score, "0"));
							mLevel_score.setText(StringUtil.noNull(integralExp.exchange_integral, "0"));
							work_age_sum.setText(StringUtil.noNull(integralExp.work_age_sum, "0"));
							current_activity.setText(StringUtil.noNull("当前活动:" + integralExp.activity, "当前活动:0"));
							String getIs_save = StringUtil.noNull(integralExp.getIs_save());
							// 设置标题
							if (getIs_save.equals("")) {
								head_center_title.setText("我的积分");
							} else {
								head_center_title.setText("我的积分(" + getIs_save + ")");
							}
							initWebView();

						} else {
							mTotalscore.setText(StringUtil.noNull("0"));
							seaon_integral.setText(StringUtil.noNull("0"));
							mLevel_score.setText(StringUtil.noNull("0"));
							work_age_sum.setText(StringUtil.noNull("0"));
							current_activity.setText("当前活动:0");
						}
						if (list.size() == 0) {
							Toast.makeText(getApplicationContext(), "无这个季度积分明细", 1500).show();
						}

					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1500).show();
					}
				}

			}
		});
	}

	private void initWebView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl("file:///android_asset/my_inergral.htm");
		final String data = JSON.toJSONString(integralExp);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {

				webView.loadUrl("javascript:init_data('" + data + "')");

			}
		});
	}

	/**
	 * 积分明细
	 * 
	 * @param view
	 */
	public void btn_kf_detail(View view) {
		if (list != null) {
			Intent intent = new Intent(this, MyInergralDetailActivity.class);

			Bundle bundleObject = new Bundle();
			bundleObject.putSerializable("list", list);
			bundleObject.putString("quarter", mSpinner_year.getText().toString() + "," + (mQuarter.getSelectedItemPosition() + 1));
			intent.putExtras(bundleObject);
			startActivity(intent);
		} else {
			Toast.makeText(this, "无数据", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	/**
	 * 查询
	 * 
	 * @param view
	 */
	public void btn_search(View view) {

		initData();
		Log.e("newland:flag", isClickable + "");
	}

	/**
	 * 当前活动跳转链接
	 * 
	 * @param view
	 */
	public void btn_current(View view) {

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
