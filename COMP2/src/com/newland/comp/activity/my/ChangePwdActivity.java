package com.newland.comp.activity.my;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.more.MoreStaffAspDetailActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 更改密码
 * 
 * @author H81
 *
 */
public class ChangePwdActivity extends BaseActivity {

	EditText original_pwd_edit, new_pwd_edit, sure_pwd_edit;
	Button saveBtn;
	LoadingDialog dialog;
	private TextView center_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepwd);
		initView();
	}

	public void initView() {
		dialog = new LoadingDialog(this);
		original_pwd_edit = (EditText) findViewById(R.id.changepwd_originalpwd);
		new_pwd_edit = (EditText) findViewById(R.id.changepwd_newpwd);
		sure_pwd_edit = (EditText) findViewById(R.id.changepwd_surepwd);
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		left_btn.setVisibility(View.VISIBLE);
		right_btn.setVisibility(View.GONE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				ChangePwdActivity.this.finish();

			}
		});
		saveBtn = (Button) findViewById(R.id.changepwd_save);
		center_title = (TextView) findViewById(R.id.head_center_title);
		center_title.setText("重置密码");
		saveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String original_pwd = original_pwd_edit.getText().toString();
				String new_pwd = new_pwd_edit.getText().toString();
				String sure_pwd = sure_pwd_edit.getText().toString();
				if (StringUtil.isEmpty(original_pwd)) {
					Toast.makeText(ChangePwdActivity.this, "原密码不能为空", 1000).show();
				}else if (StringUtil.isEmpty(new_pwd)) {
					Toast.makeText(ChangePwdActivity.this, "新密码不能为空", 1000).show();
				}else if (StringUtil.isEmpty(sure_pwd)) {
					Toast.makeText(ChangePwdActivity.this, "确认新密码不能为空", 1000).show();
				}else {
					if (new_pwd.equals(sure_pwd)) {
						getChangePwdService();
					} else {
						Toast.makeText(ChangePwdActivity.this, "新密码不一致，请再次确认", 1000).show();
					}
				}
			}
		});
	}

	/**
	 * 请求网络修改密码
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void getChangePwdService() {
		String userid = SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid");
		String userpwd = original_pwd_edit.getText().toString();
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("pwd", userpwd);
		params.put("pwd_new", new_pwd_edit.getText().toString());
		params.put("method", "edit_pwd");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println("params.toString()  =========   " + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				if (ChangePwdActivity.this==null) {
					return;
				}
				dialog.dismiss();
			}

			@Override
			public void onSuccess(Object t) {
				if (ChangePwdActivity.this==null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					
					//兼容新接口
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						Toast.makeText(getApplicationContext(), "密码修改成功", 1000).show();
						System.out.println("修改成功================" + jsonInfov2.getResultDesc());
						finish();
					}
					 else {// 显示失败信息
							Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();
							System.out.println("修改失败================" + jsonInfov2.getResultDesc());
						}

				}
			}

		});
	}

}
