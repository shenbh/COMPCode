package com.newland.comp.activity.indicator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.more.TreeBean;
import com.newland.comp.bean.my.IntegralExchangeData;
import com.newland.comp.bean.my.IntegralExchangeExp;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.Options;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 积分兑换详细页面
 * 
 * @author H81
 * 
 */
public class IntegraExchangeDetail_ extends Activity {

	IntegralExchangeData bean;
	IntegralExchangeExp integralExchangeExp; // 活动信息
	private ImageView pic;
	private TextView title;
	private TextView all_integra; // 所需积分
	private TextView all_num; // 总兑换量
	private TextView less_num; // 剩余量
	private EditText dh_num; // 总兑换数
	LoadingDialog dialog;
	private String prompt;// 留言信息
	private EditText evPrompt;// 留言
	private View prompt_layout;// 留言布局

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = Options.getListOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_exchange_detail);
		bindViews();
		initData();
	}

	private void initData() {
		bean = (IntegralExchangeData) getIntent().getSerializableExtra("bean");
		integralExchangeExp = (IntegralExchangeExp) getIntent().getSerializableExtra("integralExchangeExp");
		title.setText(bean.prize_name);
		all_integra.setText(bean.intergral);
		all_num.setText(bean.all_num);
		less_num.setText(bean.less_num);
		imageLoader.displayImage(HttpUtils.URL_PIC_ROOT + StringUtil.noNull(bean.prize_img, ""), pic, options);
		if ("0".equals(bean.isLeaveWord)) { // 不要留言

			prompt_layout.setVisibility(View.GONE);
		}
	}

	private void bindViews() {

		pic = (ImageView) findViewById(R.id.pic);
		title = (TextView) findViewById(R.id.title);
		all_integra = (TextView) findViewById(R.id.all_integra);
		all_num = (TextView) findViewById(R.id.all_num);
		less_num = (TextView) findViewById(R.id.less_num);
		dh_num = (EditText) findViewById(R.id.dh_num);
		evPrompt = (EditText) findViewById(R.id.prompt);
		prompt_layout = findViewById(R.id.prompt_layout);
		TextView title = (TextView) findViewById(R.id.head_center_title);
		title.setText("奖品详情");
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		left_btn.setVisibility(View.VISIBLE);
		left_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IntegraExchangeDetail_.this.finish();
			}
		});

	}

	/**
	 * 提交
	 * 
	 * @param view
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void submit(View view) throws UnsupportedEncodingException {
		if (StringUtil.isEmpty(evPrompt.getText().toString())) {
			Toast.makeText(this, "留言不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		dialog = new LoadingDialog(this);
		dialog.setTvMessage("正在加载...");
		if (StringUtil.isEditTextIsEmpty(dh_num)) {
			Toast.makeText(getApplicationContext(), "请输入兑换数量", 1000).show();
			return;
		}
		int num = Integer.parseInt(StringUtil.noNull(bean.less_num, "0"));
		int editNum = Integer.parseInt(StringUtil.noNull(dh_num.getText().toString(), "0"));
		if (editNum > num) {
			Toast.makeText(getApplicationContext(), "兑换数量大于剩余数量", 1000).show();
			return;
		}
		if (!isFinishing()) {
			dialog.show(true);
		}
		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "integral_exchange_submit");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("prize_id", bean.prize_id);
		params.put("num", URLEncoder.encode(dh_num.getText().toString(), "UTF-8"));
		params.put("prompt", URLEncoder.encode(evPrompt.getText().toString(), "UTF-8")); // 留言
		params.put("activityId", StringUtil.noNull(integralExchangeExp.activityId));

		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				if (IntegraExchangeDetail_.this == null) {
					return;
				}
				dialog.dismiss();
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
			}

			@Override
			public void onSuccess(Object t) {
				if (IntegraExchangeDetail_.this == null) {
					return;
				}
				dialog.dismiss();
				System.out.println(t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						Toast.makeText(getApplicationContext(), "兑换成功", 1000).show();// 显示失败提示
						finish();
					} else {
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
			}
		});
	}

}
