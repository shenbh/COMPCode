package com.newland.comp.fragment;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.my.CheckingAdapter;
import com.newland.comp.adapter.my.MarkingWorkloadAdapter;
import com.newland.comp.adapter.my.PayrollNightAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.indicator.Marking;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.PayrollNight;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 夜班时长
 * 
 * @author Administrator
 *
 */
public class PayrollNightFragment extends BaseFragment {
	Context context;
	private View mView;
	private ListView listView;
	String month;
	private PayrollNightAdapter adapter;
	private View no_data_layout;
	private LoadingDialog dialog;
	private View load_msg;
	
	
	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.listview_data, container, false);
		initView(mView, inflater);
		return mView;
	}

	private void initView(View parent, LayoutInflater inflater) {
		listView = (ListView) parent.findViewById(R.id.list_data);
		no_data_layout = parent.findViewById(R.id.no_data_layout);
		Bundle bundle = getArguments();
		if (bundle != null) {
			month = bundle.getString("day");
			System.out.println(StringUtil.noNull(month));
		}
		load_msg = parent.findViewById(R.id.load_msg);
		refresh();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void refresh() {
		load_msg.setVisibility(View.VISIBLE);	 
		 
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		String month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "payroll_night");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				Toast.makeText(context, strMsg, 1000).show();
				if(load_msg != null)
				{
					load_msg.setVisibility(View.GONE);
				}
				no_data_layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(Object t) {
				if(getActivity()==null) //说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				System.out.println(t.toString());
				if(load_msg != null)
				{
					load_msg.setVisibility(View.GONE);
				}
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				
				if (jsonInfov2 != null) {
					// dialog.dismiss();
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo =   jsonInfov2.getResultDataToClass(JsonInfo.class);
						// 服务器传回来的用户信息
						List<PayrollNight> list = jsonInfo.getDataList(PayrollNight.class);
						if (list != null) {
							if(no_data_layout != null)
							{
								no_data_layout.setVisibility(View.GONE);
							}
							adapter = new PayrollNightAdapter(context, list);
							listView.setAdapter(adapter);
						}
						if (list == null || list.size() == 0) {
//							Toast.makeText(context, "当月无数据", 1000).show();
							if(no_data_layout != null) //显示无数据布局
							{
								no_data_layout.setVisibility(View.VISIBLE);
							}
						}
					} else {// 显示登陆失败信息
						Toast.makeText(context, jsonInfov2.getResultDesc(), 1000).show();
					}
				}

			}
		});
	}

	public PayrollNightFragment(Context context) {
		super();
		this.context = context;
	}

	public static PayrollNightFragment newInstance(Context context) {
		return new PayrollNightFragment(context);
	}
}
