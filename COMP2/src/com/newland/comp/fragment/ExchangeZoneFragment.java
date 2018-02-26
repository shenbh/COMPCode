package com.newland.comp.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.indicator.IndicatorActivity;
import com.newland.comp.activity.indicator.IntegraExchangeDetail_;
import com.newland.comp.adapter.hr.ExchangeIntegralAdapter;
import com.newland.comp.bean.my.IntegralExchangeData;
import com.newland.comp.bean.my.IntegralExchangeExp;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 兑换专区
 * 
 * @author H81
 *
 */
public class ExchangeZoneFragment extends BaseFragment implements IXListViewListener {

	private View mView;
	private TextView mLine_activity;
	private TextView mLine_time;
	private TextView mLine_time_everyday;
	private TextView mStandard_line;
	private TextView mBalance;
	private com.newland.comp.view.PullRefreshListView pullRefreshListView;
	Context context;
	private int page_index = 1;// 当前页码
	LoadingDialog dialog;
	private List<IntegralExchangeData> list;
	private IntegralExchangeExp integralExchangeExp;
	ExchangeIntegralAdapter adapter;

	private View no_data_layout;// 无数据布局

	public ExchangeZoneFragment(Context context) {
		this.context = context;
	}

	
	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_exchange_zone, container, false);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		bindViews();
		refresh();
		return mView;
	}

	/**
	 * 刷新网络
	 */
	@Override
	public void refresh() {
		 dialog = new LoadingDialog(getActivity());
		 dialog.setTvMessage("正在加载...");
		 dialog.show(true);

		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "integral_exchange");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", page_index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow);// 每页显示几条
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				no_data_layout.setVisibility(View.VISIBLE);
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
						integralExchangeExp = jsonInfo.getResultDataToClass(IntegralExchangeExp.class);
						List<IntegralExchangeData> mlist = integralExchangeExp.premiumList;
						
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (no_data_layout != null) // 无数据布局隐藏
								{
									no_data_layout.setVisibility(View.GONE);
								}
								list = mlist;								
								adapter = new ExchangeIntegralAdapter(getActivity(), list);
								pullRefreshListView.setAdapter(adapter);
								addListLinster();
								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
							} else {
								Toast.makeText(getActivity(), "兑换专区无响应数据", 500).show();
								if (no_data_layout != null) // 显示无数据布局
								{
									no_data_layout.setVisibility(View.VISIBLE);
								}
							}

							// 设置exp信息
							if (integralExchangeExp != null) {
								mLine_activity.setText(StringUtil.noNull(integralExchangeExp.activity_name)); // 活动名称
								mLine_time.setText(StringUtil.noNull(integralExchangeExp.activity_time)); // 活动时间
								mLine_time_everyday.setText(StringUtil.noNull(integralExchangeExp.exchange_period)); // 每天兑换时间
								mStandard_line.setText( StringUtil.noNull(integralExchangeExp.integral_line)); // 达标线
								mBalance.setText( StringUtil.noNull(integralExchangeExp.less_integral)); // 余额
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

	/**
	 * 添加listview监听
	 */
	private void addListLinster() {
		pullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > 0) {
					System.out.println("点击" + position);
					Intent intent = new Intent(context, IntegraExchangeDetail_.class);
					intent.putExtra("bean", list.get(position - 1));
					intent.putExtra("integralExchangeExp", integralExchangeExp); // 活动信息
					context.startActivity(intent);
				}
			}
		});
	}

	public static ExchangeZoneFragment newInstance(Context context) {
		return new ExchangeZoneFragment(context);
	}

	private void bindViews() {

		mLine_activity = (TextView) mView.findViewById(R.id.line_activity);
		mLine_time = (TextView) mView.findViewById(R.id.line_time);
		mLine_time_everyday = (TextView) mView.findViewById(R.id.line_time_everyday);
		mStandard_line = (TextView) mView.findViewById(R.id.standard_line);
		mBalance = (TextView) mView.findViewById(R.id.balance);
		pullRefreshListView = (com.newland.comp.view.PullRefreshListView) mView.findViewById(R.id.list_data);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);

	}

	
	public void onListViewRefresh() {
		page_index = 1;
		refresh();
		onLoad();
	}

	
	public void onListViewLoadMore() {
		page_index++;
		refresh();
		onLoad();
	}

	private void onLoad() {
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}

}
