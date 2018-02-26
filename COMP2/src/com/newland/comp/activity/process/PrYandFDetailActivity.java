package com.newland.comp.activity.process;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.ProAuditAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.hr.LeaveInfo;
import com.newland.comp.bean.process.AuditData;
import com.newland.comp.bean.process.DepartureInfo;
import com.newland.comp.bean.process.ProcessDataComplete;
import com.newland.comp.bean.process.ProcessFlow;
import com.newland.comp.bean.process.ProcessKeyValue;
import com.newland.comp.common.AppContext;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 已办流程||我发起的流程的详细界面
 * 
 * @author H81
 * 
 *         2015年4月29日 下午1:52:54
 * @version 1.0.0
 */
public class PrYandFDetailActivity extends BaseActivity {
	private LinearLayout mLayout_daiban;
	private LinearLayout mDb_pro;
	private ImageView mLc_iv;
	private ImageView mJt;// 箭头
	private ListView listView;
	// List<ProcessKeyValue> listKeyValue; // 待办详情列表
	List<AuditData> listFlow; // 待办流程列表
	private String intentData;// 类型
//	LoadingDialog dialog;

	private TextView mFirstTitle;
	private TextView mSecondTitle;

	ProcessDataComplete processDataComplete;
	ProAuditAdapter adapter;

	LeaveInfo leaveInfo;
	DepartureInfo departureInfo;

	private TextView mName;
	private TextView mName_content;
	private TextView mDepartment;
	private TextView mDepartment_content;
	private TextView mStart;
	private TextView mStart_content;
	private TextView mUserid;
	private TextView mUserid_content;
	private TextView mEnd;
	private TextView mEnd_content;
	private TextView mLeave;
	private TextView mLeave_content;
	private TextView mType;
	private TextView mType_content;
	private TextView mIsleave;
	private TextView mIsleave_content;
	private TextView mDaibandepartment;
	private TextView mDaibandepartment_content;
	private TextView mDaibanname;
	private TextView mDaibanname_content;
	private TextView mDaibanid;
	private TextView mDaibanid_content;
	private TextView mLeavereason;
	private TextView mLeavereason_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.pryibandetail);
		// setContentView(R.layout.prdetail);
		initDialog(this);
		getIntentData();
		setTitle();
		findViewById();
		bindViews();
		getFlowTaskList();
		if (processDataComplete != null) {
			// initData();
			initData2();
		}
	}

	private void findViewById() {

		mName = (TextView) findViewById(R.id.name);
		mName_content = (TextView) findViewById(R.id.name_content);
		mDepartment = (TextView) findViewById(R.id.department);
		mDepartment_content = (TextView) findViewById(R.id.department_content);
		mStart = (TextView) findViewById(R.id.start);
		mStart_content = (TextView) findViewById(R.id.start_content);
		mUserid = (TextView) findViewById(R.id.userid);
		mUserid_content = (TextView) findViewById(R.id.userid_content);
		mEnd = (TextView) findViewById(R.id.end);
		mEnd_content = (TextView) findViewById(R.id.end_content);
		mLeave = (TextView) findViewById(R.id.leave);
		mLeave_content = (TextView) findViewById(R.id.leave_content);
		mType = (TextView) findViewById(R.id.type);
		mType_content = (TextView) findViewById(R.id.type_content);
		mIsleave = (TextView) findViewById(R.id.isleave);
		mIsleave_content = (TextView) findViewById(R.id.isleave_content);
		mDaibandepartment = (TextView) findViewById(R.id.daibandepartment);
		mDaibandepartment_content = (TextView) findViewById(R.id.daibandepartment_content);
		mDaibanname = (TextView) findViewById(R.id.daibanname);
		mDaibanname_content = (TextView) findViewById(R.id.daibanname_content);
		mDaibanid = (TextView) findViewById(R.id.daibanid);
		mDaibanid_content = (TextView) findViewById(R.id.daibanid_content);
		mLeavereason = (TextView) findViewById(R.id.leavereason);
		mLeavereason_content = (TextView) findViewById(R.id.leavereason_content);
	}

	// private void initData() {
	// listKeyValue = new ArrayList<ProcessKeyValue>();
	// ProcessKeyValue processKeyValue1 = new ProcessKeyValue();
	// processKeyValue1.keyname = "流程id";
	// ProcessKeyValue processKeyValue2 = new ProcessKeyValue();
	// processKeyValue2.keyname = "流程标题";
	// ProcessKeyValue processKeyValue3 = new ProcessKeyValue();
	// processKeyValue3.keyname = "流程创建人";
	// ProcessKeyValue processKeyValue4 = new ProcessKeyValue();
	// processKeyValue4.keyname = "状态";
	// ProcessKeyValue processKeyValue5 = new ProcessKeyValue();
	// processKeyValue5.keyname = "流程创建时间";
	// ProcessKeyValue processKeyValue6 = new ProcessKeyValue();
	// processKeyValue6.keyname = "当前处理环节";
	// ProcessKeyValue processKeyValue7 = new ProcessKeyValue();
	// processKeyValue7.keyname = "到达本人时间";
	// processKeyValue1.keyvalue =
	// StringUtil.noNull(processDataComplete.flowId);
	// processKeyValue2.keyvalue =
	// StringUtil.noNull(processDataComplete.flowTitle);
	// processKeyValue3.keyvalue =
	// StringUtil.noNull(processDataComplete.proPerson);
	// processKeyValue4.keyvalue =
	// StringUtil.noNull(processDataComplete.statue);
	// processKeyValue5.keyvalue =
	// StringUtil.noNull(processDataComplete.createTime);
	// processKeyValue6.keyvalue =
	// StringUtil.noNull(processDataComplete.arrivePro);
	// processKeyValue7.keyvalue =
	// StringUtil.noNull(processDataComplete.arriveTime);
	// listKeyValue.add(processKeyValue1);
	// listKeyValue.add(processKeyValue2);
	// listKeyValue.add(processKeyValue3);
	// listKeyValue.add(processKeyValue4);
	// listKeyValue.add(processKeyValue5);
	// listKeyValue.add(processKeyValue6);
	// setProcessKeyValue();
	// }

	private void initData2() {

		String method = "";
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
				if (PrYandFDetailActivity.this == null) {
					return;
				}
				Log.v("newland", strMsg + t.getMessage());
				// load_msg.setVisibility(View.GONE);
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (PrYandFDetailActivity.this == null) {
					return;
				}
				Log.v("newland", t.toString());
				System.out.println("HttpUtils.URL:" + t.toString());
				// load_msg.setVisibility(View.GONE);
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
						// listKeyValue = new ArrayList<ProcessKeyValue>();
						// if (processDataComplete.proType.equals("vacate")) {//
						// 请假
						// leaveInfo =
						// jsonInfov3.getResultDataToClass(LeaveInfo.class);
						// ProcessKeyValue processKeyValue1 = new
						// ProcessKeyValue();
						// processKeyValue1.keyname = "员工编号";
						// ProcessKeyValue processKeyValue2 = new
						// ProcessKeyValue();
						// processKeyValue2.keyname = "员工姓名";
						// ProcessKeyValue processKeyValue3 = new
						// ProcessKeyValue();
						// processKeyValue3.keyname = "部门名称";
						// ProcessKeyValue processKeyValue4 = new
						// ProcessKeyValue();
						// processKeyValue4.keyname = "开始时间";
						// ProcessKeyValue processKeyValue5 = new
						// ProcessKeyValue();
						// processKeyValue5.keyname = "结束时间";
						// ProcessKeyValue processKeyValue6 = new
						// ProcessKeyValue();
						// processKeyValue6.keyname = "请假时长";
						// ProcessKeyValue processKeyValue7 = new
						// ProcessKeyValue();
						// processKeyValue7.keyname = "请假类型";
						// ProcessKeyValue processKeyValue8 = new
						// ProcessKeyValue();
						// processKeyValue8.keyname = "是否离开省市";
						// ProcessKeyValue processKeyValue9 = new
						// ProcessKeyValue();
						// processKeyValue9.keyname = "代办人部门";
						// ProcessKeyValue processKeyValue10 = new
						// ProcessKeyValue();
						// processKeyValue10.keyname = "代办人姓名";
						// ProcessKeyValue processKeyValue11 = new
						// ProcessKeyValue();
						// processKeyValue11.keyname = "代办人编号";
						// ProcessKeyValue processKeyValue12 = new
						// ProcessKeyValue();
						// processKeyValue12.keyname = "请假原因";
						// try {
						// processKeyValue1.keyvalue =
						// URLDecoder.decode(leaveInfo.mis, "UTF-8");
						// processKeyValue2.keyvalue =
						// URLDecoder.decode(leaveInfo.username, "UTF-8");
						// processKeyValue3.keyvalue =
						// URLDecoder.decode(leaveInfo.organName, "UTF-8");
						// processKeyValue4.keyvalue =
						// URLDecoder.decode(leaveInfo.begineTime, "UTF-8");
						// processKeyValue5.keyvalue =
						// URLDecoder.decode(leaveInfo.endTime, "UTF-8");
						// processKeyValue6.keyvalue =
						// URLDecoder.decode(leaveInfo.lengthUnit, "UTF-8");
						// processKeyValue7.keyvalue =
						// URLDecoder.decode(leaveInfo.type, "UTF-8");
						// processKeyValue8.keyvalue =
						// URLDecoder.decode(leaveInfo.outOf, "UTF-8");
						// processKeyValue9.keyvalue =
						// URLDecoder.decode(leaveInfo.fillStaffOrg, "UTF-8");
						// processKeyValue10.keyvalue =
						// URLDecoder.decode(leaveInfo.fillStaffName, "UTF-8");
						// processKeyValue11.keyvalue =
						// URLDecoder.decode(leaveInfo.fillStaffMis, "UTF-8");
						// processKeyValue12.keyvalue =
						// URLDecoder.decode(leaveInfo.description, "UTF-8");
						// listKeyValue.add(processKeyValue1);
						// listKeyValue.add(processKeyValue2);
						// listKeyValue.add(processKeyValue3);
						// listKeyValue.add(processKeyValue4);
						// listKeyValue.add(processKeyValue5);
						// listKeyValue.add(processKeyValue6);
						// listKeyValue.add(processKeyValue7);
						// listKeyValue.add(processKeyValue8);
						// listKeyValue.add(processKeyValue9);
						// listKeyValue.add(processKeyValue10);
						// listKeyValue.add(processKeyValue11);
						// listKeyValue.add(processKeyValue12);
						// } catch (UnsupportedEncodingException e) {
						// e.printStackTrace();
						// }
						// // 重新赋值
						// processDataComplete.flowId = leaveInfo.flowId;
						// processDataComplete.taskId = leaveInfo.taskId;
						// processDataComplete.flowCode = leaveInfo.flowCode;
						// processDataComplete.bizId = leaveInfo.bizId;
						// processDataComplete.formPage = leaveInfo.formPage;
						// // }
						// } else if
						// (processDataComplete.proType.equals("leave")) {// 离职
						// departureInfo =
						// jsonInfov3.getResultDataToClass(DepartureInfo.class);
						// ProcessKeyValue processKeyValue1 = new
						// ProcessKeyValue();
						// processKeyValue1.keyname = "员工编号";
						// ProcessKeyValue processKeyValue2 = new
						// ProcessKeyValue();
						// processKeyValue2.keyname = "员工姓名";
						// ProcessKeyValue processKeyValue3 = new
						// ProcessKeyValue();
						// processKeyValue3.keyname = "身份证";
						// ProcessKeyValue processKeyValue4 = new
						// ProcessKeyValue();
						// processKeyValue4.keyname = "性别";
						// ProcessKeyValue processKeyValue5 = new
						// ProcessKeyValue();
						// processKeyValue5.keyname = "学历";
						// ProcessKeyValue processKeyValue6 = new
						// ProcessKeyValue();
						// processKeyValue6.keyname = "部门";
						// ProcessKeyValue processKeyValue7 = new
						// ProcessKeyValue();
						// processKeyValue7.keyname = "用工类型";
						// ProcessKeyValue processKeyValue8 = new
						// ProcessKeyValue();
						// processKeyValue8.keyname = "职位";
						// ProcessKeyValue processKeyValue9 = new
						// ProcessKeyValue();
						// processKeyValue9.keyname = "入司时间";
						// ProcessKeyValue processKeyValue10 = new
						// ProcessKeyValue();
						// processKeyValue10.keyname = "拟离职时间";
						// ProcessKeyValue processKeyValue11 = new
						// ProcessKeyValue();
						// processKeyValue11.keyname = "离职类型";
						// ProcessKeyValue processKeyValue12 = new
						// ProcessKeyValue();
						// processKeyValue12.keyname = "离职备注";
						// try {
						// processKeyValue1.keyvalue =
						// URLDecoder.decode(departureInfo.staffId, "UTF-8");
						// processKeyValue2.keyvalue =
						// URLDecoder.decode(departureInfo.staffName, "UTF-8");
						// processKeyValue3.keyvalue =
						// URLDecoder.decode(departureInfo.identitycardNumber,
						// "UTF-8");
						// processKeyValue4.keyvalue =
						// URLDecoder.decode(departureInfo.gender, "UTF-8");
						// processKeyValue5.keyvalue =
						// URLDecoder.decode(departureInfo.educational,
						// "UTF-8");
						// processKeyValue6.keyvalue =
						// URLDecoder.decode(departureInfo.organName, "UTF-8");
						// processKeyValue7.keyvalue =
						// URLDecoder.decode(departureInfo.userType, "UTF-8");
						// processKeyValue8.keyvalue =
						// URLDecoder.decode(departureInfo.jobName, "UTF-8");
						// processKeyValue9.keyvalue =
						// URLDecoder.decode(departureInfo.joinUnitTime,
						// "UTF-8");
						// processKeyValue10.keyvalue =
						// URLDecoder.decode(departureInfo.departureDate,
						// "UTF-8");
						// processKeyValue11.keyvalue =
						// URLDecoder.decode(departureInfo.departureType,
						// "UTF-8");
						// processKeyValue12.keyvalue =
						// URLDecoder.decode(departureInfo.description,
						// "UTF-8");
						// listKeyValue.add(processKeyValue1);
						// listKeyValue.add(processKeyValue2);
						// listKeyValue.add(processKeyValue3);
						// listKeyValue.add(processKeyValue4);
						// listKeyValue.add(processKeyValue5);
						// listKeyValue.add(processKeyValue6);
						// listKeyValue.add(processKeyValue7);
						// listKeyValue.add(processKeyValue8);
						// listKeyValue.add(processKeyValue9);
						// listKeyValue.add(processKeyValue10);
						// listKeyValue.add(processKeyValue11);
						// listKeyValue.add(processKeyValue12);
						// } catch (UnsupportedEncodingException e) {
						// e.printStackTrace();
						// }
						// // 重新赋值
						// processDataComplete.flowId = departureInfo.flowId;
						// processDataComplete.taskId = departureInfo.taskId;
						// processDataComplete.flowCode =
						// departureInfo.flowCode;
						// processDataComplete.bizId = departureInfo.bizId;
						// processDataComplete.formPage =
						// departureInfo.formPage;
						// }

						if (processDataComplete.proType.equals("vacate")) {// 请假
							leaveInfo = jsonInfov3.getResultDataToClass(LeaveInfo.class);
						}
						if (processDataComplete.proType.equals("leave")) {// 离职
							departureInfo = jsonInfov3.getResultDataToClass(DepartureInfo.class);
						}
						if (departureInfo != null) // 待办详情，用于显示原始流程信息
						{
							initDepartureInfo(departureInfo);
							// setProcessKeyValue();
						}
						if (leaveInfo != null) {
							initLeaveData(leaveInfo);
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
	 * 写死的请假详情内容
	 * 
	 */
	private void initLeaveData(LeaveInfo leaveInfo) {
		mName.setText("员工姓名:");
		mName_content.setText(StringUtil.noNull(leaveInfo.username));
		mDepartment.setText("部门名称:");
		mDepartment_content.setText(StringUtil.noNull(leaveInfo.organName));
		mStart.setText("开始时间:");
		mStart_content.setText(StringUtil.noNull(leaveInfo.begineTime));
		mUserid.setText("员工编号:");
		mUserid_content.setText(StringUtil.noNull(leaveInfo.mis));
		mEnd.setText("结束时间:");
		mEnd_content.setText(StringUtil.noNull(leaveInfo.endTime));
		mLeave.setText("请假时长:");
		mLeave_content.setText(StringUtil.noNull(leaveInfo.lengthUnit));
		mType.setText("请假类型:");
		mType_content.setText(StringUtil.noNull(leaveInfo.type));
		mIsleave.setText("是否离开省市:");
		mIsleave_content.setText(StringUtil.noNull(leaveInfo.outOf));
		mDaibandepartment.setText("代办人部门:");
		mDaibandepartment_content.setText(StringUtil.noNull(leaveInfo.fillStaffOrg));
		mDaibanname.setText("代办人姓名:");
		mDaibanname_content.setText(StringUtil.noNull(leaveInfo.fillStaffName));
		mDaibanid.setText("代办人编号:");
		mDaibanid_content.setText(StringUtil.noNull(leaveInfo.fillStaffMis));
		mLeavereason.setText("请假原因:");
		mLeavereason_content.setText(StringUtil.noNull(leaveInfo.description));
	}

	/**
	 * 写死的离职详情内容
	 */
	private void initDepartureInfo(DepartureInfo departureInfo) {
		mName.setText("员工编号:");
		mName_content.setText(StringUtil.noNull(departureInfo.staffId));
		mDepartment.setText("身份证:");
		mDepartment_content.setText(StringUtil.noNull(departureInfo.identitycardNumber));
		mStart.setText("性别:");
		mStart_content.setText(StringUtil.noNull(departureInfo.gender));
		mUserid.setText("员工姓名:");
		mUserid_content.setText(StringUtil.noNull(departureInfo.staffName));
		mEnd.setText("学历:");
		mEnd_content.setText(StringUtil.noNull(departureInfo.educational));
		mLeave.setText("部门:");
		mLeave_content.setText(StringUtil.noNull(departureInfo.organName));
		mType.setText("用工类型:");
		mType_content.setText(StringUtil.noNull(departureInfo.userType));
		mIsleave.setText("职位:");
		mIsleave_content.setText(StringUtil.noNull(departureInfo.jobName));
		mDaibandepartment.setText("入司时间:");
		mDaibandepartment_content.setText(StringUtil.noNull(departureInfo.joinUnitTime));
		mDaibanname.setText("拟离职时间:");
		mDaibanname_content.setText(StringUtil.noNull(departureInfo.departureDate));
		mDaibanid.setText("离职类型:");
		mDaibanid_content.setText(StringUtil.noNull(departureInfo.departureType));
		mLeavereason.setText("离职备注:");
		mLeavereason_content.setText(StringUtil.noNull(departureInfo.description));
	}

	/**
	 * 获取Intent过来的数据
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		intentData = intent.getStringExtra("type");
		processDataComplete = (ProcessDataComplete) intent.getSerializableExtra("ProcessDataComplete");
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
			if (intentData.equals("finished")) {
				center_tv.setText("已办流程详情");
			} else if (intentData.equals("launch")) {
				center_tv.setText("我发起的流程详情");
			}
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
//		dialog = new LoadingDialog(this);
		mLayout_daiban = (LinearLayout) findViewById(R.id.layout_daiban);
		mDb_pro = (LinearLayout) findViewById(R.id.db_pro);
		mLc_iv = (ImageView) findViewById(R.id.lc_iv);
		mJt = (ImageView) findViewById(R.id.jt);
		listView = (ListView) findViewById(R.id.pry_listv);

		mFirstTitle = (TextView) findViewById(R.id.first_title);
		mSecondTitle = (TextView) findViewById(R.id.second_title);
		if (intentData.equals("finished")) {
			mFirstTitle.setText("已办详情");
			mSecondTitle.setText("流程节点");
		} else if (intentData.equals("launch")) {
			mFirstTitle.setText("我发起流程详情");
			mSecondTitle.setText("流程节点");
		}
		final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.view_roate);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		mDb_pro.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (listView.getVisibility() == View.VISIBLE) {
					listView.setVisibility(View.GONE);
				} else {
					listView.setVisibility(View.VISIBLE);
					mJt.setAnimation(operatingAnim);
					operatingAnim.start();

				}
			}
		});
		listView.setVisibility(View.VISIBLE);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.head_left_btn) {
			finish();
		}
	}

	/**
	 * 设置待办详情，用于显示原始流程信息
	 */
	// private void setProcessKeyValue() {
	// mLayout_daiban.removeAllViews();
	// LinearLayout mainLayout = new LinearLayout(this);
	// mainLayout.setOrientation(LinearLayout.HORIZONTAL);
	// LinearLayout.LayoutParams mainlp = new
	// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT);
	//
	// for (int i = 0; i < listKeyValue.size(); i++) {
	// // keyValue为空的不显示
	// // if (StringUtil.isNotEmpty(listKeyValue.get(i).keyvalue)) {
	// if (listKeyValue.get(i).keyname.equals("请假原因") ||
	// listKeyValue.get(i).keyname.equals("离职类型") ||
	// listKeyValue.get(i).keyname.equals("身份证") ||
	// listKeyValue.get(i).keyname.equals("离职备注")
	// || listKeyValue.get(i).keyname.equals("拟离职时间")) {// 请假原因另起一行显示
	// mainLayout = new LinearLayout(this);
	// mainLayout.setOrientation(LinearLayout.HORIZONTAL);
	// final TextView tx = new TextView(this);
	// tx.setText(listKeyValue.get(i).keyname + ":  " +
	// listKeyValue.get(i).keyvalue);
	// tx.setGravity(Gravity.CENTER);
	// tx.setTextColor(Color.rgb(48, 48, 48));
	// tx.setGravity(Gravity.LEFT);
	// LinearLayout.LayoutParams lp1 = new
	// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT, 1.0f);
	// lp1.rightMargin = 10;
	// lp1.topMargin=2;
	// lp1.bottomMargin=2;
	// mainLayout.addView(tx, lp1);
	// if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
	// mLayout_daiban.addView(mainLayout, mainlp);
	// }
	// } else {
	// if (mainLayout.getChildCount() < 2) { // 显示一行
	// final TextView tx = new TextView(this);
	// tx.setText(listKeyValue.get(i).keyname + ":  " +
	// listKeyValue.get(i).keyvalue);
	// tx.setGravity(Gravity.CENTER);
	// tx.setTextColor(Color.rgb(48, 48, 48));
	// tx.setGravity(Gravity.LEFT);
	// // tx.setPadding(10, 0, 10, 0);
	// LinearLayout.LayoutParams lp1 = new
	// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT, 1.0f);
	// lp1.rightMargin = 10;
	// lp1.topMargin=2;
	// lp1.bottomMargin=2;
	// mainLayout.addView(tx, lp1);
	// if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
	// mLayout_daiban.addView(mainLayout, mainlp);
	// }
	// } else {
	// mainLayout = new LinearLayout(this);
	// mainLayout.setOrientation(LinearLayout.HORIZONTAL);
	// final TextView tx = new TextView(this);
	// tx.setText(listKeyValue.get(i).keyname + ":  " +
	// listKeyValue.get(i).keyvalue);
	// tx.setGravity(Gravity.CENTER);
	// tx.setTextColor(Color.rgb(48, 48, 48));
	// tx.setGravity(Gravity.LEFT);
	// LinearLayout.LayoutParams lp1 = new
	// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT, 1.0f);
	// lp1.rightMargin = 10;
	// lp1.topMargin=2;
	// lp1.bottomMargin=2;
	// mainLayout.addView(tx, lp1);
	// if (mainLayout.getChildCount() == 2 || i + 1 == listKeyValue.size()) {
	// mLayout_daiban.addView(mainLayout, mainlp);
	// }
	// }
	// }
	// // }
	//
	// }
	// }

	/**
	 * 网络请求,节点
	 */
	private void getFlowTaskList() {
	
		String userid = StringUtil.noNull(SharedPreferencesUtils.getConfigStr(PrYandFDetailActivity.this, BaseActivity.CASH_NAME, "userid"));
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "getFlowTaskList");
		params.put("flowId", StringUtil.noNull(processDataComplete.flowId));
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println("===========11111111===========   " + HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (PrYandFDetailActivity.this == null) {
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
				System.out.println(t.toString());
				if (PrYandFDetailActivity.this == null) {
					return;
				}
				dialog.dismiss();
				Log.v("newland", "PrToDoDetailActivity  " + t.toString());

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
							for (int i = 0; i < listFlow.size(); i++) {
								System.out.println("PrToDoDetailActivity  " + listFlow.get(i).getComment() + "  ========   " + listFlow.get(i).getEndDate() + "  == "
										+ listFlow.get(i).getOperatorName() + "  ==  " + listFlow.get(i).getName());
							}
							adapter = new ProAuditAdapter(PrYandFDetailActivity.this, listFlow);
							listView.setAdapter(adapter);
						}
					} else {
						Toast.makeText(PrYandFDetailActivity.this, jsonInfov3.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				} catch (Exception e) {

				}

			}

		});
	}

}