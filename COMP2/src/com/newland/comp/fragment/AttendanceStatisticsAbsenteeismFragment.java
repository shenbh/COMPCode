package com.newland.comp.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyAttendanceFilingAdapter;
import com.newland.comp.adapter.my.MyAttendanceStatisticsAbsentAdapter;
import com.newland.comp.adapter.my.MyAttendanceStatisticsBeLateAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.FilingData;
import com.newland.comp.bean.my.FilingDetailData;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 考勤明细-旷工
 * 
 * @author H81
 * 
 */
public class AttendanceStatisticsAbsenteeismFragment extends BaseFragment implements IXListViewListener {

	private PullRefreshListView pullRefreshListView;
	private int page_index = 1;// 当前页码
//	private int page_row = 4;// 每页显示条数
	private String month;// 月份
	private List<FilingDetailData> list;
	private MyAttendanceStatisticsAbsentAdapter myAttendanceStatisticsAbsentAdapter;
	private View mView;

	Context context;

	private View no_data_layout;// 无数据布局
	LoadingDialog dialog;

	public AttendanceStatisticsAbsenteeismFragment(Context context) {
		this.context = context;
	}

	public static AttendanceStatisticsAbsenteeismFragment newInstance(Context context) {
		return new AttendanceStatisticsAbsenteeismFragment(context);
	}

	
	public void onClick(View v) {
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		dialog = new LoadingDialog(getActivity());
		mView = inflater.inflate(R.layout.my_attendance_statistics_detail_leaveearly, container, false);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		pullRefreshListView = (PullRefreshListView) mView.findViewById(R.id.leaveearly_listview);
		pullRefreshListView.setPullLoadEnable(true);
		pullRefreshListView.setXListViewListener(this);
		page_index = 1;
		getDataFromServer(1);
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
		pullRefreshListView.stopRefresh();
		pullRefreshListView.stopLoadMore();
		String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new Date());
		pullRefreshListView.setRefreshTime(date);
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(int index) {
		dialog.setTvMessage("正在加载...");
		dialog.show(true);
		month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		AjaxParams params = new AjaxParams();
		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		params.put("userid", userid);
		params.put("method", "my_kq_mx");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);// 查询季度
		params.put("page_index", index + "");// 当前页码
		params.put("page_row", ActivityUtil.pageRow);// 每页显示几条
		params.put("type", "absence");
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Log.v("newland", strMsg + t.getMessage());
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				if (no_data_layout != null) {// 显示无数据布局
					no_data_layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onSuccess(Object t) {
				if(getActivity()==null) //说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				Log.v("newland", t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<FilingDetailData> mlist = jsonInfo.getDataList(FilingDetailData.class);
						if (page_index == 1) {// 刷新
							if (mlist != null && mlist.size() > 0) {
								if (no_data_layout != null) {// 无数据布局隐藏
									no_data_layout.setVisibility(View.GONE);
								}
								list = mlist;
								myAttendanceStatisticsAbsentAdapter = new MyAttendanceStatisticsAbsentAdapter(getActivity(), list);
								pullRefreshListView.setAdapter(myAttendanceStatisticsAbsentAdapter);

								if (mlist.size() < Integer.valueOf(ActivityUtil.pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
									pullRefreshListView.setPullLoadEnable(false);
								} else {
									pullRefreshListView.setPullLoadEnable(true);
								}
							} else {
								if (no_data_layout != null) {// 显示无数据布局
									no_data_layout.setVisibility(View.VISIBLE);
								}
								Toast.makeText(getActivity(), "无响应数据", 500).show();
							}
						} else {
							if (mlist != null && mlist.size() > 0) {
								list.addAll(mlist);
								myAttendanceStatisticsAbsentAdapter.notifyDataSetChanged();
								pullRefreshListView.setVisibility(View.VISIBLE);
							} else {
								Toast.makeText(getActivity(), "最后一页了", 500).show();
							}
						}
					} else {
						Toast.makeText(getActivity(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

	@Override
	public void refresh() {
		page_index = 1;
		getDataFromServer(1);
	}

}
