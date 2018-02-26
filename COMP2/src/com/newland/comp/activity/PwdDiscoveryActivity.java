package com.newland.comp.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.process.PrYandFDetailActivity;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**密保问题找回
 * @author H81
 *
 */
public class PwdDiscoveryActivity extends BaseActivity{

    private TextView mQuestion_1;//问题一
    private EditText mQuestion_1_answer;
    private TextView mQuestion_2;//问题二
    private EditText mQuestion_2_answer;
    private TextView mQuestion_3;//问题三
    private EditText mQuestion_3_answer;
    private Button mQuestion_commit;//提交

    private void bindViews() {

        mQuestion_1 = (TextView) findViewById(R.id.question_1);
        mQuestion_1_answer = (EditText) findViewById(R.id.question_1_answer);
        mQuestion_2 = (TextView) findViewById(R.id.question_2);
        mQuestion_2_answer = (EditText) findViewById(R.id.question_2_answer);
        mQuestion_3 = (TextView) findViewById(R.id.question_3);
        mQuestion_3_answer = (EditText) findViewById(R.id.question_3_answer);
        mQuestion_commit = (Button) findViewById(R.id.question_commit);
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwddiscovery);
		setTitle();
		bindViews();
		
		
		
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
			center_tv.setText("找回密码");
		if (left_btn != null){//返回
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PwdDiscoveryActivity.this.finish();
				}
			});
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {//日期
			right_tv.setVisibility(View.GONE);
			
		}
	}
	
	/**
	 * 网络请求数据-提交密保
	 */
	private void getMyPerformance() {
		AjaxParams params = new AjaxParams();
		params.put("userid", SharedPreferencesUtils.getConfigStr(getApplicationContext(), CASH_NAME, "userid"));
//		params.put("signid", MD5Utils.getInstance().getUserIdSignid(SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userpwd")));
		params.put("pwd_pro_answer_1", mQuestion_1_answer.getText().toString());
		params.put("pwd_pro_answer_2", mQuestion_2_answer.getText().toString());
		params.put("pwd_pro_answer_3", mQuestion_3_answer.getText().toString());
		params.put("method", "send_pwd");
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				if (PwdDiscoveryActivity.this==null) {
					return;
				}
			}

			@Override
			public void onLoading(long count, long current) {
				// TODO Auto-generated method stub
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if (PwdDiscoveryActivity.this==null) {
					return;
				}
				JsonInfo jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfo.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						Toast.makeText(getApplicationContext(), "提交成功", 1000).show();
					} else {
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	
}}
