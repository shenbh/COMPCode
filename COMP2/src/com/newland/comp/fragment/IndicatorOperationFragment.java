package com.newland.comp.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.indicator.IndicatorActivity;
import com.newland.comp.activity.indicator.IndicatorDetailActivity;
import com.newland.comp.adapter.indicator.IndicatorOperationAdapter;
import com.newland.comp.bean.indicator.IndicatorAssResultData;
import com.newland.comp.bean.indicator.IndicatorAssResultDataRelation;
import com.newland.comp.bean.indicator.IndicatorData;
import com.newland.comp.bean.indicator.IndicatorTrendData;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 运营指标
 * 
 * @author H81
 * 
 */
public class IndicatorOperationFragment extends BaseFragment {

	Context context;
	private View mView;
	private ListView listView;
	private IndicatorOperationAdapter adapter;
	List<IndicatorData> list = new ArrayList<IndicatorData>(); // 运营指标列表集合
	List<IndicatorData> listMore = new ArrayList<IndicatorData>(); // 运营指标列表集合更多

	private Handler mHandler = new Handler();
	// private int page = 1; // 当前页
	private int rows = 8;// 每页显示几条
	private String time = ""; // 时间
								// 2015-3-30/2015-03,1(2015年3月第一周)/2015-03/2015,1(2015
								// 年第1季度)
	LoadingDialog myDialog;
	private Button btn_more;

	public IndicatorOperationFragment(Context context) {
		super();
		this.context = context;
	}

	public static IndicatorOperationFragment newInstance(Context context) {
		return new IndicatorOperationFragment(context);
	}

	public void onClick(View arg0) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("IndicatorOperationFragment第一次启动");
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_indicator_operation, container, false);
		initView(mView, inflater);

		return mView;
	}

	private void initView(View parent, LayoutInflater inflater) {
		listView = (ListView) parent.findViewById(R.id.listview);
		btn_more = (Button) parent.findViewById(R.id.btn_more);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= 0) {
					System.out.println("点击" + position);
					Intent intent = new Intent(context, IndicatorDetailActivity.class);
					intent.putExtra("quarter_type", IndicatorActivity.currentDateType);
					intent.putExtra("time", time);
					intent.putExtra("zb_id", list.get(position).zb_id);
					intent.putExtra("zb_name", list.get(position).zb_name);
					context.startActivity(intent);
				}
			}
		});
		btn_more.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				list.addAll(listMore);
				adapter.notifyDataSetChanged();
				view.setVisibility(View.GONE);

			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		refresh();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void refresh() {
		Bundle bundle = getArguments();
		String item_type = null;
		if (bundle != null) {
			item_type = bundle.getString("item_type");
			System.out.println(StringUtil.noNull(item_type));
		}

		myDialog = new LoadingDialog(context);
		myDialog.setTvMessage("正在查询..");
		myDialog.show(true);

		Spinner spinner = (Spinner) getActivity().findViewById(R.id.week);
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "indicator");
		params.put("item_type", item_type);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", IndicatorActivity.currentDateType);
		if (IndicatorActivity.currentDateType.equals(IndicatorActivity.DAY)) // 按天选择
		{
			time = ((TextView) getActivity().findViewById(R.id.zb_time)).getText().toString();
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.WEEK)) {// 按周选择
																						// 2015-03,1
																						// spinner索引默认为0
			time = ((TextView) getActivity().findViewById(R.id.zb_time)).getText().toString() + "," + (spinner.getSelectedItemPosition() + 1);
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.MONTH)) {// 按月
			time = ((TextView) getActivity().findViewById(R.id.zb_time)).getText().toString();
		} else { // 按季度
			time = ((TextView) getActivity().findViewById(R.id.zb_time)).getText().toString() + "," + (spinner.getSelectedItemPosition() + 1);
		}
		time = time.replace(" ", "");
		params.put("time", time);
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
					strMsg = replaceErroStr(strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				Toast.makeText(context, strMsg, 1000).show();
				// if (myDialog != null) {
				myDialog.dismiss();
				// }
			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				System.out.println("indicator---" + t.toString());
				// if (myDialog != null) {
				myDialog.dismiss();
				// }
				JsonInfoV3 jsonInfo = null;
				try {
					jsonInfo = JSON.parseObject(t.toString(), JsonInfoV3.class);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						try {
							List<IndicatorData> mlist = jsonInfo.getResultDataKeyToClass("data", IndicatorData.class);
							if (mlist != null && mlist.size() > 0) {
								btn_more.setVisibility(View.VISIBLE);
								list.clear();
								listMore.clear();
								// 筛选前4个
								for (int i = 0; i < mlist.size(); i++) {
									if (i < 4) {
										list.add(mlist.get(i));
									} else {
										listMore.add(mlist.get(i));
									}
								}
								adapter = new IndicatorOperationAdapter(context, list, time);
								listView.setAdapter(adapter);
							} else {
								Toast.makeText(context, "获取无数据", 1000).show();// 显示登录失败提示
							}
						} catch (Exception e) {
							Toast.makeText(getActivity(), "请求数据有误", 1000).show();
						}
					} else {
						Toast.makeText(context, jsonInfo.getResultDesc(), 1000).show();// 显示登录失败提示
					}
				}
			}
		});
	}
}
