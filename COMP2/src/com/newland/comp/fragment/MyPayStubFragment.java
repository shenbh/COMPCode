package com.newland.comp.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MysalaryDissentActivity;
import com.newland.comp.activity.my.SalaryDetailActivity2;
import com.newland.comp.activity.process.PrYandFActivity;
import com.newland.comp.bean.CustomPayBill;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.PayBill;
import com.newland.comp.bean.my.PayBillExp;
import com.newland.comp.bean.my.SixMonthSalaryColumnCharts;
import com.newland.comp.bean.my.SixMonthSalaryPieCharts;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 工资单
 * 
 * @author H81
 * 
 */
public class MyPayStubFragment extends BaseFragment implements OnClickListener {
	Context context;
	private View mView;
	private TextView realTextView;
	private TextView shouldTextView; // //应发
	private Button dissent;// 我有异议

	private WebView webView;
	private ImageView mysalary_nextfragment;
	private ImageView mysalary_lastfragment;
	private SixMonthSalaryPieCharts sixMonthSalaryPieCharts;// 饼图
	private SixMonthSalaryColumnCharts sixMonthSalaryColumnCharts;// 柱状图

	private PayBill realPayBill;// 实际工资单
	private PayBill shouldPayBill;// 应际工资单
	private List<CustomPayBill> realChildPaybiBills; // 实发工资
	private List<CustomPayBill> shoulChildPayBills; // 应发工资
	private List<CustomPayBill> allChildPayBills = new ArrayList<CustomPayBill>(); // 应发工资实发工资列表合计
	private View no_data_layout;
	private View load_msg;
	private String month;// 时间

	public MyPayStubFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		System.out.println("MyPayStubFragment第一次启动");
	}

	public static MyPayStubFragment newInstance(Context context) {
		return new MyPayStubFragment(context);
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 实发工资
		case R.id.layout_shifa:
		case R.id.mysalary_shifa:
			if (realPayBill == null || realChildPaybiBills.size() == 0) {
				return;
			}
			Intent intent = new Intent(getActivity(), SalaryDetailActivity2.class);

			ArrayList list = new ArrayList();
			list.add(allChildPayBills);
			// list.add(shoulChildPayBills);

			Bundle bundle = new Bundle();
			bundle.putSerializable("sfParent", realPayBill);
			bundle.putParcelableArrayList("childs", list);
			bundle.putSerializable("yFparent", shouldPayBill);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 应发工资
		case R.id.layout_yingfa:
		case R.id.mysalary_yingfa:
			if (shouldPayBill == null || shoulChildPayBills.size() == 0) {
				return;
			}
			Intent intent1 = new Intent(getActivity(), SalaryDetailActivity2.class);
			ArrayList list2 = new ArrayList();
			list2.add(allChildPayBills);

			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("sfParent", realPayBill);
			bundle2.putParcelableArrayList("childs", list2);
			bundle2.putSerializable("yFparent", shouldPayBill);
			intent1.putExtras(bundle2);

			startActivity(intent1);
			break;
		case R.id.mysalary_lastfragment:// 上一个fragment
			initWebView(webView, sixMonthSalaryColumnCharts);
			mysalary_lastfragment.setVisibility(View.INVISIBLE);
			mysalary_nextfragment.setVisibility(View.VISIBLE);
			break;
		case R.id.mysalary_nextfragment:// 下一个fragment
			initWebView(webView, sixMonthSalaryPieCharts);
			mysalary_lastfragment.setVisibility(View.VISIBLE);
			mysalary_nextfragment.setVisibility(View.INVISIBLE);
			break;
		case R.id.mysalary_dissent:// 我有异议
			if (isClickable) {
				Intent dissentintent = new Intent(getActivity(), MysalaryDissentActivity.class);
				dissentintent.putExtra("type", "my_payroll");
				dissentintent.putExtra("time", month);
				startActivity(dissentintent);
			} else {
				Toast.makeText(getActivity(), "不在异议期内", 1000).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.my_salary, container, false);
		realTextView = (TextView) mView.findViewById(R.id.tv_realPay); // 实发
		shouldTextView = (TextView) mView.findViewById(R.id.tv_shouldPay); // 应发

		ImageView shifa = (ImageView) mView.findViewById(R.id.mysalary_shifa);// “实发金额”按钮
		ImageView yingfa = (ImageView) mView.findViewById(R.id.mysalary_yingfa);// “实发金额”布局
		View layout_shifa = mView.findViewById(R.id.layout_shifa);// “应发金额”按钮
		View layout_yingfa = mView.findViewById(R.id.layout_yingfa);// “应发金额”布局
		mysalary_nextfragment = (ImageView) mView.findViewById(R.id.mysalary_nextfragment);// 下一个fragment
		mysalary_lastfragment = (ImageView) mView.findViewById(R.id.mysalary_lastfragment);// 上一个fragment
		mysalary_lastfragment.setVisibility(View.INVISIBLE);
		dissent = (Button) mView.findViewById(R.id.mysalary_dissent);// 我有异议
		shifa.setOnClickListener(this);
		layout_shifa.setOnClickListener(this);
		yingfa.setOnClickListener(this);
		layout_yingfa.setOnClickListener(this);
		mysalary_nextfragment.setOnClickListener(this);
		mysalary_lastfragment.setOnClickListener(this);
		dissent.setOnClickListener(this);
		// dissent.setClickable(flag);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		webView = (WebView) mView.findViewById(R.id.my_salary_webview);

		realChildPaybiBills = new ArrayList<CustomPayBill>();
		shoulChildPayBills = new ArrayList<CustomPayBill>();
		load_msg = mView.findViewById(R.id.load_msg);
		refresh();
		return mView;
	}

	/**
	 * 我的薪酬-柱状图
	 * 
	 * @param webView
	 * @param sixMonthSalaryCharts
	 */
	private void initWebView(final WebView webView, SixMonthSalaryColumnCharts sixMonthSalaryCharts) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		webView.loadUrl("file:///android_asset/column_six_month_analysis.htm");

		final String data = JSON.toJSONString(sixMonthSalaryCharts);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				webView.loadUrl("javascript:init_data('" + data + "')");
			}
		});
	}

	/**
	 * 我的薪酬-饼图
	 * 
	 * @param webView
	 * @param sixMonthSalaryPieCharts
	 */
	private void initWebView(final WebView webView, SixMonthSalaryPieCharts sixMonthSalaryPieCharts) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		webView.loadUrl("file:///android_asset/pie_six_month_analysis.htm");

		final String data = JSON.toJSONString(sixMonthSalaryPieCharts);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				webView.loadUrl("javascript:init_data('" + data + "')");
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

	public void findParentPaybill(List<PayBill> payBills) {
		realPayBill = null;
		shouldPayBill = null; // 应发
		for (int i = 0; i < payBills.size(); i++) {
			PayBill tempPayBill = payBills.get(i);

			if (tempPayBill.getPay_level().equalsIgnoreCase("0")) {
				if (tempPayBill.getPay_value().equalsIgnoreCase("实发金额")) {
					realPayBill = tempPayBill;
				} else if (tempPayBill.getPay_value().equalsIgnoreCase("应发金额")) {
					shouldPayBill = tempPayBill;
				}
			}
		}
		if (realPayBill != null) {
			realTextView.setText(realPayBill.getPay_num());
		}
		if (shouldPayBill != null) {
			shouldTextView.setText(shouldPayBill.getPay_num());
		}
		findChildPayBills(payBills);
	}

	public void findChildPayBills(List<PayBill> payBills) {
		realChildPaybiBills.clear();
		shoulChildPayBills.clear();

		// 一级列表
		for (int i = 0; i < payBills.size(); i++) {
			PayBill tempPayBill = payBills.get(i);
			if (tempPayBill.getPay_level().equals("1")) {
				if (tempPayBill.getPid().equals(realPayBill.getPay_id())) {
					// 实发金额列表
					CustomPayBill tempCustomPayBill = new CustomPayBill();
					tempCustomPayBill.setPayBill(tempPayBill);
					tempCustomPayBill.setChildPayBills(new ArrayList<PayBill>());
					realChildPaybiBills.add(tempCustomPayBill);
				} else if (tempPayBill.getPid().equals(shouldPayBill.getPay_id())) {
					// 应发金额列表
					CustomPayBill tempCustomPayBill = new CustomPayBill();
					tempCustomPayBill.setPayBill(tempPayBill);
					tempCustomPayBill.setChildPayBills(new ArrayList<PayBill>());
					shoulChildPayBills.add(tempCustomPayBill);
				}
			}
		}
		// 二级列表
		for (int i = 0; i < payBills.size(); i++) {
			PayBill tempPayBill = payBills.get(i);
			if (tempPayBill.getPay_level().equals("2")) {
				Boolean isFind = false;
				for (int j = 0; j < realChildPaybiBills.size(); j++) {
					CustomPayBill customPayBill = realChildPaybiBills.get(j);
					if (tempPayBill.getPid().equals(customPayBill.getPayBill().getPay_id())) {
						customPayBill.getChildPayBills().add(tempPayBill);
						isFind = true;
						break;
					}
				}
				if (!isFind) {
					for (int j = 0; j < shoulChildPayBills.size(); j++) {
						CustomPayBill customPayBill = shoulChildPayBills.get(j);
						if (tempPayBill.getPid().equals(customPayBill.getPayBill().getPay_id())) {
							customPayBill.getChildPayBills().add(tempPayBill);
							isFind = true;
							break;
						}
					}
				}
			}
		}

		// 合并成总集合
		allChildPayBills.clear();
		allChildPayBills.addAll(shoulChildPayBills);
		allChildPayBills.addAll(realChildPaybiBills);

	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void refresh() {
		load_msg.setVisibility(View.VISIBLE);
		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "userid_payroll");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				Toast.makeText(context, strMsg, 1000).show();
				if (load_msg != null)
					load_msg.setVisibility(View.GONE);

			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (StringUtil.isNotEmpty(month)) {
					isClickAble("month", month, "my_payroll");
				}
				System.out.println(t.toString());
				if (load_msg != null)
					load_msg.setVisibility(View.GONE);

				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						// 服务器传回来的用户信息
						List<PayBill> list = jsonInfo.getDataList(PayBill.class);
						if (list != null && list.size() > 0) {
							findParentPaybill(list);
						} else {
							// Toast.makeText(context, "无当月数据", 1000).show();
							realTextView.setText("0");
							shouldTextView.setText("0");
						}

						PayBillExp payBillExp = jsonInfo.getData_exp(PayBillExp.class);
						if (payBillExp != null) {
							// 柱状图
							sixMonthSalaryColumnCharts = new SixMonthSalaryColumnCharts();
							sixMonthSalaryColumnCharts.setMonth_series_data(payBillExp.getHistogram_name());
							sixMonthSalaryColumnCharts.setMoney_series_data(payBillExp.getHistogram_value());

							// 饼图
							sixMonthSalaryPieCharts = new SixMonthSalaryPieCharts();
							sixMonthSalaryPieCharts.setPie_name(payBillExp.getPie_name());
							sixMonthSalaryPieCharts.setPie_value(payBillExp.getPie_value());

							if (mysalary_lastfragment.getVisibility() == View.VISIBLE) {
								initWebView(webView, sixMonthSalaryPieCharts);
							} else if (mysalary_nextfragment.getVisibility() == View.VISIBLE) {
								initWebView(webView, sixMonthSalaryColumnCharts);
							}
						} else {
							// 柱状图
							sixMonthSalaryColumnCharts = new SixMonthSalaryColumnCharts();
							sixMonthSalaryColumnCharts.setMonth_series_data("0,0,0,0,0");
							sixMonthSalaryColumnCharts.setMoney_series_data("0,0,0,0,0");

							// 饼图
							sixMonthSalaryPieCharts = new SixMonthSalaryPieCharts();
							sixMonthSalaryPieCharts.setPie_name("0,0,0,0,0");
							sixMonthSalaryPieCharts.setPie_value("100,0,0,0,0");

							if (mysalary_lastfragment.getVisibility() == View.VISIBLE) {
								initWebView(webView, sixMonthSalaryPieCharts);
							} else if (mysalary_nextfragment.getVisibility() == View.VISIBLE) {
								initWebView(webView, sixMonthSalaryColumnCharts);
							}
						}
					} else {// 显示登陆失败信息
						Toast.makeText(context, jsonInfov2.getResultDesc(), 1000).show();
					}
				}

			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// refresh();
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
