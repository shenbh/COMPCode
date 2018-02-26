package com.newland.comp.fragment;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.LoginActivity;
import com.newland.comp.activity.MenuTabActivity;
import com.newland.comp.adapter.my.QosAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.PayrollQos;
import com.newland.comp.bean.my.PayrollQosXs;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 服务质量
 * 
 * @author H81
 * 
 */
public class QosFragment extends BaseFragment {
	Context context;
	private View mView;
	private PopupWindow popupWindow;
	private TextView miniTitle;
	private LinearLayout xs_layout; // 系数
	private FrameLayout list_layout; // 数据布局
	int height1; // 获取控件高度
	int height2;
	private TextView currentView;
	private TextView mFwzl;
	private TextView mUskh;
	private TextView mJssm;
	private TextView alpha;// 阴影遮罩
	private ListView list_data;
	private List<PayrollQos> list; // 接口封装数据
	private PayrollQosXs payrollQosXs;
	private TextView sjfwzlxs; // 顶部2个系数
	private TextView sjfwzltj;
	private QosAdapter adapter;
	private View no_data_layout;
	private LoadingDialog dialog;
	private View load_msg;

	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.qos, container, false);
		initView(mView, inflater);
		return mView;
	}

	private void initView(View parent, LayoutInflater inflater) {

		Bundle bundle = getArguments();
		if (bundle != null) {
		}
		list_data = (ListView) parent.findViewById(R.id.list_data);
		// 实例化标题栏弹窗
		miniTitle = (TextView) parent.findViewById(R.id.mini_title);
		xs_layout = (LinearLayout) parent.findViewById(R.id.xs_layout);
		list_layout = (FrameLayout) parent.findViewById(R.id.list_layout);
		alpha = (TextView) parent.findViewById(R.id.alpha);
		sjfwzlxs = (TextView) parent.findViewById(R.id.sjfwzlxs);
		sjfwzltj = (TextView) parent.findViewById(R.id.sjfwzltj);
		miniTitle.setOnClickListener(new OnClickListener() {

			
			public void onClick(View v) {
				getPopupWindow();

				popupWindow.showAsDropDown(v);
				initBtnColor();
			}
		});
		no_data_layout = parent.findViewById(R.id.no_data_layout);
		load_msg = parent.findViewById(R.id.load_msg);
		refresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh() {
		load_msg.setVisibility(View.VISIBLE);
		System.out.println(((TextView) getActivity().findViewById(
				R.id.head_right_tv)).getText().toString());
		String month = ((TextView) getActivity().findViewById(
				R.id.head_right_tv)).getText().toString();
		String userid = SharedPreferencesUtils.getConfigStr(context,
				BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "payroll_qos");
		params.put("month", month);
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
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
				if (load_msg != null) {
					load_msg.setVisibility(View.GONE);
				}
				no_data_layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(Object t) {
				if (getActivity() == null) // 说明上层activity 已经被销毁了
				{
					System.out.println("getActivity() 拦截");
					return;
				}
				if (load_msg != null) {
					load_msg.setVisibility(View.GONE);
				}
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
						payrollQosXs = jsonInfo.getData_exp(PayrollQosXs.class);
						if (payrollQosXs != null) {
							sjfwzlxs.setText(StringUtil.noNull(payrollQosXs
									.getService_final_num()));
							sjfwzltj.setText(StringUtil.noNull(payrollQosXs
									.getService_adjust_num()));
						} else {
							sjfwzlxs.setText("0");
							sjfwzltj.setText("0");
						}
						list = jsonInfo.getDataList(PayrollQos.class);
						if (list != null) {
							if (list.size() == 0) {
								Toast.makeText(context, "无当月数据信息", 1000).show();

								if (no_data_layout != null) // 显示无数据布局
								{
									no_data_layout.setVisibility(View.VISIBLE);
								}
								return;
							}
							if (no_data_layout != null) {
								no_data_layout.setVisibility(View.GONE);
							}
							adapter = new QosAdapter(context, list,
									getActivity().findViewById(
											R.id.mColumnHorizontalScrollView));
							list_data.setAdapter(adapter);
						}

					} else {// 显示登陆失败信息
						Toast.makeText(context, jsonInfov2.getResultDesc(),
								1000).show();
					}
				}

			}
		});

	}

	public static QosFragment newInstance(Context context) {
		return new QosFragment(context);
	}

	public QosFragment(Context context) {
		super();
		this.context = context;
	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub

		View popupWindow_view = getActivity().getLayoutInflater().inflate(
				R.layout.title_popup, null, false);

		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setAnimationStyle(R.style.pop_anim);
		popupWindow.showAsDropDown(miniTitle, 0, 0);

		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
					alpha.setVisibility(View.GONE);
					// list_data.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		mFwzl = (TextView) popupWindow_view.findViewById(R.id.fwzl);
		mUskh = (TextView) popupWindow_view.findViewById(R.id.uskh);
		mJssm = (TextView) popupWindow_view.findViewById(R.id.jssm);
		if (currentView == null) {
			currentView = mFwzl;
		}
		initBtnColor();
		mFwzl.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = mFwzl;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.FWZL;
					adapter.notifyDataSetChanged();
				}

			}
		});
		mUskh.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = mUskh;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.YSKH;
					adapter.notifyDataSetChanged();
				}
			}
		});
		mJssm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = mJssm;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.JSSM;
					adapter.notifyDataSetChanged();
				}
			}
		});
		alpha.setVisibility(View.VISIBLE);
		// list_data.setVisibility(View.GONE);
	}

	/**
	 * 初始化颜色
	 */
	@SuppressLint("ResourceAsColor")
	private void initBtnColor() {
		mFwzl.setBackgroundResource(R.drawable.title_mini_nor);
		mUskh.setBackgroundResource(R.drawable.title_mini_nor);
		mJssm.setBackgroundResource(R.drawable.title_mini_nor);
		mFwzl.setTextColor(context.getResources().getColor(R.color.app_black));
		mUskh.setTextColor(context.getResources().getColor(R.color.app_black));
		mJssm.setTextColor(context.getResources().getColor(R.color.app_black));
		if (currentView.getId() == R.id.fwzl) {
			mFwzl.setBackgroundResource(R.drawable.title_mini_pre);
			mFwzl.setTextColor(context.getResources().getColor(
					R.color.app_green));
		} else if (currentView.getId() == R.id.uskh) {
			mUskh.setBackgroundResource(R.drawable.title_mini_pre);
			mUskh.setTextColor(context.getResources().getColor(
					R.color.app_green));
		} else if (currentView.getId() == R.id.jssm) {
			mJssm.setBackgroundResource(R.drawable.title_mini_pre);
			mJssm.setTextColor(context.getResources().getColor(
					R.color.app_green));
		}

	}

	@SuppressLint("NewApi")
	private void getPopupWindow() {

		if (null != popupWindow) {
			popupWindow.dismiss();
			alpha.setVisibility(View.GONE);
			// list_data.setVisibility(View.VISIBLE);
			return;
		} else {
			initPopuptWindow();
		}
	}
}
