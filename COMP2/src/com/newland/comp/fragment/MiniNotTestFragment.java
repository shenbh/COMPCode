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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.MyAttendanceStatisticsBeLateAdapter;
import com.newland.comp.adapter.my.minitest.MiniTestListAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.my.FilingDetailData;
import com.newland.comp.bean.my.minitest.MiniTest;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp.view.PullRefreshListView;
import com.newland.comp.view.PullRefreshListView.IXListViewListener;
import com.newland.comp2.R;

/**
 * 未考试
 * 
 * @author Administrator
 * 
 */
public class MiniNotTestFragment extends BaseFragment {
	private ListView listView;
	private List<MiniTest> list;
	private MiniTestListAdapter miniTestListAdapter;
	private View mView;
	Context context;

	String hasCheck;
	private View no_data_layout;// 无数据布局
	LoadingDialog dialog;

	public MiniNotTestFragment(Context context,String hasCheck) {
		this.context = context;
		this.hasCheck=hasCheck;
	}

	public static MiniNotTestFragment newInstance(Context context,String hasCheck) {
		return new MiniNotTestFragment(context,hasCheck);
	}

	public void onClick(View v) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		dialog = new LoadingDialog(getActivity());
		mView = inflater.inflate(R.layout.minitestlist_item, container, false);
		no_data_layout = mView.findViewById(R.id.no_data_layout);
		listView = (ListView) mView.findViewById(R.id.listview);
		refresh();
		return mView;
	}

	/**
	 * 从服务器获取数据
	 * 
	 * userid;//工号 signid: md5(userid+'comp') hasCheck: 未考：N,已考：Y
	 * method：getTrPaperInfo
	 */
	@Override
	public void refresh() {
		dialog.setTvMessage("正在加载...");
		dialog.show(true);
		AjaxParams params = new AjaxParams();
		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		params.put("userid", userid);
		params.put("method", "getTrPaperInfo");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("hasCheck", hasCheck);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
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
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				dialog.dismiss();
				System.out.println("getTrPaperInfo--"+t.toString());

				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						List<MiniTest> mlist = jsonInfo.getDataList(MiniTest.class);
						if (mlist != null && mlist.size() > 0) {
							if (no_data_layout != null) {// 无数据布局隐藏
								no_data_layout.setVisibility(View.GONE);
							}
							list = mlist;
							miniTestListAdapter = new MiniTestListAdapter(getActivity(), list);
							listView.setAdapter(miniTestListAdapter);
						} else {
							if (no_data_layout != null) {// 显示无数据布局
								no_data_layout.setVisibility(View.VISIBLE);
							}
							Toast.makeText(getActivity(), "无响应数据", 500).show();
						}
					} else {
						Toast.makeText(getActivity(), jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}

}
