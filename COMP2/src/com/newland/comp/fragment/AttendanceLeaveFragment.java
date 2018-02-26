package com.newland.comp.fragment;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyAttendanceLeaveActivity;
import com.newland.comp.activity.my.MyAttendanceOverTimeActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.LeaveCountData;
import com.newland.comp.bean.my.LeaveCountExp;
import com.newland.comp.bean.my.OverTimeCountData;
import com.newland.comp.bean.my.OverTimeCountExp;
import com.newland.comp.fragment.AttendanceOverTimeFragment.DemoJavaScriptInterface;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * 请假统计
 * 
 * @author H81
 *
 */
public class AttendanceLeaveFragment extends BaseFragment{
	Context context;
	
	private RelativeLayout mRl_attendance_detail;// 请假明细
	private WebView webView;
	private TextView overtime_sum;//请假总计
	private LeaveCountExp leaveCountExp;// 柱状图
	
	private View mView;

	private TextView timeView;//日期
	private String month;
	
	private int page_index=1;//当前页码
	LoadingDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("MyPayStubFragment第一次启动");
	
	}
	public AttendanceLeaveFragment(Context context) {
		this.context=context;
	}

	public static AttendanceLeaveFragment newInstance(Context context) {
		return new AttendanceLeaveFragment(context);
	}
	
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.rl_attendance_detail:
			Intent intent=new Intent(getActivity(), MyAttendanceLeaveActivity.class);
			intent.putExtra("month", month);//月份
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		page_index=1;
		dialog = new LoadingDialog(getActivity());
		
		mView = inflater.inflate(R.layout.my_attendance_leave, container, false);
		mRl_attendance_detail = (RelativeLayout) mView.findViewById(R.id.rl_attendance_detail);
		mRl_attendance_detail.setOnClickListener(this);
		webView = (WebView) mView.findViewById(R.id.webview);
		overtime_sum=(TextView) mView.findViewById(R.id.overtime_sum);
	
		
		initWebView(webView, leaveCountExp);
		refresh();
		return mView;
	}
	
	/**请假统计-柱状图
	 * @param webView
	 * @param sixMonthSalaryCharts
	 */
	private void initWebView(final WebView webView, LeaveCountExp leaveCountExp) {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportMultipleWindows(true);
		webView.setWebChromeClient(new WebChromeClient());
//		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "Android");
		webView.loadUrl("file:///android_asset/column_leave.htm");

		final String data = JSON.toJSONString(leaveCountExp);
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
			dialog.setTvMessage("正在加载...");
			dialog.show(true);
			
			
			String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
			AjaxParams params = new AjaxParams();
			params.put("userid", userid);
			params.put("method", "my_kq_qj");
			params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
			params.put("month", month+"");
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
					Toast.makeText(context, strMsg, 1000).show();
					if (getActivity() == null) // 说明上层activity 已经被销毁了
					{
						System.out.println("getActivity() 拦截");
						return;
					}
					dialog.dismiss();
				}

				@Override
				public void onSuccess(Object t) {
					System.out.println(t.toString());
					if(getActivity()==null) //说明上层activity 已经被销毁了
					{
						System.out.println("getActivity() 拦截");
						return;
					}
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
							// 服务器传回来的用户信息
							JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
							LeaveCountExp leaveCountExp = jsonInfo.getData_exp(LeaveCountExp.class);
							
							if (leaveCountExp != null) {
								String smonth=month.replace("-", "年");
								leaveCountExp.setDate(smonth);//把月份保存
								// 柱状图
								leaveCountExp.setChar_x(StringUtil.noNull(leaveCountExp.getChar_x()));
								leaveCountExp.setChar_y(StringUtil.noNull(leaveCountExp.getChar_y()));
								leaveCountExp.setDate(StringUtil.noNull(leaveCountExp.getDate()));//月份
								overtime_sum.setText(leaveCountExp.getQj_sum()+"天");
								initWebView(webView, leaveCountExp);
								if (StringUtil.isEmpty(leaveCountExp.char_x)) {
									Toast.makeText(context, "无当月数据", 1000).show();
								}
							}else
							{
								overtime_sum.setText("0");
							}
						} else {// 显示登陆失败信息
							Toast.makeText(context, StringUtil.noNull(jsonInfov2.getResultDesc()), 1000).show();
						}
					}

				}
			});
		}
}
