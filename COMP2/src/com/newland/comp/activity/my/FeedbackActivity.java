package com.newland.comp.activity.my;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 反馈意见
 */
public class FeedbackActivity extends BaseActivity {

	private EditText inputText;// 反馈意见内容
	private TextView center_title, count_txt;
	private ImageButton left_btn;// 返回
	private Button feedback_btn;// 提交按钮
	private static final int MAX_COUNT = 300;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		inputText = (EditText) findViewById(R.id.feedback_content);// 输入框
		center_title = (TextView) findViewById(R.id.head_center_title);// 标题
		count_txt = (TextView) findViewById(R.id.feedback_count);// 剩余字数
		left_btn = (ImageButton) findViewById(R.id.head_left_btn);// 返回
		feedback_btn = (Button) findViewById(R.id.feedback_btn);// 提交
		center_title.setText("意见反馈");
		inputText.addTextChangedListener(mTextWatcher);
		inputText.setSelection(inputText.length()); // 将光标移动最后一个字符后面
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		feedback_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (StringUtil.isEmpty(inputText.getText().toString())) {
					Toast.makeText(FeedbackActivity.this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
				} else {
					postFeedBace();
				}
			}
		});
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable s) {
			editStart = inputText.getSelectionStart();
			editEnd = inputText.getSelectionEnd();

			inputText.removeTextChangedListener(mTextWatcher);
			while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			inputText.setText(s);
			inputText.setSelection(editStart);

			inputText.addTextChangedListener(mTextWatcher);

			setLeftCount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

	};

	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	private void setLeftCount() {
		count_txt.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

	/**
	 * 获取用户输入的分享内容字数
	 * 
	 * @return
	 */
	private long getInputCount() {
		return calculateLength(inputText.getText().toString());
	}

	/**
	 * 反馈信息-网络请求
	 */
	@SuppressWarnings("unchecked")
	private void postFeedBace() {
		String userid = SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userid");
		String userpwd = SharedPreferencesUtils.getConfigStr(getApplication(), CASH_NAME, "userpwd");
		AjaxParams params = new AjaxParams();
		String second = StringUtil.getNowTime(StringUtil.SECOND_TIME);// yyyy-MM-dd
																// HH:ss:mm
		params.put("userid", userid);
		params.put("retroaction_content", inputText.getText().toString());
		params.put("retroaction_time", second);
		params.put("retroaction_type", "android");
		params.put("method", "retroaction_info");
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
				if (FeedbackActivity.this==null) {
					return;
				}
			}

			@Override
			public void onSuccess(Object t) {
				if (FeedbackActivity.this==null) {
					return;
				}
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				
				if (jsonInfov2 != null) {
					// 服务器传回来的用户信息
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						Toast.makeText(getApplicationContext(), "提交成功", 1000).show();
						System.out.println("提交成功================" + jsonInfov2.getResultDesc());
						finish();
					}
					 else {// 显示失败信息
							Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();
							System.out.println("提交失败================" + jsonInfov2.getResultDesc());
						}

				}
			}

		});
	}
}
