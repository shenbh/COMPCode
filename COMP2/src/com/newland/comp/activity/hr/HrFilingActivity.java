package com.newland.comp.activity.hr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.hr.CheckOn;
import com.newland.comp.bean.hr.LeaveData;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 人资-考勤报备
 * 
 * @author H81
 *
 *         2015年4月27日 上午11:53:28
 * @version 1.0.0
 */
public class HrFilingActivity extends BaseActivity {
	private Spinner mSpinner_type;// 申请类型
	private TextView mTv_starttime;// 开始时间
	private TextView mTv_endtime;// 结束时间
	private EditText mEt_otherreason;// 申请原因
	private Button mCommit;// 提交报备
	private String[] type_id;
	private String[] type_val;
	
	private String next_userid=""; //下一节点人
	private int inputNum=500;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hr_filing);
		setTitle();
		bindViews();
		reflush();
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
			center_tv.setText("考勤报备");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
		}
	}

	private void bindViews() {

		mSpinner_type = (Spinner) findViewById(R.id.spinner_type);
		mTv_starttime = (TextView) findViewById(R.id.tv_starttime);
		mTv_endtime = (TextView) findViewById(R.id.tv_endtime);
		mEt_otherreason = (EditText) findViewById(R.id.et_otherreason);
		mCommit = (Button) findViewById(R.id.commit);
		mTv_starttime.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		mTv_starttime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new DatePickDialog(HrFilingActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(mTv_starttime);
			}
		});
		mTv_endtime.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		mTv_endtime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new DatePickDialog(HrFilingActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(mTv_endtime);
			}
		});
//		限制输入的个数	
		mEt_otherreason.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
				System.out.println("s=" + s);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				int number = inputNum - s.length();
				selectionStart = mEt_otherreason.getSelectionStart();
				selectionEnd = mEt_otherreason.getSelectionEnd();
				if (temp.length() > inputNum) {
					Toast.makeText(HrFilingActivity.this, "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mEt_otherreason.setText(s);
					mEt_otherreason.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
	}

	/**
	 * 请求参数
	 */
	@Override
	public void reflush() {
		initDialog(this);
		
		System.out.println("reflush dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);// 工号
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "check_on");
		System.out.println("filing---" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (HrFilingActivity.this==null) {
					return;
				}
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				if (HrFilingActivity.this==null) {
					return;
				}
				dialog.dismiss();
				super.onSuccess(t);
				System.out.println(t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(HrFilingActivity.this, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						CheckOn checkOn = jsonInfo.getResultDataToClass(CheckOn.class);
						if (checkOn != null) {
							type_id = StringUtil.noNull(checkOn.type_id).split(",");
							type_val = StringUtil.noNull(checkOn.type_val).split(",");
							ActivityUtil.showDropDown(HrFilingActivity.this, mSpinner_type, type_val, R.layout.simple_spinner_item);

						} else {
						}

					} else {
						Toast.makeText(HrFilingActivity.this, "下拉表返回无数据"+jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	/*
	 * 网络请求提交数据
	 */
	private void postData() throws UnsupportedEncodingException {
		initDialog(this);
		
		System.out.println("postdata dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		String str_starttime = URLEncoder.encode(mTv_starttime.getText().toString(), "UTF-8");
		String str_endtime = URLEncoder.encode(mTv_endtime.getText().toString(), "UTF-8");

		AjaxParams params = new AjaxParams();
		params.put("userid", userid);// 工号
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "check_on_submit");
		params.put("type_id", type_id[mSpinner_type.getSelectedItemPosition()]);// 是否代办选项框id
		params.put("start_time", str_starttime);// 开始时间
		params.put("end_time", str_endtime);// 结束时间
		params.put("reason", URLEncoder.encode(mEt_otherreason.getText().toString(), "UTF-8"));// 其他原因
		System.out.println(params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (HrFilingActivity.this==null) {
					return;
				}
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (HrFilingActivity.this==null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(HrFilingActivity.this, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						
						
							Toast.makeText(HrFilingActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
							finish();
						
						
					} else {
						Toast.makeText(HrFilingActivity.this, jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	public void onClick(View view) throws UnsupportedEncodingException {
		int id = view.getId();
		if (id == R.id.head_left_btn) {// 返回
			finish();
		}
		if (id == R.id.commit) {// 提交申请
			commit();
		}
	}

	/**
	 * 提交
	 * @throws UnsupportedEncodingException 
	 */
	private void commit() throws UnsupportedEncodingException {
		String str_starttime = mTv_starttime.getText().toString();
		String str_endtime = mTv_endtime.getText().toString();
		String str_reason = mEt_otherreason.getText().toString();
		if (StringUtil.isEmpty(str_starttime)) {// 提交前验证
			Toast.makeText(getApplicationContext(), "开始时间不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(str_endtime)) {
			Toast.makeText(getApplicationContext(), "结束时间不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(str_reason)) {
			Toast.makeText(getApplicationContext(), "申请理由不能为空", Toast.LENGTH_SHORT).show();
		} else {
			postData();
		}
	}
	
	
	public void clickSpinner_type(View v){
		mSpinner_type.performClick();
	}
}
