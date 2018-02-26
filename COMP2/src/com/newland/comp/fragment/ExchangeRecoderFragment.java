package com.newland.comp.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.indicator.IndicatorActivity;
import com.newland.comp.adapter.hr.ExchangeRecoderAdapter;
import com.newland.comp.adapter.my.MyAttendanceStatisticsBeLateAdapter;
import com.newland.comp.bean.hr.ExchangeRecord;
import com.newland.comp.bean.my.FilingDetailData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 兑换记录Fragment
 * 
 * @author H81
 *
 *         2015年5月12日 上午9:30:00
 * @version 1.0.0
 */
public class ExchangeRecoderFragment extends BaseFragment implements IXListViewListener {

	private PullRefreshListView pullRefreshListView;
	private int page_index = 1;// 当前页码
	// private int page_row = 4;// 每页显示条数
	private String month;// 月份
	private List<ExchangeRecord> list;
	private ExchangeRecoderAdapter adapter;
	private View mView;
	private String[] type = { "所有兑换记录", "未评价记录", "已撤销记录" };
	Context context;
//	private TextView mDate_start; // 开始时间
//	private TextView mDate_end; // 结束时间
	private Spinner mType_spinner; // 选择分类
	private Button search_btn;

	private View no_data_layout;// 无数据布局

	LoadingDialog dialog;
	public ExchangeRecoderFragment(Context context) {
		this.context = context;

	}

	public static ExchangeRecoderFragment newInstance(Context context) {
		return new ExchangeRecoderFragment(context);
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.exchangerecoder, container, false);
		pullRefreshListView = (PullRefreshListView) mView.findViewById(R.id.listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		dialog=new LoadingDialog(context);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		page_index = 1;
		bindViews();
		return mView;
	}

	private void bindViews() {
		search_btn = (Button) mView.findViewById(R.id.search_btn);
		mType_spinner = (Spinner) mView.findViewById(R.id.type_spinner);
		ActivityUtil.showDropDown(context, mType_spinner, type, R.layout.simple_spinner_item);
		search_btn.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				page_index = 1;
				getDataFromServer(page_index);
			}
		});

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
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(int index) {
		dialog=new LoadingDialog(context);
		dialog.setTvMessage("正在加载...");
		dialog.show(true);
		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		AjaxParams params = new AjaxParams();
		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		params.put("userid", userid);
		params.put("method", "exchange_record");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("page_index", index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow);// 每页显示几条
		params.put("start_time", "");// 起始时间
		params.put("end_time", "");//
		params.put("search_type", mType_spinner.getSelectedItemPosition() + "");// 0(所有兑换记录)
																				// 1（未评价记录）2（已撤销记录）

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
						List<ExchangeRecord> mlist = jsonInfo.getResultDataKeyToClass("consumeList", ExchangeRecord.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								list = mlist;
								adapter = new ExchangeRecoderAdapter(getActivity(), list, ExchangeRecoderFragment.this);
								pullRefreshListView.setAdapter(adapter);

								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
								if (no_data_layout != null) // 无数据布局隐藏
								{
									no_data_layout.setVisibility(View.GONE);
								}
							} else {
								if (no_data_layout != null) // 显示无数据布局
								{
									no_data_layout.setVisibility(View.VISIBLE);
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
						Toast.makeText(getActivity(), jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
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

	
	public void onClick(View view) {

	}
	public void clicktypespinner(View v){
		mType_spinner.performClick();
	}
}
