package com.newland.comp.fragment;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.hr.ExchangeIntegralAdapter;
import com.newland.comp.bean.my.IntegralExchangeData;
import com.newland.comp.bean.my.IntegralExchangeExp;
import com.newland.comp.bean.my.IntegralLucky;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 积分抽奖
 * 
 * @author H81
 *
 */
@SuppressLint("NewApi")
public class LotteryFragment extends BaseFragment {

	private View mView;
	Context context;
	private List<IntegralExchangeData> list;
	private IntegralExchangeExp integralExchangeExp;
	ExchangeIntegralAdapter adapter;

	private TextView mLine_activity;
	private View egg_layout; // 蛋图片
	private TextView mLine_time;
	private TextView mLess_num;
	private TextView mStandard_line;
	private TextView mPrize_result;
	private Button btn_boom;
	private IntegralLucky bean;
	// private String[] mPrize_results; //抽奖结果数组
	// private int boomNum = 0; //砸蛋次数
	private Handler mHandler;
//	private View load_msg; // 加载布局
	private LoadingDialog dialog;

	private View no_data_layout;// 无数据布局

	public LotteryFragment(Context context) {
		this.context = context;
	}

	
	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_skill_zone, container, false);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		bindViews();
		initHandle();
		refresh();
		return mView;
	}

	private void initHandle() {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1: // 让按钮变可见
					mPrize_result.setVisibility(View.GONE);
					egg_layout.setBackground(getResources().getDrawable(R.drawable.egg_close));
					btn_boom.setEnabled(true);
					btn_boom.setText("开始抽奖");

					break;

				}
			}
		};

	}

	/**
	 * 刷新网络
	 */
	@Override
	public void refresh() {
		dialog = new LoadingDialog(context);
		dialog.setTvMessage("正在加载...");
		dialog.show(true);
		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "integral_lucky");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				Log.v("newland", strMsg + t.getMessage());
				no_data_layout.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				Log.v("newland", t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						bean = jsonInfo.getResultDataToClass(IntegralLucky.class);

						if (bean != null) {
							mLine_activity.setText(StringUtil.noNull(bean.activity_name));
							mLine_time.setText(StringUtil.noNull(bean.activity_time));
							mLess_num.setText( StringUtil.noNull(bean.less_integral));
							mStandard_line.setText(  StringUtil.noNull(bean.line));
							// mPrize_results =
							// StringUtil.noNull(bean.prize_name).split(",");
							// boomNum = 0;
							if (no_data_layout != null) // 无数据布局隐藏
							{
								no_data_layout.setVisibility(View.GONE);
							}
						} else {
							Toast.makeText(context, "积分抽奖无数据", 1000).show();// 显示登录失败提示
							if (no_data_layout != null) // 显示无数据布局
							{
								no_data_layout.setVisibility(View.VISIBLE);
							}
						}

					} else {
						if (no_data_layout != null) // 显示无数据布局
						{
							no_data_layout.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});

	}

	public static LotteryFragment newInstance(Context context) {
		return new LotteryFragment(context);
	}

	private void bindViews() {

		mLine_activity = (TextView) mView.findViewById(R.id.line_activity);
		mLine_time = (TextView) mView.findViewById(R.id.line_time);
		mLess_num = (TextView) mView.findViewById(R.id.less_num);
		mStandard_line = (TextView) mView.findViewById(R.id.standard_line);
		mPrize_result = (TextView) mView.findViewById(R.id.prize_result);
		btn_boom = (Button) mView.findViewById(R.id.btn_boom);
		egg_layout = mView.findViewById(R.id.egg_layout);
		// 砸蛋
		btn_boom.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			
			public void onClick(View arg0) {
				// if(mPrize_results.length ==0)
				// {
				// Toast.makeText(context, "活动还没开始", 1000).show();
				// return;
				//
				// }
				// if(boomNum < 5)//只有5次机会
				// {
				// mPrize_result.setVisibility(View.VISIBLE);
				// mPrize_result.setText(mPrize_results[boomNum]);
				// egg_layout.setBackground(getResources().getDrawable(R.drawable.egg_open));
				// boomNum++;
				// btn_boom.setEnabled(false);
				// btn_boom.setText("还有"+(5-boomNum)+"次机会");
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// Thread.sleep(2000);
				// mHandler.sendEmptyMessage(1); //刷新系统消息数
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				//
				//
				// }
				// }).start();
				//
				//
				// }
				// else {
				// Toast.makeText(context, "您已经用完抽奖机会", 1000).show();
				// }

				getPrizeInfo();

			}
		});
	}

	/**
	 * 砸蛋信息
	 */
	private void getPrizeInfo() {

		if (bean == null) // 活动信息的bean
		{
			Toast.makeText(context, "活动初始化参数未获取，请重新进入", 1000).show();
			return;
		}
		dialog = new LoadingDialog(context);
		dialog.show(true);
		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "lotto_submit");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("lotteryId", bean.lotteryId);
		params.put("activityId", bean.activityId);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
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
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				Log.v("newland", t.toString());
				dialog.dismiss();

				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						String prizeInfo = jsonInfo.getResultDataToJsonObject().getString("message");
						mPrize_result.setVisibility(View.VISIBLE);
						mPrize_result.setText(StringUtil.noNull(prizeInfo));
						egg_layout.setBackground(getResources().getDrawable(R.drawable.egg_open));
						btn_boom.setEnabled(false);
						btn_boom.setText("请稍等...");
						new Thread(new Runnable() {
							
							public void run() {
								try {
									Thread.sleep(2000);
									mHandler.sendEmptyMessage(1); // 刷新系统消息数
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
					} else {
						// Toast.makeText(context, jsonInfo.getResultDesc(),
						// 1500).show();// 显示登录失败提示
					}
				}
			}
		});
	}

}
