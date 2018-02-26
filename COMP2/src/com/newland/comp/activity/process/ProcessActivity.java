package com.newland.comp.activity.process;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.TabBaseActivity;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 流程界面
 * 
 * @author H81
 * 
 */
public class ProcessActivity extends TabBaseActivity {
	private TextView unred_num;// 待办流程未读消息数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.process);
		initView();
		setTitle();
		checkPop();
	}

	private void initView() {
		unred_num = (TextView) findViewById(R.id.unred_num);
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("流程");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.GONE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 待办流程
	 * 
	 * @param view
	 */
	public void dbprocess_click(View view) {
		// 在缓存中保存未读消息数
		if (SharedPreferencesUtils.getConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num").equals("0")) {
			unred_num.setVisibility(View.GONE);
		} else {
			unred_num.setVisibility(View.VISIBLE);
			unred_num.setText(SharedPreferencesUtils.getConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num"));
		}
		Intent intent = new Intent(getApplicationContext(), PrToDoActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 已办流程
	 * 
	 * @param view
	 */
	public void ybprocess_click(View view) {
		Intent intent = new Intent(getApplicationContext(), PrYandFActivity.class);
		intent.putExtra("type", "finished");
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 发起的流程
	 * 
	 * @param view
	 */
	public void fqprocess_click(View view) {
		Intent intent = new Intent(getApplicationContext(), PrYandFActivity.class);
		intent.putExtra("type", "launch");
		startActivity(intent);
		overridePendingTransition(R.anim.scale_rotate, R.anim.my_alpha_action);
	}

	/**
	 * 检查服务是否运行
	 */
	private void checkPop() {
		// 检查有没冒泡提示
		String numstr = SharedPreferencesUtils.getConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num");
		int num = Integer.parseInt(StringUtil.noNull(numstr, "0"));
		if (num > 0) {
			unred_num.setVisibility(View.VISIBLE);
			unred_num.setText(numstr);
		} else if (num == 0) {
			unred_num.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
//		reflushProNum();
		checkPop();
	}

	/**
	 * 刷新待办流程未处理消息条数
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void reflushProNum() {
		initDialog(this);
		
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "getOperatorTasksCount");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				System.out.println("getOperatorTasksCount:" + t.toString());
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov3 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) // 正常返回数据
					{
						JSONObject json3 = jsonInfov3.getResultDataToJsonObject();
						// 在缓存中保存未读消息数
						SharedPreferencesUtils.setConfigStr(getApplicationContext(), SharedPreferencesUtils.Name, "pro_unred_num", StringUtil.noNull(json3.getString("count"), "0"));
				
						checkPop();
					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfov3.getResultDesc(), 1500).show();
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (dialog!=null) {
					dialog.dismiss();
				}
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				System.out.println("服务请求网络失败" + strMsg);
			}

		});
	}
}
