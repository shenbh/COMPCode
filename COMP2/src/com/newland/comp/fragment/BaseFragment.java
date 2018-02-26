package com.newland.comp.fragment;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	LoadingDialog dialog;
	protected FragmentActivity fragmentActivity = null;
	public int page = 1; // 当前页

	public boolean isClickable = false;
	public int dissentColor;// 我有异议字体颜色

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentActivity = getActivity();
		dissentColor = getResources().getColor(R.color.app_text_gray2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = createAndInitView(inflater, container, savedInstanceState);
		return view;
	}

	protected abstract View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	public abstract void refresh();

	/**
	 * 是否在异议期内
	 */
	public void isClickAble(String quarteType, String time, String type) {
		dialog = new LoadingDialog(getActivity());
		dialog.setTvMessage("正在加载...");
		dialog.show(true);

		String userid = SharedPreferencesUtils.getConfigStr(getActivity(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", quarteType);
		params.put("time", time);
		params.put("method", "dissent_auth");
		params.put("type", type);
		System.out.println(HttpUtils.URL + "?" + params.toString());
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);
		fh.post(HttpUtils.URL, params, new AjaxCallBack<Object>() {

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (dialog != null) {
					dialog.dismiss();
				}
				JsonInfoV2 jsonInfov2 = null;
				System.out.println("dissent_auth--" + t.toString());
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getActivity(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						isClickable = true;
						dissentColor = getResources().getColor(R.color.app_black);
						setDissentBtnColor();
					} else {
						// Toast.makeText(getActivity(),
						// jsonInfov2.getResultDesc(), 1000).show();// 显示登录失败提示
						isClickable = false;
						dissentColor = getResources().getColor(R.color.app_text_gray2);
						setDissentBtnColor();
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					super.onFailure(t, errorNo, strMsg);
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (dialog != null) {
					dialog.dismiss();
				}
				strMsg = "连接失败";
				Toast.makeText(getActivity(), strMsg, 1000).show();// 显示登录失败提示
			}
		});
	}

	protected void setDissentBtnColor() {

	}

	/**
	 * 替换连接失败信息
	 */
	public String replaceErroStr(String str) {
		int index = str.indexOf("Connection to", 0);
		if (index != -1) {
			str = str.replaceAll(str, "连接失败");
		}
		// str=str.replaceAll("Connection to", "连接");
		// str=str.replace("refused", "失败");
		return str;
	}
}
