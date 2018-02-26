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
import com.newland.comp.adapter.my.WorkLoadAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.UserInfo;
import com.newland.comp.bean.my.PayrollQos;
import com.newland.comp.bean.my.PayrollQosXs;
import com.newland.comp.bean.my.PayrollWorkload;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.LoadingDialog;
import com.newland.comp2.R;

/**
 * 工作量
 * 
 * @author H81
 * 
 */
public class WorkLoadFragment extends BaseFragment {
	Context context;
	private View mView;
	private PopupWindow popupWindow;
	private TextView miniTitle;
	private LinearLayout xs_layout; // 系数
	private FrameLayout list_layout; // 数据布局
	int height1; // 获取控件高度
	int height2;
	private TextView currentView;
	private TextView zzzhl;// 工作折合量
	private TextView ysgzl;// 原始工作量
	private TextView grsjl;// 个人实际量
	private TextView zdyxz;// 最大允许值
	private TextView alpha;// 阴影遮罩
	private ListView list_data;
	private List<PayrollWorkload> list; // 接口封装数据
	private PayrollWorkload payrollWorkload = new PayrollWorkload();
	private TextView work_name; // 顶部2个系数
	private TextView work_value;
	private WorkLoadAdapter adapter;
	private View no_data_layout;
	private LoadingDialog dialog;
	private View load_msg;

	public void onClick(View arg0) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.workload, container, false);
		initView(mView, inflater);
		return mView;
	}

	private void initView(View parent, LayoutInflater inflater) {

		Bundle bundle = getArguments();
		list_data = (ListView) parent.findViewById(R.id.list_data);
		// 实例化标题栏弹窗
		miniTitle = (TextView) parent.findViewById(R.id.mini_title);
		xs_layout = (LinearLayout) parent.findViewById(R.id.xs_layout);
		list_layout = (FrameLayout) parent.findViewById(R.id.list_layout);
		alpha = (TextView) parent.findViewById(R.id.alpha);
		work_name = (TextView) parent.findViewById(R.id.work_name);
		work_value = (TextView) parent.findViewById(R.id.work_value);
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

		System.out.println(((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString());
		String month = ((TextView) getActivity().findViewById(R.id.head_right_tv)).getText().toString();
		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "payroll_workload");
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
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				if (StringUtil.isNotEmpty(strMsg))
					strMsg = replaceErroStr(strMsg);
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
				System.out.println(t.toString());
				if (load_msg != null) {
					load_msg.setVisibility(View.GONE);
				}

				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(context, "更新接口数据返回异常，请检查接口格式", 1000).show();
				}
				if (jsonInfov2 != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) // 正常返回数据
					{
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						list = jsonInfo.getDataList(PayrollWorkload.class);
						if (list != null) {
							if (list.size() == 0) {
								Toast.makeText(context, "无当月数据信息", 1000).show();
								work_name.setText("合计标准工作折合量：");
								work_value.setText("0");
								if (no_data_layout != null) // 显示无数据布局
								{
									no_data_layout.setVisibility(View.VISIBLE);
								}
								return;
							}

							if (no_data_layout != null) {
								no_data_layout.setVisibility(View.GONE);
							}
							// TODO
							findTopLevel();
							adapter = new WorkLoadAdapter(context, list, getActivity().findViewById(R.id.mColumnHorizontalScrollView));
							list_data.setAdapter(adapter);
						}
					} else {// 显示失败信息
						Toast.makeText(context, jsonInfov2.getResultDesc(), 1000).show();
					}
				}
			}
		});
	}

	/**
	 * 寻找顶层菜单 合计标准工作折合量
	 */
	private void findTopLevel() {
		for (PayrollWorkload bean : list) {
			if (("0").equals(bean.getSkill_level()))// 说明是顶级菜单
			{
				payrollWorkload = bean;
				break;
			}
		}
		list.remove(payrollWorkload);
		work_name.setText(StringUtil.noNull(payrollWorkload.getSkill_value()));
		work_value.setText(StringUtil.noNull(payrollWorkload.getWorkload()));
	}

	public static WorkLoadFragment newInstance(Context context) {
		return new WorkLoadFragment(context);
	}

	public WorkLoadFragment(Context context) {
		super();
		this.context = context;
	}

	private void popWindosDismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
			alpha.setVisibility(View.GONE);
		}
	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	protected void initPopuptWindow() {

		View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.workload_title_popup, null, false);

		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setAnimationStyle(R.style.pop_anim);
		popupWindow.showAsDropDown(miniTitle, 0, 0);

		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				popWindosDismiss();
				return false;
			}
		});

		zzzhl = (TextView) popupWindow_view.findViewById(R.id.zzzhl);
		ysgzl = (TextView) popupWindow_view.findViewById(R.id.ysgzl);
		grsjl = (TextView) popupWindow_view.findViewById(R.id.grsjl);
		zdyxz = (TextView) popupWindow_view.findViewById(R.id.zdyxz);
		if (currentView == null) {
			currentView = zzzhl;
		}
		initBtnColor();
		zzzhl.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = zzzhl;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.WORK;
					adapter.notifyDataSetChanged();
				}
				popWindosDismiss();
			}
		});
		ysgzl.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = ysgzl;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.ORIGIN_WORK;
					adapter.notifyDataSetChanged();
				}
				popWindosDismiss();
			}
		});
		grsjl.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = grsjl;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.FINAL_WORK;
					adapter.notifyDataSetChanged();
				}
				popWindosDismiss();
			}
		});
		zdyxz.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				currentView = zdyxz;
				initBtnColor();
				miniTitle.setText(currentView.getText().toString());
				if (adapter != null) {
					adapter.currentShow = adapter.MAX_VALUE;
					adapter.notifyDataSetChanged();
				}
				popWindosDismiss();
			}
		});
		alpha.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化颜色
	 */
	@SuppressLint("ResourceAsColor")
	private void initBtnColor() {
		zzzhl.setBackgroundResource(R.drawable.title_mini_nor);
		ysgzl.setBackgroundResource(R.drawable.title_mini_nor);
		grsjl.setBackgroundResource(R.drawable.title_mini_nor);
		zdyxz.setBackgroundResource(R.drawable.title_mini_nor);
		zzzhl.setTextColor(context.getResources().getColor(R.color.app_black));
		ysgzl.setTextColor(context.getResources().getColor(R.color.app_black));
		grsjl.setTextColor(context.getResources().getColor(R.color.app_black));
		zdyxz.setTextColor(context.getResources().getColor(R.color.app_black));
		if (currentView.getId() == R.id.zzzhl) {
			zzzhl.setBackgroundResource(R.drawable.title_mini_pre);
			zzzhl.setTextColor(context.getResources().getColor(R.color.app_green));
		} else if (currentView.getId() == R.id.ysgzl) {
			ysgzl.setBackgroundResource(R.drawable.title_mini_pre);
			ysgzl.setTextColor(context.getResources().getColor(R.color.app_green));
		} else if (currentView.getId() == R.id.grsjl) {
			grsjl.setBackgroundResource(R.drawable.title_mini_pre);
			grsjl.setTextColor(context.getResources().getColor(R.color.app_green));
		} else if (currentView.getId() == R.id.zdyxz) {
			zdyxz.setBackgroundResource(R.drawable.title_mini_pre);
			zdyxz.setTextColor(context.getResources().getColor(R.color.app_green));
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
