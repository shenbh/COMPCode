package com.newland.comp.activity.my;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;


/**我的消息详情
 * @author H81
 *
 * 2015年4月22日 下午4:44:26
 * @version 1.0.0
 */
public class MyMessageDetailActivity extends Activity {
	private TextView mSender;// 发送人
	private TextView mContent;// 内容
	private TextView mTime;// 时间

	private String strSender;//发送人
	private String strTime;//时间
	private String strContent;//内容
	private String strState;//未读、已读状态
	private String strMsg_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.my_messagedetail);
		getIntentData();
		setTitle();
		bindViews();
		if (strState.equals("0")) {//0未读， 1已读
			flush(strMsg_id);
		}
	}

	/**
	 * 获取Intent过来的数据
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		strMsg_id=intent.getStringExtra("msgid");
		System.out.println(strMsg_id);
		strSender = intent.getStringExtra("sender");
		strTime = intent.getStringExtra("time");
		strContent = intent.getStringExtra("content");
		strState=intent.getStringExtra("state");
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView timeView = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("详情");
		if (left_btn != null)
			left_btn.setVisibility(View.VISIBLE);
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (timeView != null) {
			timeView.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化控件
	 */
	private void bindViews() {
		mSender = (TextView) findViewById(R.id.sender);
		mContent = (TextView) findViewById(R.id.content);
		mTime = (TextView) findViewById(R.id.time);
		if (mSender != null) {
			mSender.setText(strSender);
		}
		if (mTime != null) {
			mTime.setText(strTime);
		}
		if (mContent != null) {
			mContent.setText(strContent);
		}
	}
	
	public void onClick(View view){
		if (view.getId()==R.id.head_left_btn) {
			finish();
		}
	}

	/**网络请求--把（服务器）消息设置成已读状态

	 */
	private void flush(String msg_id){
		String userid = SharedPreferencesUtils.getConfigStr(MyMessageDetailActivity.this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "sys_msg_red");
		params.put("msg_id", msg_id);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=new BaseActivity().replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				// load_msg.setVisibility(View.GONE);
				if (MyMessageDetailActivity.this==null) {
					return;
				}
				
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (MyMessageDetailActivity.this==null) {
					return;
				}
				Log.v("newland", t.toString());
				// load_msg.setVisibility(View.GONE);
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
			
				if (jsonInfov2 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
//						Toast.makeText(MyMessageDetailActivity.this, jsonInfo.getResultCode(), 1000).show();// 显示登录成功提示
					} else {
						Toast.makeText(MyMessageDetailActivity.this, jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}
}
