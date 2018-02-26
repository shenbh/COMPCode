package com.newland.comp.fragment;

import java.util.Dictionary;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.R.string;
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
import com.newland.comp.adapter.my.PayrollOthersAdapter;
import com.newland.comp.bean.Confirm;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.indicator.Marking;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.bean.my.Fault;
import com.newland.comp.bean.my.PayrollNight;
import com.newland.comp.bean.my.PayrollOthers;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 其他信息
 * 
 * @author Administrator
 * 
 */
public class PayrollOthersFragment extends BaseFragment {
	Context context;
	private View mView;
	private ListView listView;
	private ListView faultListView;
	private ListView confirmListView;
	private TextView praiseNumberTextView;
	private TextView compositeValueTextView;
	String month;
	private LoadingDialog dialog;
	private View load_msg;

	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.payroll_others_fragment, container,
				false);
		initView(mView, inflater);
		return mView;
	}

	private void initView(View parent, LayoutInflater inflater) {
		praiseNumberTextView = (TextView) parent
				.findViewById(R.id.tv_praise_number);
		compositeValueTextView = (TextView) parent
				.findViewById(R.id.tv_composite_value);
		listView = (ListView) parent.findViewById(R.id.listView);
		load_msg = parent.findViewById(R.id.load_msg);
		refresh();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void refresh() {
		load_msg.setVisibility(View.VISIBLE);
		String userid = SharedPreferencesUtils.getConfigStr(context,
				BaseActivity.CASH_NAME, "userid");
		String month = ((TextView) getActivity().findViewById(
				R.id.head_right_tv)).getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "payroll_others");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("month", month);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
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
				// if(dialog != null)
				// {
				// dialog.dismiss();
				// }
				if (load_msg != null)
					load_msg.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (load_msg != null)
					load_msg.setVisibility(View.GONE);
				System.out.println(t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(),
							JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {

					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2
							.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2
								.getResultDataToClass(JsonInfo.class);
						PayrollOthers payrollOthers = jsonInfo
								.getData_exp(PayrollOthers.class);
						String praiseNum = payrollOthers.getByls();
						String compositeValue = payrollOthers.getZhkp();
						praiseNumberTextView.setText(StringUtil.noNull(
								praiseNum, "0"));
						compositeValueTextView.setText(StringUtil.noNull(
								compositeValue, "0"));
						// 服务器传回来的用户信息
						List<Fault> faultsList = jsonInfo
								.getDataList(Fault.class);
						List<Confirm> confirmList = jsonInfo
								.getDataList2(Confirm.class);
						PayrollOthersAdapter payrollOthersAdapter = new PayrollOthersAdapter(
								context, faultsList, confirmList);
						listView.setAdapter(payrollOthersAdapter);
					} else {// 显示登陆失败信息
						Toast.makeText(context, jsonInfov2.getResultDesc(),
								1000).show();
					}
				}

			}
		});
	}

	public PayrollOthersFragment(Context context) {
		super();
		this.context = context;
	}

	public static PayrollOthersFragment newInstance(Context context) {
		return new PayrollOthersFragment(context);
	}
}
