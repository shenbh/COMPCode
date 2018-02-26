package com.newland.comp.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.hr.SecondKillAdapter;
import com.newland.comp.bean.my.IntegralSeckillData;
import com.newland.comp.bean.my.IntegralSeckillExp;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 积分商城之积分秒杀
 * 
 * @author H81
 * 
 *         2015年5月12日 下午12:03:05
 * @version 1.0.0
 */
public class SecondKillFragment extends BaseFragment implements IXListViewListener {
	final int TIME_COUNT = 1;

	private TextView mLine_activity;
	private TextView mLine_time;
	private TextView mLine_time_everyday;
	private TextView mExchangeable;

	private com.newland.comp.view.PullRefreshListView mList_data;
	private int page_index = 1;// 当前页码
	private String month;// 月份
	private List<IntegralSeckillData> list;
	private IntegralSeckillExp integralSeckillExp;

	private View mView;
	private View no_data_layout;// 无数据布局
	LoadingDialog dialog;

	Context context;
	private SecondKillAdapter adapter;
	private String userid;

	private String sysdate;// 服务器时间
	private long sysdate2Long;// 服务器时间
	private long activityStartTime;// 活动开始时间
	private long activityEndTime;// 活动结束时间
	public boolean isSeckill = false;// 是否可以秒杀

	TextView mCountTime;

	public SecondKillFragment(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public static SecondKillFragment newInstance(Context context) {
		return new SecondKillFragment(context);
	}

	public void onClick(View v) {
	}

	private void bindViews() {

		mLine_activity = (TextView) mView.findViewById(R.id.line_activity);// 秒杀活动名称
		mLine_time = (TextView) mView.findViewById(R.id.line_time);// 活动时间+下一场开始时间
		mLine_time_everyday = (TextView) mView.findViewById(R.id.line_time_everyday);// 活动说明
		mExchangeable = (TextView) mView.findViewById(R.id.exchangeable);// 积分余额
		mList_data = (com.newland.comp.view.PullRefreshListView) mView.findViewById(R.id.list_data);
		mCountTime = (TextView) mView.findViewById(R.id.counttime);
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_secondkill, container, false);
		mList_data = (PullRefreshListView) mView.findViewById(R.id.list_data);
		mList_data.setPullLoadEnable(true);
		mList_data.setXListViewListener(this);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		bindViews();
		page_index = 1;
		refresh();
		return mView;
	}

	public void onListViewRefresh() {
		page_index = 1;
		getDataFromServer(page_index);

		onLoad();
	}

	public void onListViewLoadMore() {
		page_index++;
		getDataFromServer(page_index);
		onLoad();
	}

	private void onLoad() {
		mList_data.stopRefresh();
		mList_data.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		mList_data.setRefreshTime(date);
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(int index) {
		dialog = new LoadingDialog(context);
		dialog.setTvMessage("正在加载...");
		dialog.show(true);

		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		AjaxParams params = new AjaxParams();
		userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		params.put("userid", userid);
		params.put("method", "integral_seckill");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow);// 每页显示几条
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (getActivity() == null) {// 说明上层activity 已经被销毁了
					System.out.println("getActivity() 拦截");
					return;
				}
				if (StringUtil.isNotEmpty(strMsg)) {
					strMsg = replaceErroStr(strMsg);
				}
				super.onFailure(t, errorNo, strMsg);
				no_data_layout.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) {// 说明上层activity 已经被销毁了
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				System.out.println("integral_seckill:"+t.toString());
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						List<IntegralSeckillData> mlist = jsonInfo.getResultDataToClass(IntegralSeckillExp.class).getPremiumList();

						if (page_index == 1) {// 刷新
							integralSeckillExp = jsonInfo.getResultDataToClass(IntegralSeckillExp.class);
							// 设置exp信息
							if (integralSeckillExp != null) {
								mLine_activity.setText(StringUtil.noNull(integralSeckillExp.activity_name)); // 活动名称
								String seckillTime = StringUtil.noNull(integralSeckillExp.seckill_time);
								mLine_time_everyday.setText(StringUtil.noNull(integralSeckillExp.activity_explain)); // 活动说明
								mExchangeable.setText("已抽次数："+"剩余次数："+"可用积分：" + StringUtil.noNull(integralSeckillExp.less_integral));
								if (mlist != null && mlist.size() > 0) {
									list = mlist;
									String s=integralSeckillExp.next_time;
									getServiceDate(seckillTime, integralSeckillExp.next_time, list, integralSeckillExp);
									// TODO

									if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
										mList_data.setPullLoadEnable(false);
									} else {
										mList_data.setPullLoadEnable(true);
									}

									if (no_data_layout != null) // 无数据布局隐藏
									{
										no_data_layout.setVisibility(View.GONE);
									}
								} else {
									Toast.makeText(getActivity(), "积分秒杀无响应数据", 500).show();
									if (no_data_layout != null) // 显示无数据布局
									{
										no_data_layout.setVisibility(View.VISIBLE);
									}
								}
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								list.addAll(mlist);
								adapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getActivity(), "最后一页了", 500).show();
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

	@Override
	public void refresh() {
		getDataFromServer(1);
	}

	/**
	 * 获取服务器日期
	 * 
	 * @param list2
	 * @param integralSeckillExp2
	 */
	private void getServiceDate(final String seckillTime, final String nextTime, final List<IntegralSeckillData> list2, final IntegralSeckillExp integralSeckillExp2) {
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("method", "getSysdate");
		System.out.println(HttpUtils.URL + "?" + params);
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				// t.toString():{"resultCode":"000000","resultData":{"sysdate":"2015-10-12 15:14:47"},"resultDesc":""}
				System.out.println("getSysdate:"+t.toString());
				JsonInfoV3 jsonInfov3 = null;
				try {
					jsonInfov3 = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov3 != null) {
					if (JsonInfoV3.SUCCESS_CODE.equals(jsonInfov3.getResultCode())) {
						JSONObject jO = jsonInfov3.getResultDataToJsonObject();
						sysdate = jO.getString("sysdate");
						System.out.println(sysdate);
						// mCountdown.setText(sysdate);
						sysdate2Long = StringUtil.getLongTime(sysdate,StringUtil.SECOND_TIME);
						startTimeAndEndTime2Long(seckillTime, sysdate2Long, nextTime, list2, integralSeckillExp2);

					} else {
						System.out.println("获取服务器时间响应失败");
					}
				}
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}
		});

	}

	/**
	 * 获取活动开始时间和结束时间，并转化为毫秒值
	 * 
	 * @param seckillTime
	 * @param integralSeckillExp2
	 * @param list2
	 */
	private void startTimeAndEndTime2Long(String seckillTime, long sysdate2Long, String nextTime, List<IntegralSeckillData> list2, IntegralSeckillExp integralSeckillExp2) {
		// seckillTime：2015-10-09 18:30:00 到 2015-12-20 15:45:00
		mLine_time.setText(seckillTime); // 活动时间
		String[] seckillTimes = seckillTime.split("到");
		int index = 0, index1 = 0, index2 = 0;
		index = seckillTimes[0].indexOf(" ", 0);
		index1 = seckillTimes[0].indexOf(" ", index + 1);
		if (index1 != -1) {
			seckillTimes[0] = seckillTimes[0].substring(0, index1);
			activityStartTime = StringUtil.getLongTime(seckillTimes[0],StringUtil.SECOND_TIME);

		} else {
			activityStartTime = StringUtil.getLongTime(seckillTimes[0],StringUtil.SECOND_TIME);
		}
		index2 = seckillTimes[1].indexOf(" ", 0);
		if (index2 != -1) {
			seckillTimes[1] = seckillTimes[1].substring(index2 + 1, seckillTimes[1].length());
			activityEndTime = StringUtil.getLongTime(seckillTimes[1],StringUtil.SECOND_TIME);
		} else {
			activityEndTime = StringUtil.getLongTime(seckillTimes[1],StringUtil.SECOND_TIME);
		}
		if (sysdate2Long >= activityStartTime && sysdate2Long <= activityEndTime) {// 在活动中
			mCountTime.setText("下一场时间：" + nextTime);
			isSeckill = true;
		} else if (sysdate2Long < activityStartTime) {
			isSeckill = false;
			String s= nextTime;
			new MyCount( activityStartTime-sysdate2Long, 1000L, nextTime, list2, integralSeckillExp2).start();
		}
		adapter = new SecondKillAdapter(getActivity(), list2, integralSeckillExp2, isSeckill);
		mList_data.setAdapter(adapter);
	}

	/**
	 * 传入毫秒值转成天时分秒
	 */
	private String formatLongToTimeStr(Long l) {
		int hour = 0, minute = 0, second = 0, day = 0;
		second = l.intValue() / 1000;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		if (hour > 24) {
			day = hour / 24;
			hour = hour % 24;
		}
		String str = (getTwoLength(day) + "天" + getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
		return str;
	}

	private String getTwoLength(final int data) {
		if (data < 10) {
			return "0" + data;
		} else {
			return "" + data;
		}
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		String nextTime;
		List<IntegralSeckillData> list2;
		IntegralSeckillExp integralSeckillExp2;

		public MyCount(long millisInFuture, long countDownInterval, String nextTime, List<IntegralSeckillData> list2, IntegralSeckillExp integralSeckillExp2) {
			super(millisInFuture, countDownInterval);
			this.nextTime = nextTime;
			this.list2 = list2;
			this.integralSeckillExp2 = integralSeckillExp2;
		}

		@Override
		public void onFinish() {
			mCountTime.setText("下一场时间：" + nextTime);
			isSeckill = true;
			adapter = new SecondKillAdapter(getActivity(), list2, integralSeckillExp2, isSeckill);
			mList_data.setAdapter(adapter);
			System.out.println("-----nextTime-------" + nextTime);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			isSeckill = false;
			mCountTime.setText("倒计时：" + formatLongToTimeStr(millisUntilFinished));
		}
	}
}
