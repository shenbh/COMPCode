package com.newland.comp.fragment;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyAttendanceOverTimeActivity;
import com.newland.comp.activity.my.MyPerformanceActivity;
import com.newland.comp.activity.my.MysalaryDissentActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.OverTimeCountData;
import com.newland.comp.bean.my.OverTimeCountExp;
import com.newland.comp.bean.my.PayBill;
import com.newland.comp.bean.my.PayBillExp;
import com.newland.comp.bean.my.SixMonthSalaryColumnCharts;
import com.newland.comp.bean.my.SixMonthSalaryPieCharts;
import com.newland.comp.fragment.MyPayStubFragment.DemoJavaScriptInterface;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 加班统计
 * 
 * @author H81
 * 
 */
public class AttendanceOverTimeFragment extends BaseFragment {
	Context context;

	private RelativeLayout mRl_attendance_detail;// 考勤明细
	private WebView webView;
	private TextView overtime_sum;// 加班总计
	private OverTimeCountExp overTimeCountExp;// 柱状图
	private Button dissent;//我有异议
//	boolean flag = false;
	
	private View mView;
	private String month;// 月份

	private int page_index = 1;// 当前页码
//	private int page_row = 4;// 每页显示条数

	TextView date;

	public AttendanceOverTimeFragment(Context context) {
		this.context = context;
	}

	public static AttendanceOverTimeFragment newInstance(Context context) {
		return new AttendanceOverTimeFragment(context);
	}

	
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		System.out.println("MyPayStubFragment第一次启动");
	}

	
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.rl_attendance_detail://加班明细
			Intent intent = new Intent(getActivity(), MyAttendanceOverTimeActivity.class);
			intent.putExtra("month", month);
			startActivity(intent);
			break;
		case R.id.dissent://我有异议
			if (isClickable) {
				Intent dissentintent=new Intent(getActivity(), MysalaryDissentActivity.class);
				dissentintent.putExtra("type", "my_kq_jb");
				dissentintent.putExtra("time", month);//传递时间
				startActivity(dissentintent);
			}else
			{
				Toast.makeText(getActivity(), "不在异议期内", 1000).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		page_index = 1;
		
		mView = inflater.inflate(R.layout.my_attendance_overtime, container, false);
		mRl_attendance_detail = (RelativeLayout) mView.findViewById(R.id.rl_attendance_detail);
		mRl_attendance_detail.setOnClickListener(this);

		dissent=(Button) mView.findViewById(R.id.dissent);
		dissent.setOnClickListener(this);
		webView = (WebView) mView.findViewById(R.id.webview);
		overtime_sum = (TextView) mView.findViewById(R.id.overtime_sum);
		refresh();

		initWebView(webView, overTimeCountExp);

		return mView;
	}

	/**
	 * 加班统计-柱状图
	 * 
	 * @param webView
	 * @param sixMonthSalaryCharts
	 */
	private void initWebView(final WebView webView, OverTimeCountExp overTimeCountExp) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		webView.loadUrl("file:///android_asset/column_overtime.htm");

		final String data = JSON.toJSONString(overTimeCountExp);
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

	@SuppressWarnings("unchecked")
	@Override
	public void refresh() {
		

		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "my_kq_jb");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);// 查询季度
		params.put("page_index", page_index + "");// 当前页码
		params.put("page_row",ActivityUtil.pageRow);// 每页显示几条
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				Toast.makeText(context, strMsg, 1000).show();
				// load_msg.setVisibility(View.GONE);
				
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(t.toString());
				// load_msg.setVisibility(View.GONE);
				if(getActivity()==null) //说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (StringUtil.isNotEmpty(month)) {
					isClickAble("month", month, "my_kq_jb");
				}
				
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
			

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(StringUtil.noNull(jsonInfov2.getResultCode()))) // 正常返回数据
					{
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						OverTimeCountExp payBillExp = jsonInfo.getData_exp(OverTimeCountExp.class);

						if (payBillExp != null) {
							// 柱状图
							payBillExp.setDate(month);
							overTimeCountExp = new OverTimeCountExp();
							overTimeCountExp.setChar_x(StringUtil.noNull(payBillExp.getChar_x()));
							overTimeCountExp.setChar_y(StringUtil.noNull(payBillExp.getChar_y()));
							overTimeCountExp.setDate(StringUtil.noNull(payBillExp.getDate()));
							overtime_sum.setText(payBillExp.getJb_sum()+"小时");
							initWebView(webView, overTimeCountExp);
							if (StringUtil.isEmpty(payBillExp.getChar_x())) {
								Toast.makeText(context, "无当月数据", 1000).show();
							}
						} else {
							overtime_sum.setText("0");
						}
					} else {// 显示登陆失败信息
						Toast.makeText(context, StringUtil.noNull(jsonInfov2.getResultDesc()), 1000).show();
					}
				}
			}
		});
		
	}
	@Override
	public void setDissentBtnColor(){
		if (isClickable) {
			dissent.setTextColor(dissentColor);
		} else {
			dissent.setTextColor(dissentColor);
		}
	}
}