package com.newland.comp.fragment;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyAttendanceStatisticsActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.FilingTotalExp;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 考勤统计
 * 
 * @author H81
 * 
 */
public class AttendanceStatisticsFragment extends BaseFragment {
	Context context;
	private View mView;
	private WebView webView;
	private RelativeLayout mRl_attendance_detail;// 考勤明细
	private FilingTotalExp filingTotalExp;// 柱状图
	private int page_index = 1;// 当前页码
	private int page_row = 10;// 每页显示条数
	private String month;

	private TextView mLate_sum;// 迟到次数
	private TextView mLeaveearly_sum;// 早退次数
	private TextView mAbsent_sum;// 旷工次数

	LoadingDialog dialog;
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		System.out.println("MyPayStubFragment第一次启动");

	}

	public AttendanceStatisticsFragment(Context context) {
		this.context = context;
	}

	public static AttendanceStatisticsFragment newInstance(Context context) {
		return new AttendanceStatisticsFragment(context);
	}

	
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.rl_attendance_detail:
			Intent intent = new Intent(getActivity(), MyAttendanceStatisticsActivity.class);
			intent.putExtra("month", month);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		page_index=1;
		dialog=new LoadingDialog(getActivity());
		
		mView = inflater.inflate(R.layout.my_attendance_statistics, container, false);
		mRl_attendance_detail = (RelativeLayout) mView.findViewById(R.id.rl_attendance_detail);
		mRl_attendance_detail.setOnClickListener(this);

		mLate_sum = (TextView) mView.findViewById(R.id.late_sum);
		mLeaveearly_sum = (TextView) mView.findViewById(R.id.leaveearly_sum);
		mAbsent_sum = (TextView) mView.findViewById(R.id.absent_sum);

		webView = (WebView) mView.findViewById(R.id.webview);
		refresh();
		initWebView(webView, filingTotalExp);

		return mView;
	}

	/**
	 * 考勤统计-柱状图
	 * 
	 * @param webView
	 * @param sixMonthSalaryCharts
	 */
	private void initWebView(final WebView webView, FilingTotalExp filingTotalExp) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		webView.loadUrl("file:///android_asset/column_attendancestatistics.htm");

		final String data = JSON.toJSONString(filingTotalExp);
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

	/* 网络请求 */
	@SuppressWarnings("unchecked")
	@Override
	public void refresh() {
		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "my_kq_tj");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);// 查询季度
		 params.put("page_index", page_index+"");//当前页码
		 params.put("page_row", ActivityUtil.pageRow);//每页显示几条
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
				dialog.dismiss();
			}

			@SuppressWarnings("unused")
			@Override
			public void onSuccess(Object t) {
				if(getActivity()==null) //说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				System.out.println("AttendanceStatisticsFragment"+t.toString());
				dialog.dismiss();
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
						FilingTotalExp filingTotalExp = jsonInfo.getData_exp(FilingTotalExp.class);

						if (filingTotalExp != null) {
							filingTotalExp.setDate(month);// 把月份保存
							// 柱状图
							filingTotalExp.setChar_x(StringUtil.noNull(filingTotalExp.getChar_x()));
							filingTotalExp.setChar_y(StringUtil.noNull(filingTotalExp.getChar_y()));
							filingTotalExp.setDate(StringUtil.noNull(filingTotalExp.getDate()));// 月份
							
							mLate_sum.setText(StringUtil.noNull(filingTotalExp.getLate_num())+"次");
							mLeaveearly_sum.setText(StringUtil.noNull(filingTotalExp.getLevel_num())+"次");
							mAbsent_sum.setText(StringUtil.noNull(filingTotalExp.getAbsent_num())+"天");
//							mAbsent_sum.setText(filingTotalExp.getAbsent_num());
							
							initWebView(webView, filingTotalExp);
							if (StringUtil.isEmpty(filingTotalExp.char_x)) {
								Toast.makeText(context, "无当月数据", 1000).show();
							}
						}
						else {
							mLate_sum.setText("0");// 总得分
							mLeaveearly_sum.setText("0");// 综合考评
							mAbsent_sum.setText("0");// 绩效等级
							Toast.makeText(getActivity(), "当月无数据", 1000).show();// 显示登录失败提示
						}
					} else {// 显示登陆失败信息
						Toast.makeText(context, StringUtil.noNull(jsonInfov2.getResultDesc()), 1000).show();
					}
				}

			}
		});
	}

}
