package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.MyStartData;
import com.newland.comp.bean.my.MyStartData2;
import com.newland.comp.bean.my.MyStartExp;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 我的星级
 * 
 * @author H81
 * 
 */
public class MyStartActivity extends BaseActivity {

	private TextView mSpinner_year; // 年选择
	private Spinner mQuarter; // 半年选择
	private TextView mPx_sum; // 评选总分
	private TextView mJf_sum;// 积分总分
	private TextView mRand_sum;// 总分排名
	private TextView mUsername;// 用户名
	private TextView mUserid;// 编号
	private TextView mCsp_no;// csp工号
	private TextView mDq;// 所在区域
	private TextView mMain_skill;// 主技能线
	private TextView mService_sum;// 营销服务得分
	private TextView mStart_remark;//星级备注
	private LinearLayout mStart_layout;// 起评星级
	private String[] period = { "上半年", "下半年" };
	private MyStartExp myStartExp;
	private ArrayList<MyStartData> turningList;// 轮岗信息
	private List<MyStartData2> list;// 星级

	private Button dissent;// 我有异议
	String quarterStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_start);
		bindViews();
		setTitle();
		addLinster();
		reflush();
	}

	private void bindViews() {

		mSpinner_year = (TextView) findViewById(R.id.spinner_year);
		mQuarter = (Spinner) findViewById(R.id.quarter);
		mPx_sum = (TextView) findViewById(R.id.px_sum);
		mJf_sum = (TextView) findViewById(R.id.jf_sum);
		mRand_sum = (TextView) findViewById(R.id.rand_sum);
		mUsername = (TextView) findViewById(R.id.username);
		mUserid = (TextView) findViewById(R.id.userid);
		mCsp_no = (TextView) findViewById(R.id.csp_no);
		mDq = (TextView) findViewById(R.id.dq);
		mMain_skill = (TextView) findViewById(R.id.main_skill);
		mService_sum = (TextView) findViewById(R.id.service_sum);
		mStart_remark=(TextView) findViewById(R.id.start_remark);
		mStart_layout = (LinearLayout) findViewById(R.id.start_layout);
		dissent = (Button) findViewById(R.id.dissent);
	}

	private void setTitle() {
		TextView head_center_title = (TextView) findViewById(R.id.head_center_title);
		head_center_title.setText("我的星级");
		ImageButton head_left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		head_left_btn.setVisibility(View.VISIBLE);
		head_left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				MyStartActivity.this.finish();

			}
		});
	}

	/**
	 * 添加监听
	 */
	private void addLinster() {
		ActivityUtil.showDropDown(this, mQuarter, period, R.layout.simple_spinner_item);
		// DatePicker datePicker=new DatePicker(this);
		//
		// int month=datePicker.getMonth()+1;
		// System.out.println("================================"+month+"  ======11111111111");
		// if (month>6) {
		// mQuarter.setSelection(1);// 默认选择下半年
		// }else {
		mQuarter.setSelection(0);
		// }

		mSpinner_year.setText(StringUtil.getLastYear());
		mSpinner_year.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				new DatePickDialog(MyStartActivity.this, DatePickDialog.YEAR).datePicKDialog(mSpinner_year);

			}
		});
		if (dissent != null) {
			dissent.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					if (isClickable) {
						Intent dissentintent = new Intent(MyStartActivity.this, MysalaryDissentActivity.class);
						dissentintent.putExtra("type", "my_start");
						dissentintent.putExtra("time", mSpinner_year.getText().toString().trim() + "," + (mQuarter.getSelectedItemPosition() + 1));
						startActivity(dissentintent);
					} else {
						Toast.makeText(MyStartActivity.this, "不在异议期内", 1000).show();
					}
				}
			});
		}
	}

	/**
	 * 请求网络
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void reflush() {
		initDialog(this);
		
		// 去除空格
		String str = mSpinner_year.getText().toString();
		str = str.replaceAll(" ", "");
		quarterStr = str + "," + (mQuarter.getSelectedItemPosition() + 1);

		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "my_start");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter", quarterStr);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (MyStartActivity.this==null) {
					return;
				}
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
			}

			@Override
			public void onSuccess(Object t) {
				if (MyStartActivity.this==null) {
					return;
				}
				if (StringUtil.isNotEmpty(quarterStr)) {
					isClickAble(MyStartActivity.this, "half_year", quarterStr, "my_start");
				}
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						myStartExp = jsonInfo.getData_exp(MyStartExp.class);
						list = jsonInfo.getDataList2(MyStartData2.class);
						turningList = (ArrayList<MyStartData>) jsonInfo.getDataList(MyStartData.class);

						if (myStartExp != null && StringUtil.isNotEmpty(myStartExp.user_name)) {
							setExpData();
						} else {
							setExpNoData();
							Toast.makeText(getApplicationContext(), "无查询数据", 1500).show();
						}

						if (list.size() > 0) // 星级列表有数据
						{
							setStartList();
						}

					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});

	}

	/**
	 * 把服务器传回来的数据赋值
	 */
	private void setExpData() {
		mPx_sum.setText(StringUtil.noNull(myStartExp.px_sum)); // 评选总分
		mJf_sum.setText(StringUtil.noNull(myStartExp.zh_score));// 积分总分
		mRand_sum.setText(StringUtil.noNull(myStartExp.total));// 总分排名
		mUsername.setText(StringUtil.noNull(myStartExp.user_name));// 用户名
		mUserid.setText(StringUtil.noNull(myStartExp.user_num));// 编号
		mCsp_no.setText(StringUtil.noNull(myStartExp.csp));// csp工号
		mDq.setText(StringUtil.noNull(myStartExp.dq));// 所在区域
		mMain_skill.setText(StringUtil.noNull(myStartExp.main_skill));// 主技能线
		mService_sum.setText(StringUtil.noNull(myStartExp.service_sum));// 营销服务得分
		mStart_remark.setText(StringUtil.noNull(myStartExp.remark));//星级备注
		System.out.println(myStartExp.service_sum);
	}

	/**
	 * 把服务器传回来的数据赋值
	 */
	private void setExpNoData() {
		mPx_sum.setText("0"); // 评选总分
		mJf_sum.setText("0");// 积分总分
		mRand_sum.setText("0");// 总分排名
		mService_sum.setText("0");// 营销服务得分
		mUsername.setText("");// 用户名
		mUserid.setText("");// 编号
		mCsp_no.setText("");// csp工号
		mDq.setText("");// 所在区域
		mMain_skill.setText("");// 主技能线
	}

	/**
	 * 设置星级列表
	 */
//	private void setStartList() {
//		mStart_layout.removeAllViews();
//		LinearLayout mainLayout = new LinearLayout(this);
//		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
//		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//
//		for (int i = 0; i < list.size(); i++) {
//			if (mainLayout.getChildCount() < 2) { // 显示一行
//				final TextView tx = new TextView(this);
//				tx.setText(list.get(i).xj_name + ":  " + list.get(i).xj_value);
//				tx.setGravity(Gravity.LEFT);
//				tx.setTextColor(Color.rgb(141, 194, 31));
//				// tx.setPadding(10, 0, 10, 0);
//				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//				lp1.weight=1;
//				lp1.rightMargin = 20;
//				mainLayout.addView(tx, lp1);
//				if (mainLayout.getChildCount() == 2 || i + 1 == list.size()) {
//					mStart_layout.addView(mainLayout, mainlp);
//				}
//			} else {
//				mainLayout = new LinearLayout(this);
//				mainLayout.setOrientation(LinearLayout.HORIZONTAL);
//				final TextView tx = new TextView(this);
//				tx.setText(list.get(i).xj_name + ":  " + list.get(i).xj_value);
//				tx.setGravity(Gravity.LEFT);
//				tx.setTextColor(Color.rgb(141, 194, 31));
//				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//				lp1.weight=1;
//				lp1.rightMargin = 20;
//				mainLayout.addView(tx, lp1);
//				if (mainLayout.getChildCount() == 2 || i + 1 == list.size()) {
//					mStart_layout.addView(mainLayout, mainlp);
//				}
//			}
//
//		}
//	}
	private void setStartList() {
		mStart_layout.removeAllViews();
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout childLp;
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp1.weight=1;
		lp1.rightMargin = 20;
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp2.weight=1;
		for (int i = 0; i < list.size(); i++) {
			if (mainLayout.getChildCount() < 2) { // 显示一行
				childLp =new LinearLayout(this);
				childLp.setOrientation(LinearLayout.HORIZONTAL);
				final TextView tx = new TextView(this);
				tx.setText(list.get(i).xj_name + ":  ");
				tx.setGravity(Gravity.LEFT);
				tx.setTextColor(Color.rgb(0,0,0));
				final TextView tx1 = new TextView(this);
				tx1.setText( list.get(i).xj_value);
				tx1.setGravity(Gravity.LEFT);
				tx1.setTextColor(Color.rgb(141, 194, 31));
				childLp.addView(tx,lp2);
				childLp.addView(tx1,lp2);
				mainLayout.addView(childLp, lp1);
				if (mainLayout.getChildCount() == 2 || i + 1 == list.size()) {
					mStart_layout.addView(mainLayout, mainlp);
				}
			} else {
				mainLayout = new LinearLayout(this);
				mainLayout.setOrientation(LinearLayout.HORIZONTAL);
				childLp= new LinearLayout(this);
				childLp.setOrientation(LinearLayout.HORIZONTAL);
				final TextView tx = new TextView(this);
				tx.setText(list.get(i).xj_name + ":  ");
				tx.setGravity(Gravity.LEFT);
				tx.setTextColor(Color.rgb(0,0,0));
				final TextView tx1 = new TextView(this);
				tx1.setText( list.get(i).xj_value);
				tx1.setGravity(Gravity.LEFT);
				tx1.setTextColor(Color.rgb(141, 194, 31));
				childLp.addView(tx,lp2);
				childLp.addView(tx1,lp2);
				mainLayout.addView(childLp, lp1);
				if (mainLayout.getChildCount() == 2 || i + 1 == list.size()) {
					mStart_layout.addView(mainLayout, mainlp);
				}
			}

		}
	}

	/**
	 * 绩效等级
	 * 
	 * @param view
	 */
	public void btn_performance(View view) {
		if (myStartExp != null) {
			Intent intent = new Intent(this, MyStartPerforRankActivity.class);
			intent.putExtra("bean", myStartExp);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "无该周期数据", 1000).show();
		}

	}

	/**
	 * 培训学习
	 * 
	 * @param view
	 */
	public void btn_training(View view) {
		if (myStartExp != null) {
			Intent intent = new Intent(this, MyStartTrainActivity.class);
			intent.putExtra("bean", myStartExp);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "无该周期数据", 1000).show();
		}
	}

	/**
	 * 轮岗信息
	 * 
	 * @param view
	 */
	public void btn_work_shift(View view) {
		if (turningList != null)// 防止访问服务失败时的报错
			if (turningList.size() > 0) {
				Intent intent = new Intent(this, MyStartWorkShiftActivity.class);
				intent.putExtra("list", turningList);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "无该周期数据", 1000).show();
			}
	}

	/**
	 * 知识传承
	 * 
	 * @param view
	 */
	public void btn_knowledge(View view) {
		if (myStartExp != null) {
			Intent intent = new Intent(this, MyStartKnowledgeActivity.class);
			intent.putExtra("bean", myStartExp);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "无该周期数据", 1000).show();
		}
	}

	/**
	 * 查询
	 * 
	 * @param view
	 */
	public void btn_search(View view) {
		reflush();
	}
	
	@Override
	public void setDissentBtnColor(){
		if (isClickable) {
			dissent.setTextColor(dissentColor);
		} else {
			dissent.setTextColor(dissentColor);
		}
	}
	
}
