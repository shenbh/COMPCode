package com.newland.comp.activity;

import static com.newland.comp.activity.BaseActivity.SCREEN_HIGHT;
import static com.newland.comp.activity.BaseActivity.SCREEN_WIDTH;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 基类
 * 
 * @author H81
 * 
 */
public class BaseActivity extends Activity {

	public static final String CASH_NAME = "COMP";
	public static String mStrImei = null;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HIGHT = 0;
	public static final String PROCESS_TYPE_VACATE = "vacate";
	public static final String PROCESS_TYPE_LEAVE = "leave";

	public static String CURRENTACTIVITY = "";
	public boolean isClickable = false;
	public int dissentColor;// 我有异议字体颜色

	public LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDisplay();
		AppContext.getInstance().addActivity(this);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		dissentColor = getResources().getColor(R.color.app_text_gray2);
	}

	/**
	 * 设置标题栏 title_head
	 */
	public void setTitle(String centerStr,String rightStr) {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null )
			center_tv.setText(centerStr);
		if (left_btn != null ) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null && StringUtil.isNotEmpty(rightStr) ) {
			right_tv.setVisibility(View.VISIBLE);
			right_tv.setText(rightStr);
		}else {
			right_tv.setVisibility(View.GONE);
		}
	}
	/**
	 * 获取屏幕宽高
	 */
	private void getDisplay() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_HIGHT = displayMetrics.heightPixels;
	}

	/**
	 * 获取系统的IMEI
	 */
	public static String getSystemImei(Context ctx) {
		if (mStrImei == null) {
			TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null) {
				mStrImei = telephonyManager.getDeviceId();
				telephonyManager.getDeviceSoftwareVersion();
			}
		}
		return mStrImei;
	}

	/**
	 * 获取系统SDK版本
	 * 
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	InputMethodManager manager;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {

				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),

				InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}

		return super.onTouchEvent(event);

	}

	/**
	 * 刷新网络
	 */
	public void reflush() {

	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 是否在异议期内
	 */
	public void isClickAble(final Context context, String quarteType, String time, String type) {
		System.out.println("context===============================" + context);

		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", quarteType);
		params.put("time", time);
		params.put("method", "dissent_auth");
		params.put("type", type);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (context == null) {
					return;
				}
				dialog.dismiss();
				JsonInfoV2 jsonInfov2 = null;
				System.out.println("dissent_auth--" + t.toString());
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						isClickable = true;
						dissentColor = getResources().getColor(R.color.app_black);
						setDissentBtnColor();
					} else {
						isClickable = false;
						dissentColor = getResources().getColor(R.color.app_text_gray2);
						setDissentBtnColor();
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					super.onFailure(t, errorNo, strMsg);
				if (context == null) {
					return;
				}
				dialog.dismiss();
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();// 显示登录失败提示
			}
		});
	}

	public void initDialog(Context context) {
		dialog = new LoadingDialog(context);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {
			dialog.show(true);
		}else {
			dialog.show(false);
		}
	}

	public void setDissentBtnColor() {

	}

	public void makeToast(Context context, String strMsg) {
		Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 替换连接失败信息
	 */
	public String replaceErroStr(String str) {
		int index = str.indexOf("Connection to", 0);
		if (index != -1) {
			str = str.replaceAll(str, "连接失败");
		}
		return str;
	}

}
