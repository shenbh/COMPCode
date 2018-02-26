package com.newland.comp.activity.my;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.Base64Test;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 我有异议
 * 
 * @author H81
 * 
 */
public class MysalaryDissentActivity extends BaseActivity {
	// private TextView mDissent_month;// 月份
	private String time = "2015-04";
	private EditText mDissent_content;// 异议内容
	private Button mDissent_commit;// 提交
	private TextView next_userid;

	// 时间选择
	private TextView mSpinner_year;
	private Spinner mQuarter; // 时间周期
	// private Button mBtn_user_selected;
	private String[] quarter = { "第一季度", "第二季度", "第三季度", "第四季度" };
	private String[] halfyear = { "上半年", "下半年" };
	private Spinner spinner_halfyear; // 上下半年

	String type;// 接收传过来的类型
	private View next_user_layout; // 选择下一节点人布局
	private LoadingDialog dialog;
	private String nextUserid; // 下一个节点用户id

	private int inputNum = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mysalary_dissent);
		type = getIntent().getStringExtra("type");
		time = getIntent().getStringExtra("time");
		System.out.println("gettype---" + type);
		System.out.println("time---" + time);
		bindViews();
		setTitle();
	}

	private void bindViews() {
		String[] str = time.split(",");// 接收年份和季度

		mDissent_content = (EditText) findViewById(R.id.dissent_content);
		mDissent_commit = (Button) findViewById(R.id.dissent_commit);

		next_userid = (TextView) findViewById(R.id.next_userid);
		spinner_halfyear = (Spinner) findViewById(R.id.half_year);
		mSpinner_year = (TextView) findViewById(R.id.spinner_year);
		mSpinner_year.setClickable(false);
		mQuarter = (Spinner) findViewById(R.id.quarter);
		mQuarter.setClickable(false);
		next_user_layout = findViewById(R.id.next_user_layout);
//		ActivityUtil.showDropDown(this, mQuarter, quarter, R.layout.simple_spinner_item);
//		ActivityUtil.showDropDown(this, spinner_halfyear, halfyear, R.layout.simple_spinner_item);

		if ("my_payroll".equals(type))// 我的薪酬传来的
		{
			mSpinner_year.setText(StringUtil.noNull(time));
			mQuarter.setVisibility(View.GONE);
			spinner_halfyear.setVisibility(View.GONE);
		}
		if ("my_effect".equals(type))// 我的绩效传来的
		{
			mSpinner_year.setText(StringUtil.noNull(time));
			mQuarter.setVisibility(View.GONE);
			spinner_halfyear.setVisibility(View.GONE);
		}
		if ("my_integral".equals(type))// 我的积分传来的
		{
			mSpinner_year.setText(StringUtil.noNull(str[0]));
			mQuarter.setVisibility(View.VISIBLE);
			spinner_halfyear.setVisibility(View.GONE);
			setDefaultQuarter(str[1]);
			next_user_layout.setVisibility(View.GONE); // 不需要选择下一个节点人
		}
		if ("my_start".equals(type))// 我的星级传来的
		{
			mSpinner_year.setText(StringUtil.noNull(str[0]));
			mQuarter.setVisibility(View.GONE);
			spinner_halfyear.setVisibility(View.VISIBLE);
			setDefaultHalfYear(str[1]);
			next_user_layout.setVisibility(View.GONE); // 不需要选择下一个节点人
		}
		if ("my_kq_jb".equals(type))// 我的考勤传来的
		{
			mSpinner_year.setText(StringUtil.noNull(time));
			mQuarter.setVisibility(View.GONE);
			spinner_halfyear.setVisibility(View.GONE);
		}

//		mSpinner_year.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				if ("my_payroll".equals(type))// 我的薪酬传来的
//				{
//					new DatePickDialog(MysalaryDissentActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(mSpinner_year);
//				}
//				if ("my_effect".equals(type))// 我的绩效传来的
//				{
//					new DatePickDialog(MysalaryDissentActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(mSpinner_year);
//				}
//				if ("my_integral".equals(type))// 我的积分传来的
//				{
//					new DatePickDialog(MysalaryDissentActivity.this, DatePickDialog.YEAR).datePicKDialog(mSpinner_year);
//				}
//				if ("my_start".equals(type))// 我的星级传来的
//				{
//					new DatePickDialog(MysalaryDissentActivity.this, DatePickDialog.YEAR).datePicKDialog(mSpinner_year);
//				}
//				if ("my_kq_jb".equals(type))// 我的考勤传来的
//				{
//					new DatePickDialog(MysalaryDissentActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(mSpinner_year);
//				}
//			}
//		});

		mDissent_commit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (StringUtil.isNotEmpty(time) && StringUtil.isNotEmpty(mDissent_content.getText().toString())) {
					reflush();
				} else {
					Toast.makeText(getApplicationContext(), "日期或内容为空", 1000).show();
				}
			}
		});
		// 限制输入的个数
		mDissent_content.addTextChangedListener(new TextWatcher() {
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
				selectionStart = mDissent_content.getSelectionStart();
				selectionEnd = mDissent_content.getSelectionEnd();
				// System.out.println("start="+selectionStart+",end="+selectionEnd);
				if (temp.length() > inputNum) {
					Toast.makeText(getApplicationContext(), "字数不能超过" + inputNum + "个字", Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					mDissent_content.setText(s);
					mDissent_content.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});
	}

	/**
	 * 设置默认的上、下半年
	 * 
	 * @param string
	 */
	private void setDefaultHalfYear(String string) {
		if (string.equals("1")) {
			spinner_halfyear.setSelection(0);
		} else if (string.equals("2")) {
			spinner_halfyear.setSelection(1);
		}
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
			center_tv.setText("我有异议");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					MysalaryDissentActivity.this.finish();
				}
			});
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置默认季度
	 */
	private void setDefaultQuarter(String s) {
		// String[] arr = nows.split("-"); //把月份切割
		if (s.equals("1")) {
			mQuarter.setSelection(0);
		} else if (s.equals("2")) {
			mQuarter.setSelection(1);
		} else if (s.equals("3")) {
			mQuarter.setSelection(2);
		} else if (s.equals("4")) {
			mQuarter.setSelection(3);
		}
	}

	/**
	 * 网络请求数据
	 */
	@Override
	public void reflush() {

		if (next_user_layout.getVisibility() == View.VISIBLE) { // 下一节点人必填
			if ("".equals(next_userid.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "下一节点人必须选择", 1000).show();// 显示登录失败提示
				return;
			}
		}
		if ("".equals(mDissent_content.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "异议内容必填", 1000).show();
			return;
		}
		dialog = new LoadingDialog(this);
		if (!isFinishing()) {dialog.show(true);}

		AjaxParams params = new AjaxParams();
		params.put("userid", SharedPreferencesUtils.getConfigStr(getApplicationContext(), CASH_NAME, "userid"));
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid")));
		params.put("method", "dissent");
		

		if ("my_payroll".equals(type))// 我的薪酬传来的
		{
			params.put("type", "my_payroll");
			params.put("quarter_type", "month");// 月份
			time=mSpinner_year.getText().toString();
		}
		if ("my_effect".equals(type))// 我的绩效传来的
		{
			params.put("type", "my_effect");
			params.put("quarter_type", "month");// 月份
			time=mSpinner_year.getText().toString();
		}
		if ("my_integral".equals(type))// 我的积分传来的
		{
			params.put("type", "my_integral");
			params.put("quarter_type", "quarte");// 月份
			time=mSpinner_year.getText().toString();
			time=time+",";
			int selectIndex=mQuarter.getSelectedItemPosition()+1;
			time=time+selectIndex+"";
		}
		if ("my_start".equals(type))// 我的星级传来的
		{
			params.put("type", "my_start");
			params.put("quarter_type", "half_year");// 半年
			time=mSpinner_year.getText().toString();
			time=time+",";
			int selectIndex=spinner_halfyear.getSelectedItemPosition()+1;
			time=time+selectIndex+"";
		}
		if ("my_kq_jb".equals(type))// 我的考勤传来的
		{
			params.put("type", "my_kq_jb");
			params.put("quarter_type", "month");// 月份
			time=mSpinner_year.getText().toString();
			
		}
		params.put("next_userid", StringUtil.noNull(nextUserid));
		params.put("time", time);// 月份
		// 异议内容
		try {
			params.put("content", URLEncoder.encode(mDissent_content.getText().toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println(HttpUtils.URL + params.toString());

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (MysalaryDissentActivity.this==null) {
					return;
				}
				dialog.dismiss();
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();// 显示登录失败提示
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (MysalaryDissentActivity.this==null) {
					return;
				}
				dialog.dismiss();
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
						Toast.makeText(getApplicationContext(), "提交成功", 1000).show();
						MysalaryDissentActivity.this.finish();
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});

	}

	/**
	 * 选择下一个节点人
	 * 
	 * @param view
	 */
	public void btn_next_userid_layout(View view) {
		Intent intent = new Intent(this, MyPersonListActivity.class);
		intent.putExtra("type", type);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {

			if (requestCode == 1) { // 选择人
				System.out.println("data:" + data.getStringExtra("userid"));
				nextUserid = StringUtil.noNull(data.getStringExtra("userid"));
				next_userid.setText(StringUtil.noNull(data.getStringExtra("username")));
			}
		}

	}

}
