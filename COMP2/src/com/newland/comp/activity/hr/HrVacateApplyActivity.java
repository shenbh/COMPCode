package com.newland.comp.activity.hr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyPersonListNoSearchActivity;
import com.newland.comp.bean.hr.LeaveData;
import com.newland.comp.bean.my.EffictData;
import com.newland.comp.bean.my.EffictData2;
import com.newland.comp.bean.my.EffictDataExp;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.bean.process.SubmitLeaveBean;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.DateTimePickDialogUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 人资-请假申请界面
 * 
 * @author H81
 *
 *         2015年4月24日 上午10:09:54
 * @version 1.0.0
 */
public class HrVacateApplyActivity extends BaseActivity {
	// 注
	private TextView mTv_total;// 年假总额
	private TextView mTv_apply;// 已申请天数
	private TextView mTv_usable;// 可用天数

	private Spinner mSpinner_daiban;// 是否代办------------无*
	private Spinner mSpinner_type;// 请假类型
	private Spinner mSpinner_leave;// 是否离开本省市
	private EditText mTv_userno;// 员工编号
	private EditText mTv_username;// 员工姓名
	private EditText mEt_usersection;// 员工部门------------无*
	private EditText mDurTime;// 请假时长
	private RadioGroup mRadioGroup;// 请假时长
	private RadioButton mRadiobtn_day;// 请假时长--天
	private RadioButton mRadiobtn_hours;// 请假时长--小时
	private TextView mSpinner_starttime;// 开始时间
	private TextView mSpinner_endtime;// 结束时间
	private Spinner mSpinner_leavereason;// 请假事由
	private EditText mEt_otherreason;// 其他原因-------------无*

	private String radiounit = "day";// 选择天还是小时
	private String str_durTime;// 请假时长

	List<LeaveData> list;// LeaveData
	// 下拉框数据
	private String[] commission_id;
	private String[] commission_val;
	private String[] leave_type_id;
	private String[] leave_type_val;
	private String[] outside_id;
	private String[] outside_val;

	private List<String> reason_id_list;
	private List<String> reason_val_list;
	LeaveData dataExp;

	LoadingDialog dialog;
	private String next_userid = ""; // 下一节点人
	private String flow_code = ""; // 流程模板
	private String mis = "";// 请假代办人id，如果是本人就是本人的id

	private int inputNum = 500;
	boolean isDurTimeRight=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vacate);
		setTitle();
		bindViews();
		reflush();
		CURRENTACTIVITY="HrVacateApplyActivity";
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
			center_tv.setText("请假申请");
		if (left_btn != null) // 返回
		{
			left_btn.setVisibility(View.VISIBLE);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null)// 日期
		{
			right_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 请求参数 获取初始化数值
	 */
	@Override
	public void reflush() {
		// updateDurTime();

		dialog = new LoadingDialog(HrVacateApplyActivity.this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		System.out.println("reflush dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);// 工号
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "getleaveData");
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (HrVacateApplyActivity.this==null) {
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
				if (HrVacateApplyActivity.this==null) {
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
						dataExp = jsonInfo.getResultDataToClass(LeaveData.class);
						if (dataExp != null) {
							commission_id = StringUtil.noNull(dataExp.commission_id).split(",");
							commission_val = StringUtil.noNull(dataExp.commission_val).split(",");
							leave_type_id = StringUtil.noNull(dataExp.leave_type_id).split(",");
							leave_type_val = StringUtil.noNull(dataExp.leave_type_val).split(",");
							outside_id = StringUtil.noNull(dataExp.outside_id).split(",");
							outside_val = StringUtil.noNull(dataExp.outside_val).split(",");
							String[] reason_id = StringUtil.noNull(dataExp.reason_id).split(",");
							String[] reason_val = StringUtil.noNull(dataExp.reason_val).split(",");

							reason_id_list = new ArrayList<String>();
							reason_val_list = new ArrayList<String>();
							Collections.addAll(reason_id_list, reason_id);
							Collections.addAll(reason_val_list, reason_val);
							reason_id_list.add("其他原因(手工输入)");
							reason_val_list.add("其他原因(手工输入)");

							ActivityUtil.showDropDown(HrVacateApplyActivity.this, mSpinner_daiban, commission_val, R.layout.simple_spinner_item);
							ActivityUtil.showDropDown(HrVacateApplyActivity.this, mSpinner_type, leave_type_val, R.layout.simple_spinner_item);
							ActivityUtil.showDropDown(HrVacateApplyActivity.this, mSpinner_leave, outside_val, R.layout.simple_spinner_item);
							ActivityUtil.showDropDown(HrVacateApplyActivity.this, mSpinner_leavereason, reason_val_list, R.layout.simple_spinner_item);

							mTv_total.setText(StringUtil.noNull(dataExp.year_hol));// 年假总额
							mTv_apply.setText(StringUtil.noNull(dataExp.used_hol));// 已申请天数
							mTv_usable.setText(StringUtil.noNull(dataExp.ava_hol));// 可用天数

							mTv_userno.setText(StringUtil.noNull(dataExp.userno));// mis
							mTv_username.setText(StringUtil.noNull(dataExp.username));// 用户名
							mEt_usersection.setText(StringUtil.noNull(dataExp.dep));// 所在部门

						} else {
						}

					} else {
						Toast.makeText(HrVacateApplyActivity.this, "下拉表返回无数据" + jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void bindViews() {
		mTv_total = (TextView) findViewById(R.id.tv_total);
		mTv_apply = (TextView) findViewById(R.id.tv_apply);
		mTv_usable = (TextView) findViewById(R.id.tv_usable);
		mDurTime = (EditText) findViewById(R.id.durtime);// 请假时长
		mSpinner_daiban = (Spinner) findViewById(R.id.spinner_daiban);
		mSpinner_type = (Spinner) findViewById(R.id.spinner_type);
		mSpinner_leave = (Spinner) findViewById(R.id.spinner_leave);
		mTv_userno = (EditText) findViewById(R.id.tv_userno);
		mTv_username = (EditText) findViewById(R.id.tv_username);
		mEt_usersection = (EditText) findViewById(R.id.et_usersection);
		mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		mRadiobtn_day = (RadioButton) findViewById(R.id.radiobtn_day);
		mRadiobtn_hours = (RadioButton) findViewById(R.id.radiobtn_hours);
		mSpinner_starttime = (TextView) findViewById(R.id.spinner_starttime);
		mSpinner_starttime.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		mSpinner_starttime.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				timeDialog(mSpinner_starttime);
			}
		});
		mSpinner_endtime = (TextView) findViewById(R.id.spinner_endtime);
		mSpinner_endtime.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
		mSpinner_endtime.setOnClickListener(new OnClickListener() {

		
			public void onClick(View v) {
				timeDialog(mSpinner_endtime);
			}
		});


		mSpinner_leavereason = (Spinner) findViewById(R.id.spinner_leavereason);
		mEt_otherreason = (EditText) findViewById(R.id.et_otherreason);
		addLinster();
	}

	/**
	 * 显示时间选择器
	 * 
	 * @param t
	 *            textView控件
	 */
	private void timeDialog(TextView t) {
		if (mRadioGroup.getCheckedRadioButtonId() == R.id.radiobtn_day)// 天
		{
			if ((new DatePickDialog(HrVacateApplyActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(t, HrVacateApplyActivity.this).toString()).equals(null))// 点击了取消
			{
				t.setText(StringUtil.getNowTime(StringUtil.DAY_TIME));
			}
		} else if ((new DateTimePickDialogUtil(HrVacateApplyActivity.this, null).dateTimePicKDialog(t, HrVacateApplyActivity.this).toString()).equals(null)) // 小时对话框且点击了取消
		{
			t.setText(StringUtil.getNowTime(StringUtil.MINUTE_TIME));

		}
	}

	private void addLinster() {
		// 设置请假时长单位
		if (mRadioGroup != null) {
			mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					if (checkedId == mRadiobtn_day.getId()) {
						radiounit = "day";
						mSpinner_starttime.setText("");
						mSpinner_endtime.setText("");
					}

					if (checkedId == mRadiobtn_hours.getId()) {
						radiounit = "hour";
						mSpinner_starttime.setText("");
						mSpinner_endtime.setText("");
					}
				}
			});
		}
		// 是否代办选择框
		mSpinner_daiban.setOnItemSelectedListener(new OnItemSelectedListener() {

		
			public void onItemSelected(AdapterView<?> arg0, View view, int poistion, long arg3) {

				if (commission_val[poistion].equals("是")) // 不是本人请假
				{
					mTv_userno.setEnabled(true);// mis
					mTv_username.setText("");// 用户名
					mEt_usersection.setText("");// 所在部门
				} else// 是本人请假
				{
					mTv_userno.setEnabled(false);// mis
					if (dataExp != null) {
						mTv_userno.setText(StringUtil.noNull(dataExp.userno)); // 员工编号
						mTv_username.setText(StringUtil.noNull(dataExp.username));// 用户名
						mEt_usersection.setText(StringUtil.noNull(dataExp.dep));// 所在部门
						String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
						mis = userid;
					}

				}
			}

		
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// 监听其他原因输入，当选择手工输入时，其他原因选项框可输入
		mSpinner_leavereason.setOnItemSelectedListener(new OnItemSelectedListener() {

			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int poistion, long arg3) {
				if (poistion == (reason_id_list.size() - 1)) {
					mEt_otherreason.setEnabled(true);
				} else {
					mEt_otherreason.setEnabled(false);
					mEt_otherreason.setText("");
				}
			}

		
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		// 员工编号输入后，获取关联的部门
		mTv_userno.setOnFocusChangeListener(new OnFocusChangeListener() {

		
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) // 失去焦点
				{
					String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
					AjaxParams params = new AjaxParams();
					params.put("userid", mTv_userno.getText().toString());// 工号
					params.put("signid", MD5Utils.getInstance().getUserIdSignid(mTv_userno.getText().toString()));
					params.put("method", "getdepName");
					System.out.println(HttpUtils.URL + "?" + params.toString());
					FinalHttp fh = new FinalHttp();
					fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
					fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
							super.onFailure(t, errorNo, strMsg);
							if (HrVacateApplyActivity.this==null) {
								return;
							}
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(Object t) {
							if (HrVacateApplyActivity.this==null) {
								return;
							}
							System.out.println(t.toString());
							super.onSuccess(t);

							JsonInfoV3 jsonInfo = null;
							try {
								jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
							}
							if (jsonInfo != null) {
								if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
									JSONObject object = jsonInfo.getResultDataToJsonObject();
									if (object != null) {
										mTv_username.setText(StringUtil.noNull(object.getString("username")));// 用户名
										mEt_usersection.setText(StringUtil.noNull(object.getString("user_dep")));// 所在部门
										mis = mTv_userno.getText().toString(); // 代办人的id
									}
								} else {
									Toast.makeText(getApplicationContext(), "关联名字和部门失败:" + jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
								}
							}
						}
					});
				}
			}
		});

		// 限制输入的个数
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
					Toast.makeText(getApplicationContext(), "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mEt_otherreason.setText(s);
					mEt_otherreason.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});

	}


	/**
	 * 网络请求提交数据
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void postData() throws UnsupportedEncodingException {
		dialog = new LoadingDialog(HrVacateApplyActivity.this);
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {dialog.show(true);}
		System.out.println("postdata dialog exist");
		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		String s = mSpinner_starttime.getText().toString();

		String str_starttime = mSpinner_starttime.getText().toString();
		String str_endtime = mSpinner_endtime.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);// 工号
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "submit_leave");
		params.put("commission_id", commission_id[mSpinner_daiban.getSelectedItemPosition()]);// 是否代办选项框id
		params.put("leave_type_id", leave_type_id[mSpinner_type.getSelectedItemPosition()]);// 请假类型
		params.put("outside_id", outside_id[mSpinner_leave.getSelectedItemPosition()]);// 是否离开本省市选项id
		params.put("leave_time", mDurTime.getText().toString() + "," + radiounit);// 请假时间
																					// 时间与单位逗号分隔
																					// 2,hour
																					// (2小时)
																					// 2,day(2天)
		params.put("start_time", str_starttime);// 开始时间
		params.put("end_time", str_endtime);// 结束时间
		params.put("reason_id", reason_id_list.get(mSpinner_leavereason.getSelectedItemPosition()));// 请假事由id
		params.put("others_reason", mEt_otherreason.getText().toString());// 其他原因
		params.put("next_userid", next_userid);// 下一节点人
		params.put("flow_code", StringUtil.noNull(flow_code));//
		params.put("mis", mis);// 请假代办人id，如果是本人就是本人的id

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.configCharset("UTF-8");
		System.out.println(HttpUtils.URL + "?" + params.toString());
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (HrVacateApplyActivity.this==null) {
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
				if (HrVacateApplyActivity.this==null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				super.onSuccess(t);
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						if (StringUtil.isEmpty(next_userid))// 第一次提交为空，获取下一节点人列表
						{

							SubmitLeaveBean submitLeaveBean = jsonInfo.getResultDataToClass(SubmitLeaveBean.class);
							flow_code = submitLeaveBean.flow_code;
							ArrayList<PersonList> listPerson = (ArrayList<PersonList>) submitLeaveBean.userList;
							if (listPerson.size() > 0) {

								String startTime = mSpinner_starttime.getText().toString();
								String endTime = mSpinner_endtime.getText().toString();
								long durTime = 0;// 请假时长

								if (radiounit == "day") {
									try {
										long m1 = (new SimpleDateFormat(StringUtil.DAY_TIME).parse(startTime).getTime());// 开始时间换算成毫秒值
										long m2 = (new SimpleDateFormat(StringUtil.DAY_TIME).parse(endTime).getTime());// 结束时间换算成毫秒值
										durTime = (m2 + 1000 * 60 * 60 * 24 - m1) / (1000 * 60 * 60 * 24);// 把毫秒值转成天数
										if (durTime != Long.parseLong(mDurTime.getText().toString())) {
											isDurTimeRight = false;
										}
									} catch (ParseException e) {
										e.printStackTrace();
									}
								} else if (radiounit == "hour") {
									try {
										long m1 = (new SimpleDateFormat(StringUtil.MINUTE_TIME).parse(startTime).getTime());// 开始时间换算成毫秒值
										long m2 = (new SimpleDateFormat(StringUtil.MINUTE_TIME).parse(endTime).getTime());// 结束时间换算成毫秒值
										durTime = (m2 - m1) / (1000 * 60 * 60);
										if (durTime != Long.parseLong(mDurTime.getText().toString())) {
											isDurTimeRight = false;
										}
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}

								Intent intent = new Intent(HrVacateApplyActivity.this, MyPersonListNoSearchActivity.class);
								intent.putExtra("list", listPerson);
								startActivityForResult(intent, 1);
							}
						} else {
							Toast.makeText(HrVacateApplyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
							finish();
							CURRENTACTIVITY="";
						}

					} else {
						Toast.makeText(getApplicationContext(), "提交失败:" + jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
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
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void commit() throws UnsupportedEncodingException {
		String str_no = mTv_userno.getText().toString();
		String str_name = mTv_username.getText().toString();
		boolean flag = false;
		str_durTime = mDurTime.getText().toString();// 请假时长
		if (StringUtil.isEmpty(str_no)) {// 提交前验证
			Toast.makeText(getApplicationContext(), "员工编号不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(str_name)) {
			Toast.makeText(getApplicationContext(), "员工姓名不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(str_durTime)) {
			Toast.makeText(getApplicationContext(), "请假时长不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(mSpinner_starttime.getText().toString())) {
			Toast.makeText(getApplicationContext(), "开始时间不能为空", Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isEmpty(mSpinner_endtime.getText().toString())) {
			Toast.makeText(getApplicationContext(), "结束时间不能为空", Toast.LENGTH_SHORT).show();
		} else if (radiounit.equals("day") && (Float.valueOf(mTv_usable.getText().toString())) < (Float.valueOf(str_durTime))) {// 单位为天
			Toast.makeText(getApplicationContext(), "请假时长不能大于可用时间", Toast.LENGTH_SHORT).show();
		} else if (radiounit.equals("hour") && (Float.valueOf(mTv_usable.getText().toString()) * 24) < (Float.valueOf(str_durTime))) {// 单位为小时
			Toast.makeText(getApplicationContext(), "请假时长不能大于可用时间", Toast.LENGTH_SHORT).show();
		} else if (radiounit.equals("day")) {
			flag = compareTime(StringUtil.DAY_TIME);
			if (flag) {// 开始时间在结束时间之前
				postData();
			} else {
				Toast.makeText(getApplicationContext(), "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (radiounit.equals("hour")) {
			flag = compareTime(StringUtil.MINUTE_TIME);
			if (flag) {// 开始时间在结束时间之前
				postData();
			} else {
				Toast.makeText(getApplicationContext(), "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

	/**
	 * 比较开始时间与结束时间的先后
	 */
	private boolean compareTime(String pattern) {
		boolean flag = false;
		try {
			if ((new SimpleDateFormat(pattern).parse(mSpinner_starttime.getText().toString())).before(new SimpleDateFormat(pattern).parse(mSpinner_endtime.getText().toString()))||(mSpinner_starttime.getText().toString().equals(mSpinner_endtime.getText().toString()))) {// 结束时间必须>=开始时间
				flag = true;
			} else {
				flag = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {

			if (requestCode == 1) {// 选择人
				System.out.println("data:" + data.getStringExtra("userid"));
				next_userid = data.getStringExtra("userid");
				try {
					if (isDurTimeRight==false) {
						Toast.makeText(getApplication(), "请假时间段差跟请假天数不符，请重新填写！", Toast.LENGTH_LONG).show();
					}else{
						postData();
						
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			}
		} else if (Activity.RESULT_CANCELED == resultCode) {// 没有选择下一节点人（点击取消了）
			flow_code = "";
		}
	}
	public void clickDaiban(View v){
		mSpinner_daiban.performClick();
	}
	public void clickType(View v){
		mSpinner_type.performClick();
	}
	public void clickLeave(View v){
		mSpinner_leave.performClick();
	}
	public void clickLeavereason(View v){
		mSpinner_leavereason.performClick();
	}
}
