package com.newland.comp.activity.hr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyPersonListNoSearchActivity;
import com.newland.comp.bean.hr.LeaveOffice;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.bean.process.SubmitLeaveBean;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LeaveOfficePopupWindow;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 人资-离职申请
 * 
 * @author H81
 *
 */
public class HrLeaveOfficeActivity extends BaseActivity {

	private EditText mUsername;
	private EditText mUserno;
	private EditText mDentify;
	private EditText mMale;
	private EditText mKnowledge;
	private EditText mDep;
	private EditText mType;
	private EditText mOffice;// 职位
	private EditText mIn_comp;// 入司时间
	private EditText mOut_comp;
	private Spinner mLeave_type;
	private Spinner mLeave_main_reason;
	private Spinner mLeave_sce_reason;
	private EditText mOthers;// 其他原因
	private EditText mSuggest;// 意见建议
	
	private LeaveOffice leaveOffice;
	private View layout;
	// 下拉框数据
	private String[] Leave_type_id;
	private String[] Leave_type_val;
	private String[] reason_first_id;
	private String[] reason_first_val;
	private String[] reason_sce_id;
	private String[] reason_sce_val;

	private LeaveOfficePopupWindow popWin; // 离职指南泡泡弹窗
	private String next_userid = ""; // 下一节点人
	private String flow_code = "";// 流程模板

	private int inputNum = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leave_office);
		bindViews();
		getSpannerData();
		addLinster();
		CURRENTACTIVITY = "HrLeaveOfficeActivity";
	}

	private void bindViews() {

		mUsername = (EditText) findViewById(R.id.username);
		mUserno = (EditText) findViewById(R.id.userno);
		mDentify = (EditText) findViewById(R.id.dentify);
		mMale = (EditText) findViewById(R.id.male);
		mKnowledge = (EditText) findViewById(R.id.knowledge);
		mDep = (EditText) findViewById(R.id.dep);
		mType = (EditText) findViewById(R.id.type);
		mOffice = (EditText) findViewById(R.id.office);
		mIn_comp = (EditText) findViewById(R.id.in_comp);
		mOut_comp = (EditText) findViewById(R.id.out_comp);
		mOthers = (EditText) findViewById(R.id.others);
		mSuggest = (EditText) findViewById(R.id.suggest);
		mLeave_type = (Spinner) findViewById(R.id.leave_type);
		mLeave_main_reason = (Spinner) findViewById(R.id.leave_main_reason);
		mLeave_sce_reason = (Spinner) findViewById(R.id.leave_sce_reason);
		layout = findViewById(R.id.layout);
		TextView center_title = (TextView) findViewById(R.id.head_center_title);
		center_title.setText("离职申请");
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				HrLeaveOfficeActivity.this.finish();
			}
		});
		dialog = new LoadingDialog(this);
		mOthers.addTextChangedListener(new TextWatcher() {
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
				selectionStart = mOthers.getSelectionStart();
				selectionEnd = mOthers.getSelectionEnd();
				if (temp.length() > inputNum) {
					Toast.makeText(getApplicationContext(), "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mOthers.setText(s);
					mOthers.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
		mSuggest.addTextChangedListener(new TextWatcher() {
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
				selectionStart = mSuggest.getSelectionStart();
				selectionEnd = mSuggest.getSelectionEnd();
				if (temp.length() > inputNum) {
					Toast.makeText(getApplicationContext(), "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mSuggest.setText(s);
					mSuggest.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
	}

	private void addLinster() {
		mOut_comp.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				new DatePickDialog(HrLeaveOfficeActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(HrLeaveOfficeActivity.this, mOut_comp);
			}
		});
		// 离职主要原因 选择其他后，其他原因说明可填写
		mLeave_main_reason.setOnItemSelectedListener(new OnItemSelectedListener() {

		
			public void onItemSelected(AdapterView<?> arg0, View arg1, int poistion, long arg3) {
				if (poistion == mLeave_main_reason.getCount() - 1) // 说明选择了其他原因
				{
					mOthers.setEnabled(true);

				} else {
					mOthers.setEnabled(false);
					mOthers.setText("");
				}
			}

		
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// 离职次要原因 选择其他后，其他原因说明可填写
		mLeave_sce_reason.setOnItemSelectedListener(new OnItemSelectedListener() {

			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int poistion, long arg3) {
				if (poistion == mLeave_main_reason.getCount() - 1) // 说明选择了其他原因
				{
					mOthers.setEnabled(true);

				} else {
					mOthers.setEnabled(false);
					mOthers.setText("");
				}
			}

		
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * 请求网络获取数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void getSpannerData() {
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "leave_office");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				if (HrLeaveOfficeActivity.this==null) {
					return;
				}
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				dialog.dismiss();
			}

			@Override
			public void onSuccess(Object t) {
				if (HrLeaveOfficeActivity.this==null) {
					return;
				}
				System.out.println(t.toString());
				dialog.dismiss();
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfo != null) {

					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) // 正常返回数据
					{
						leaveOffice = jsonInfo.getResultDataToClass(LeaveOffice.class);
						if (leaveOffice != null) {
							if (HrLeaveOfficeActivity.this.isFinishing()) {
								return;
							}
							mUsername.setText(StringUtil.noNull(leaveOffice.username));
							mUserno.setText(StringUtil.noNull(leaveOffice.userno));

							mMale.setText(StringUtil.noNull(leaveOffice.male));
							mKnowledge.setText(StringUtil.noNull(leaveOffice.edu));
							mDentify.setText(StringUtil.noNull(leaveOffice.Identity_card));
							mType.setText(StringUtil.noNull(leaveOffice.userType));
							mDep.setText(StringUtil.noNull(leaveOffice.dep));
							mOffice.setText(StringUtil.noNull(leaveOffice.jobName));
							mIn_comp.setText(StringUtil.noNull(leaveOffice.joinUnitTime));

							Leave_type_val = StringUtil.noNull(leaveOffice.Leave_type_val).split(",");
							reason_first_val = StringUtil.noNull(leaveOffice.reason_first_val).split(",");
							reason_sce_val = StringUtil.noNull(leaveOffice.reason_sce_val).split(",");

							Leave_type_id = StringUtil.noNull(leaveOffice.Leave_type_id).split(",");
							reason_first_id = StringUtil.noNull(leaveOffice.reason_first_id).split(",");
							reason_sce_id = StringUtil.noNull(leaveOffice.reason_sce_id).split(",");

							ActivityUtil.showDropDown(HrLeaveOfficeActivity.this, mLeave_type, Leave_type_val, R.layout.simple_spinner_item);
							ActivityUtil.showDropDown(HrLeaveOfficeActivity.this, mLeave_main_reason, reason_first_val, R.layout.simple_spinner_item);
							ActivityUtil.showDropDown(HrLeaveOfficeActivity.this, mLeave_sce_reason, reason_sce_val, R.layout.simple_spinner_item);

							popWindow();
						} else {
							Toast.makeText(getApplicationContext(), "无法获取下拉框数据", 1000).show();
						}

					} else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), "下拉表返回无数据" + jsonInfo.getResultDesc(), 1000).show();
					}
				}

			}
		});
	}

	/**
	 * 提交表单
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void submit_from(View view) throws UnsupportedEncodingException {

		// 校验必填项
		if (StringUtil.isEmpty(mUsername.getText().toString())) {
			Toast.makeText(getApplicationContext(), "员工姓名必填！", 1000).show();
			return;
		}
		if (StringUtil.isEmpty(mUserno.getText().toString())) {
			Toast.makeText(getApplicationContext(), "员工编号必填！", 1000).show();
			return;
		}
		if (StringUtil.isEmpty(mOut_comp.getText().toString())) {
			Toast.makeText(getApplicationContext(), "拟离职日期必填！", 1000).show();
			return;
		}

		postFrom();
	}

	/**
	 * 提交表单
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void postFrom() throws UnsupportedEncodingException {
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "leave_office_submit");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));

		params.put("username", URLEncoder.encode(mUsername.getText().toString(), "UTF-8"));
		params.put("userno", URLEncoder.encode(mUserno.getText().toString(), "UTF-8"));
		params.put("leave_time", URLEncoder.encode(mOut_comp.getText().toString(), "UTF-8"));
		params.put("leave_type", Leave_type_id[mLeave_type.getSelectedItemPosition()]);
		params.put("reason_first_id", reason_first_id[mLeave_main_reason.getSelectedItemPosition()]);
		params.put("reason_sce_id", reason_sce_id[mLeave_sce_reason.getSelectedItemPosition()]);
		params.put("reason_other", URLEncoder.encode(mOthers.getText().toString(), "UTF-8"));
		params.put("suggest", URLEncoder.encode(mSuggest.getText().toString(), "UTF-8"));
		params.put("next_userid", next_userid);// 下一节点人
		params.put("flow_code", StringUtil.noNull(flow_code));// 流程模板
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				if (HrLeaveOfficeActivity.this==null) {
					return;
				}
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				dialog.dismiss();
			}

			@Override
			public void onSuccess(Object t) {
				if (HrLeaveOfficeActivity.this==null) {
					return;
				}
				System.out.println(t.toString());
				dialog.dismiss();
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) // 正常返回数据
					{
						if (StringUtil.isEmpty(next_userid))// 第一次提交为空，获取下一节点人列表
						{
							SubmitLeaveBean submitLeaveBean = jsonInfo.getResultDataToClass(SubmitLeaveBean.class);
							flow_code = submitLeaveBean.flow_code;
							ArrayList<PersonList> listPerson = (ArrayList<PersonList>) submitLeaveBean.userList;
							if (listPerson.size() > 0) {
								Intent intent = new Intent(getApplicationContext(), MyPersonListNoSearchActivity.class);
								intent.putExtra("list", listPerson);
								startActivityForResult(intent, 1);
							}
						} else {
							Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
							finish();
							CURRENTACTIVITY = "";
						}

					}

					else {// 显示登陆失败信息
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();
					}
				}
			}
		});
	}

	private void popWindow() {
		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popWin = new LeaveOfficePopupWindow(HrLeaveOfficeActivity.this, null, leaveOffice);
		popWin.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});

		// 显示窗口
		popWin.showAtLocation(HrLeaveOfficeActivity.this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {

			if (requestCode == 1) { // 选择人
				System.out.println("data:" + data.getStringExtra("userid"));
				next_userid = data.getStringExtra("userid");
				try {
					postFrom();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void clickLeave_type(View v){
		mLeave_type.performClick();
	}
	public void clickMain_reason(View v){
		mLeave_main_reason.performClick();
	}
	public void clickSce_reason(View v){
		mLeave_sce_reason.performClick();
	}
}
