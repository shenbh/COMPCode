package com.newland.comp.activity.process;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.my.MyPersonListNoSearchActivity;
import com.newland.comp.adapter.ProAuditAdapter;
import com.newland.comp.bean.AuditInfo;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.PrombleType;
import com.newland.comp.bean.hr.LeaveInfo;
import com.newland.comp.bean.my.PersonList;
import com.newland.comp.bean.process.AuditData;
import com.newland.comp.bean.process.DepartureInfo;
import com.newland.comp.bean.process.ProcessDataComplete;
import com.newland.comp.bean.process.ProcessFlow;
import com.newland.comp.bean.process.ProcessKeyValue;
import com.newland.comp.bean.process.SubmitLeaveBean;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 待办流程详细界面（本地）
 * 
 * 
 */
public class PrToDoDetailActivity extends PrToDoActivity {

	private LinearLayout mLayout_daiban;
	private ImageView mLc_iv, pr_listiv;
	private EditText mSuggest;
	private RadioGroup mButton_group;
	private RadioButton mBtn_0;// 同意
	private RadioButton mBtn_1;// 拒绝or驳回
	private RadioGroup mButton_group_leave;
	private RadioButton mBtn_0_leave;// 同意
	private RadioButton mBtn_1_leave;// 驳回话务经理
	private RadioButton mBtn_2_leave;// 驳回科室经理
	private String flowId;
	LoadingDialog dialog;
	List<ProcessKeyValue> listKeyValue; // 待办详情列表
	LeaveInfo leaveInfo;
	DepartureInfo departureInfo;
	ListView lisView;
	List<AuditData> listFlow; // 待办流程列表
	ProAuditAdapter adapter;
	Boolean flagslist = true;

	ProcessDataComplete processDataComplete;
	private int inputNum = 500;

	String next_userid = "";

	int activityTemp = 0;// 存放Activity.RESULT_OK=-1/Activity.RESULT_CANCELED=0
							// 变量值
	int resultCode;
	int requestFinal;

	private boolean isFirstPost;// 是否执行setResult()

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		processDataComplete = (ProcessDataComplete) getIntent().getSerializableExtra("ProcessDataComplete");
		setContentView(R.layout.prtodo_detail);
		bindViews();

		if (processDataComplete != null) {
			initData();
		}

		getFlowTaskList();

	}

	private void initData() {
		String method = "";
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		if (processDataComplete.proType.equals("vacate")) {// 请假
			method = "getLeaveInfo";
		} else if (processDataComplete.proType.equals("leave")) {// 离职
			method = "getDepartureInfo";
		}
		params.put("method", StringUtil.noNull(method));
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("flowId", StringUtil.noNull(processDataComplete.flowId));// 流程单号
		params.put("taskId", StringUtil.noNull(processDataComplete.taskId));
		params.put("flowCode", StringUtil.noNull(processDataComplete.flowCode));
		params.put("bizId", StringUtil.noNull(processDataComplete.bizId));
		params.put("formPage", StringUtil.noNull(processDataComplete.formPage));

		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				Log.v("newland", strMsg + t.getMessage());
				// load_msg.setVisibility(View.GONE);
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				Log.v("newland", t.toString());
				System.out.println("HttpUtils.URL:" + t.toString());
				// load_msg.setVisibility(View.GONE);
				dialog.dismiss();
				// JsonInfoV2 jsonInfov2 = null;
				JsonInfoV3 jsonInfov3 = null;
				try {

					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov3 != null) {
					if (JsonInfoV3.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) {

						// JsonInfo
						// jsonInfo=jsonInfov3.getResultDataToClass(JsonInfo.class);
						listKeyValue = new ArrayList<ProcessKeyValue>();
						if (processDataComplete.proType.equals("vacate")) {// 请假
							leaveInfo = jsonInfov3.getResultDataToClass(LeaveInfo.class);
							ProcessKeyValue processKeyValue1 = new ProcessKeyValue();
							processKeyValue1.keyname = "员工编号";
							ProcessKeyValue processKeyValue2 = new ProcessKeyValue();
							processKeyValue2.keyname = "员工姓名";
							ProcessKeyValue processKeyValue3 = new ProcessKeyValue();
							processKeyValue3.keyname = "部门名称";
							ProcessKeyValue processKeyValue4 = new ProcessKeyValue();
							processKeyValue4.keyname = "开始时间";
							ProcessKeyValue processKeyValue5 = new ProcessKeyValue();
							processKeyValue5.keyname = "结束时间";
							ProcessKeyValue processKeyValue6 = new ProcessKeyValue();
							processKeyValue6.keyname = "请假时长";
							ProcessKeyValue processKeyValue7 = new ProcessKeyValue();
							processKeyValue7.keyname = "请假类型";
							ProcessKeyValue processKeyValue8 = new ProcessKeyValue();
							processKeyValue8.keyname = "是否离开省市";
							ProcessKeyValue processKeyValue9 = new ProcessKeyValue();
							processKeyValue9.keyname = "代办人部门";
							ProcessKeyValue processKeyValue10 = new ProcessKeyValue();
							processKeyValue10.keyname = "代办人姓名";
							ProcessKeyValue processKeyValue11 = new ProcessKeyValue();
							processKeyValue11.keyname = "代办人编号";
							ProcessKeyValue processKeyValue12 = new ProcessKeyValue();
							processKeyValue12.keyname = "请假原因";
							try {
								processKeyValue1.keyvalue = URLDecoder.decode(leaveInfo.mis, "UTF-8");
								processKeyValue2.keyvalue = URLDecoder.decode(leaveInfo.username, "UTF-8");
								processKeyValue3.keyvalue = URLDecoder.decode(leaveInfo.organName, "UTF-8");
								processKeyValue4.keyvalue = URLDecoder.decode(leaveInfo.begineTime, "UTF-8");
								processKeyValue5.keyvalue = URLDecoder.decode(leaveInfo.endTime, "UTF-8");
								processKeyValue6.keyvalue = URLDecoder.decode(leaveInfo.lengthUnit, "UTF-8");
								processKeyValue7.keyvalue = URLDecoder.decode(leaveInfo.type, "UTF-8");
								processKeyValue8.keyvalue = URLDecoder.decode(leaveInfo.outOf, "UTF-8");
								processKeyValue9.keyvalue = URLDecoder.decode(leaveInfo.fillStaffOrg, "UTF-8");
								processKeyValue10.keyvalue = URLDecoder.decode(leaveInfo.fillStaffName, "UTF-8");
								processKeyValue11.keyvalue = URLDecoder.decode(leaveInfo.fillStaffMis, "UTF-8");
								processKeyValue12.keyvalue = URLDecoder.decode(leaveInfo.description, "UTF-8");
								listKeyValue.add(processKeyValue1);
								listKeyValue.add(processKeyValue2);
								listKeyValue.add(processKeyValue3);
								listKeyValue.add(processKeyValue4);
								listKeyValue.add(processKeyValue5);
								listKeyValue.add(processKeyValue6);
								listKeyValue.add(processKeyValue7);
								listKeyValue.add(processKeyValue8);
								listKeyValue.add(processKeyValue9);
								listKeyValue.add(processKeyValue10);
								listKeyValue.add(processKeyValue11);
								listKeyValue.add(processKeyValue12);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							// 重新赋值
							processDataComplete.flowId = leaveInfo.flowId;
							processDataComplete.taskId = leaveInfo.taskId;
							processDataComplete.flowCode = leaveInfo.flowCode;
							processDataComplete.bizId = leaveInfo.bizId;
							processDataComplete.formPage = leaveInfo.formPage;
							// }
						} else if (processDataComplete.proType.equals("leave")) {// 离职
							departureInfo = jsonInfov3.getResultDataToClass(DepartureInfo.class);
							ProcessKeyValue processKeyValue1 = new ProcessKeyValue();
							processKeyValue1.keyname = "员工编号";
							ProcessKeyValue processKeyValue2 = new ProcessKeyValue();
							processKeyValue2.keyname = "员工姓名";
							ProcessKeyValue processKeyValue3 = new ProcessKeyValue();
							processKeyValue3.keyname = "身份证";
							ProcessKeyValue processKeyValue4 = new ProcessKeyValue();
							processKeyValue4.keyname = "性别";
							ProcessKeyValue processKeyValue5 = new ProcessKeyValue();
							processKeyValue5.keyname = "学历";
							ProcessKeyValue processKeyValue6 = new ProcessKeyValue();
							processKeyValue6.keyname = "部门";
							ProcessKeyValue processKeyValue7 = new ProcessKeyValue();
							processKeyValue7.keyname = "用工类型";
							ProcessKeyValue processKeyValue8 = new ProcessKeyValue();
							processKeyValue8.keyname = "职位";
							ProcessKeyValue processKeyValue9 = new ProcessKeyValue();
							processKeyValue9.keyname = "入司时间";
							ProcessKeyValue processKeyValue10 = new ProcessKeyValue();
							processKeyValue10.keyname = "拟离职时间";
							ProcessKeyValue processKeyValue11 = new ProcessKeyValue();
							processKeyValue11.keyname = "离职类型";
							ProcessKeyValue processKeyValue12 = new ProcessKeyValue();
							processKeyValue12.keyname = "离职备注";
							try {
								processKeyValue1.keyvalue = URLDecoder.decode(departureInfo.staffId, "UTF-8");
								processKeyValue2.keyvalue = URLDecoder.decode(departureInfo.staffName, "UTF-8");
								processKeyValue3.keyvalue = URLDecoder.decode(departureInfo.identitycardNumber, "UTF-8");
								processKeyValue4.keyvalue = URLDecoder.decode(departureInfo.gender, "UTF-8");
								processKeyValue5.keyvalue = URLDecoder.decode(departureInfo.educational, "UTF-8");
								processKeyValue6.keyvalue = URLDecoder.decode(departureInfo.organName, "UTF-8");
								processKeyValue7.keyvalue = URLDecoder.decode(departureInfo.userType, "UTF-8");
								processKeyValue8.keyvalue = URLDecoder.decode(departureInfo.jobName, "UTF-8");
								processKeyValue9.keyvalue = URLDecoder.decode(departureInfo.joinUnitTime, "UTF-8");
								processKeyValue10.keyvalue = URLDecoder.decode(departureInfo.departureDate, "UTF-8");
								processKeyValue11.keyvalue = URLDecoder.decode(departureInfo.departureType, "UTF-8");
								processKeyValue12.keyvalue = URLDecoder.decode(departureInfo.description, "UTF-8");
								listKeyValue.add(processKeyValue1);
								listKeyValue.add(processKeyValue2);
								listKeyValue.add(processKeyValue3);
								listKeyValue.add(processKeyValue4);
								listKeyValue.add(processKeyValue5);
								listKeyValue.add(processKeyValue6);
								listKeyValue.add(processKeyValue7);
								listKeyValue.add(processKeyValue8);
								listKeyValue.add(processKeyValue9);
								listKeyValue.add(processKeyValue10);
								listKeyValue.add(processKeyValue11);
								listKeyValue.add(processKeyValue12);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							// 重新赋值
							processDataComplete.flowId = departureInfo.flowId;
							processDataComplete.taskId = departureInfo.taskId;
							processDataComplete.flowCode = departureInfo.flowCode;
							processDataComplete.bizId = departureInfo.bizId;
							processDataComplete.formPage = departureInfo.formPage;
						}

						if (departureInfo != null || leaveInfo != null) // 待办详情，用于显示原始流程信息
						{
							setProcessKeyValue();
						}
					} else {
						if (StringUtil.isNotEmpty(jsonInfov3.getResultDesc())) {
							Toast.makeText(getApplicationContext(), jsonInfov3.getResultDesc(), 1000).show();
						}// 显示登录失败提示
					}
				}
			}
		});
	}

	/**
	 * 设置待办详情，用于显示原始流程信息
	 */
	private void setProcessKeyValue() {
		mLayout_daiban.removeAllViews();
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
		lp1.rightMargin = 10;
		lp1.topMargin = 2;
		lp1.bottomMargin = 2;

		for (int i = 0; i < listKeyValue.size(); i++) {
			// keyValue为空的不显示
//			if (StringUtil.isNotEmpty(listKeyValue.get(i).keyvalue)) {
				if (listKeyValue.get(i).keyname.equals("请假原因") || listKeyValue.get(i).keyname.equals("离职类型") || listKeyValue.get(i).keyname.equals("身份证") || listKeyValue.get(i).keyname.equals("离职备注")) {// 请假原因另起一行显示
					mainLayout = new LinearLayout(this);
					mainLayout.setOrientation(LinearLayout.HORIZONTAL);
					TextView tx = new TextView(this);
					tx.setText(listKeyValue.get(i).keyname + ":  " + listKeyValue.get(i).keyvalue);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(48, 48, 48));
					tx.setGravity(Gravity.LEFT);
					mainLayout.addView(tx, lp1);
					if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
						mLayout_daiban.addView(mainLayout, mainlp);
					}
				} else {
					if (mainLayout.getChildCount() < 2) { // 显示一行
						TextView tx = new TextView(this);
						tx.setText(listKeyValue.get(i).keyname + ":  " + listKeyValue.get(i).keyvalue);
						tx.setGravity(Gravity.CENTER);
						tx.setTextColor(Color.rgb(48, 48, 48));
						tx.setGravity(Gravity.LEFT);
						// tx.setPadding(10, 0, 10, 0);
						mainLayout.addView(tx, lp1);
						if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
							mLayout_daiban.addView(mainLayout, mainlp);
						}
					} else {
						mainLayout = new LinearLayout(this);
						mainLayout.setOrientation(LinearLayout.HORIZONTAL);
						TextView tx = new TextView(this);
						tx.setText(listKeyValue.get(i).keyname + ":  " + listKeyValue.get(i).keyvalue);
						tx.setGravity(Gravity.CENTER);
						tx.setTextColor(Color.rgb(48, 48, 48));
						tx.setGravity(Gravity.LEFT);
						mainLayout.addView(tx, lp1);
						if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
							mLayout_daiban.addView(mainLayout, mainlp);
						}
					}
				}
//			}
		}
	}

	// End Of Content View Elements
	private void bindViews() {

		mLayout_daiban = (LinearLayout) findViewById(R.id.layout_daiban);
		mLc_iv = (ImageView) findViewById(R.id.lc_iv);
		mSuggest = (EditText) findViewById(R.id.suggest);
		lisView = (ListView) findViewById(R.id.prtodo_detail_listv);
		pr_listiv = (ImageView) findViewById(R.id.prtodo_detail_image);

		pr_listiv.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (pr_listiv.isClickable()) {
					flagslist = !flagslist;
					System.out.println("+++++++++============" + flagslist);
					if (flagslist) {
						pr_listiv.setBackgroundResource(R.drawable.turn_down);
						lisView.setVisibility(View.VISIBLE);
					} else {
						pr_listiv.setBackgroundResource(R.drawable.jt1);
						lisView.setVisibility(View.GONE);
					}
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
				// tv_num.setText("" + number);
				selectionStart = mSuggest.getSelectionStart();
				selectionEnd = mSuggest.getSelectionEnd();
				// System.out.println("start="+selectionStart+",end="+selectionEnd);
				if (temp.length() > inputNum) {
					Toast.makeText(getApplicationContext(), "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mSuggest.setText(s);
					mSuggest.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
		mButton_group = (RadioGroup) findViewById(R.id.button_group);
		mBtn_0 = (RadioButton) findViewById(R.id.btn_0);
		mBtn_1 = (RadioButton) findViewById(R.id.btn_1);
		mButton_group_leave = (RadioGroup) findViewById(R.id.button_group_leave);
		mBtn_0_leave = (RadioButton) findViewById(R.id.btn_0_leave);
		mBtn_1_leave = (RadioButton) findViewById(R.id.btn_1_leave);
		mBtn_2_leave = (RadioButton) findViewById(R.id.btn_2_leave);

		if (processDataComplete.formPage.equals("hr_dpt_approve_com.jsp")) {// 三个按钮特殊处理
			mButton_group.setVisibility(View.GONE);
			mButton_group_leave.setVisibility(View.VISIBLE);
		} else {
			mButton_group.setVisibility(View.VISIBLE);
			mButton_group_leave.setVisibility(View.GONE);
			if (processDataComplete.proType.equals("leave")) {// 离职
				mBtn_1.setText("驳回");
			} else if (processDataComplete.proType.equals("vacate")) {// 请假
				mBtn_1.setText("拒绝");
			}
		}

		// db_pro= findViewById(R.id.db_pro);
		// jt = findViewById(R.id.jt);
		dialog = new LoadingDialog(this);

		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("待办详情");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					PrToDoDetailActivity.this.finish();

				}
			});
		}

	}

	/**
	 * @param view
	 */
	public void submit_from(View view) {
		try {
			postData();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请假审批提交or离职审批提交
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void postData() throws UnsupportedEncodingException {
		String method = "";
		String reject_to = "";
		isFirstPost = true;
		
		dialog.setTvMessage("正在提交...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		if (processDataComplete.proType.equals("vacate")) {// 请假
			method = "leaveAuditSubmit";
			RadioButton radioButton = (RadioButton) findViewById(mButton_group.getCheckedRadioButtonId());
			if (radioButton.getTag().toString().equals("0")) {// accept
				params.put("audit_result", "accept");
			} else if (radioButton.getTag().toString().equals("1")) {// reject
				params.put("audit_result", "reject");
			}
		} else if (processDataComplete.proType.equals("leave")) {// 离职
			method = "departureAuditSubmit";
			if (processDataComplete.formPage.equals("hr_dpt_approve_com.jsp")) {
				RadioButton radioButton_leave = (RadioButton) findViewById(mButton_group_leave.getCheckedRadioButtonId());
				if (radioButton_leave.getTag().toString().equals("0")) {// accept
					reject_to = "";
					params.put("audit_result", "accept");
				} else {// reject
					if (radioButton_leave.getTag().toString().equals("1")) {// 驳回专区
						reject_to = "tel_mgr";
					} else if (radioButton_leave.getTag().toString().equals("2")) {// 驳回科室
						reject_to = "dept_mgr";
					}
					params.put("audit_result", "reject");
				}
			} else {
				RadioButton radioButton = (RadioButton) findViewById(mButton_group.getCheckedRadioButtonId());
				if (radioButton.getTag().toString().equals("0")) {// accept
					params.put("audit_result", "accept");
				} else if (radioButton.getTag().toString().equals("1")) {// reject
					params.put("audit_result", "reject");
				}
				reject_to = "";
			}
			params.put("reject_to", reject_to);
		}

		params.put("userid", userid);
		params.put("method", method);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("flowId", StringUtil.noNull(processDataComplete.flowId));// 流程单号
		params.put("taskId", StringUtil.noNull(processDataComplete.taskId));
		params.put("bizId", StringUtil.noNull(processDataComplete.bizId));
		params.put("flowCode", StringUtil.noNull(processDataComplete.flowCode));
		params.put("formPage", StringUtil.noNull(processDataComplete.formPage));
		params.put("comment", URLEncoder.encode(StringUtil.noNull(mSuggest.getText().toString()), "UTF-8"));

		if (processDataComplete.proType.equals("leave")) {// 离职
			// params.put("reject_to",
			// URLEncoder.encode(StringUtil.noNull(processDataComplete.reject_to),"UTF-8"));
			params.put("next_userid", StringUtil.noNull(next_userid));
		}
		if (processDataComplete.proType.equals("vacate")) {
			params.put("next_userid", StringUtil.noNull(next_userid));
		}
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				Log.v("newland", strMsg + t.getMessage());
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				Log.v("newland", t.toString());
				System.out.println(HttpUtils.URL + ":" + t.toString());
				dialog.dismiss();
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov3 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) {// 正常返回数据
						if (StringUtil.isEmpty(next_userid)) {// 第一次提交为空，获取下一节点人列表
							SubmitLeaveBean submitLeaveBean = jsonInfov3.getResultDataToClass(SubmitLeaveBean.class);
							ArrayList<PersonList> listPerson = (ArrayList<PersonList>) submitLeaveBean.userList;
							if (listPerson.size() > 0) {
								Intent intent = new Intent(PrToDoDetailActivity.this, MyPersonListNoSearchActivity.class);
								intent.putExtra("list", listPerson);
								startActivityForResult(intent, 1);
								isFirstPost = false;
							} else {
								Toast.makeText(getApplicationContext(), "提交成功!", 1000).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "提交成功!", 1000).show();
						}
						if (isFirstPost) {
							Intent intent = new Intent();
							intent.putExtra("ok", "0");

							setResult(Activity.RESULT_OK, intent);
							finish();
						}

					} else {
						if (StringUtil.isNotEmpty(jsonInfov3.getResultDesc())) {
							Toast.makeText(getApplicationContext(), jsonInfov3.getResultDesc(), 1000).show();
						}
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.resultCode = resultCode;
		if (Activity.RESULT_OK == resultCode) {

			if (requestCode == 1) // 选择人
			{
				System.out.println("data:" + data.getStringExtra("userid"));
				next_userid = data.getStringExtra("userid");
				try {
					requestFinal = 2;

					postData();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 网络请求,节点
	 */
	private void getFlowTaskList() {
		dialog.setTvMessage("正在加载...");
		if (!isFinishing()) {
			dialog.show(true);
		}
		String userid = StringUtil.noNull(SharedPreferencesUtils.getConfigStr(PrToDoDetailActivity.this, BaseActivity.CASH_NAME, "userid"));
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "getFlowTaskList");
		params.put("flowId", StringUtil.noNull(processDataComplete.flowId));
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (PrToDoDetailActivity.this == null) {
					return;
				}
				dialog.dismiss();
				System.out.println("getFlowTaskList--" + t.toString());
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				try {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) {// 正常返回数据

						listFlow = jsonInfov3.getResultDataKeyToClass(t.toString(), "resultData", AuditData.class);

						if (listFlow != null) {
							lisView.setVisibility(View.VISIBLE);
							adapter = new ProAuditAdapter(PrToDoDetailActivity.this, listFlow);
							lisView.setAdapter(adapter);
						} else {
							lisView.setVisibility(View.GONE);
						}
					} else {
						Toast.makeText(PrToDoDetailActivity.this, jsonInfov3.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				} catch (Exception e) {

				}
			}
		});
	}

	@Override
	public void onBackPressed() {

		setResult(Activity.RESULT_CANCELED);
		finish();
	}

}
