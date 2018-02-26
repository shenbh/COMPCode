package com.newland.comp.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.activity.indicator.IndicatorOrgActivity;
import com.newland.comp.bean.indicator.OrgIdSelect;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 选择指标机构 弹出框
 * 
 * @author Administrator
 * 
 */
public class IndicatorOrgPopupWindow extends PopupWindow {

	private RelativeLayout mTop_layout;
	private TextView mPop_title;
	private Button mClose;
	private LinearLayout mKs;
	private LinearLayout mQy;
	private List<OrgIdSelect> list;
	private Context context;
	List<OrgIdSelect> titleList;// 中心\科室list
	List<OrgIdSelect> ksList;
	private View mMenuView;
	String quarter_type;
	String zb_id;

	public IndicatorOrgPopupWindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.indicator_acc_popwin, null);
		this.context = context;

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(false);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

		bindViews();
		refresh();
	}

	private void bindViews() {

		mTop_layout = (RelativeLayout) mMenuView.findViewById(R.id.top_layout);
		mPop_title = (TextView) mMenuView.findViewById(R.id.pop_title);
		mClose = (Button) mMenuView.findViewById(R.id.close);
		mKs = (LinearLayout) mMenuView.findViewById(R.id.ks);
		mQy = (LinearLayout) mMenuView.findViewById(R.id.qy);
		mClose = (Button) mMenuView.findViewById(R.id.close);
		// 取消按钮
		mClose.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				((Activity) context).findViewById(R.id.alpha).setVisibility(View.GONE);
				// 销毁弹出框
				dismiss();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refresh() {

		String userid = SharedPreferencesUtils.getConfigStr(context, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "org_id_select");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", quarter_type);
		params.put("zb_id", zb_id);
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
					strMsg = new BaseActivity().replaceErroStr(strMsg);
				Toast.makeText(context, strMsg, 1000).show();
			}

			@Override
			public void onSuccess(Object t) {
				System.out.println(t.toString());
				JsonInfo jsonInfo = JSON.parseObject(t.toString(), JsonInfo.class);
				if (jsonInfo != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据

						list = jsonInfo.getDataList(OrgIdSelect.class);
						if (list.size() > 0) {
							initDataView();
						} else {
							Toast.makeText(context, "服务器返回无数据", 1000).show();// 显示失败提示
						}
					} else {
						Toast.makeText(context, jsonInfo.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
			}
		});

	}

	/**
	 * 把返回的数据按固定格式显示
	 */
	private void initDataView() {
		LinearLayout mainLayout = new LinearLayout(context);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleList = new ArrayList<OrgIdSelect>();// 中心\科室list
		ksList = new ArrayList<OrgIdSelect>(); // 区域list

		for (OrgIdSelect bean : list) {
			if (StringUtil.isEmpty(bean.org_pid)) // 如果没有父id则是顶级id
			{
				titleList.add(bean);
			} else {
				ksList.add(bean);
			}
		}

		for (int i = 1; i <= titleList.size(); i++) {

			if (mainLayout.getChildCount() < 2) {
				TextView tx = new TextView(context);
				tx.setText(titleList.get(i - 1).org_value);
				tx.setTag(titleList.get(i - 1).org_id);
				tx.setBackgroundResource(R.drawable.org_nor);
				tx.setGravity(Gravity.CENTER);
				tx.setTextColor(Color.rgb(0, 0, 0));
				tx.setPadding(10, 0, 10, 0);
				tx.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						initQuView(arg0.getTag().toString());
					}
				});

				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp1.leftMargin = 20;
				lp1.topMargin = 10;
				mainLayout.addView(tx, lp1);
				if (mainLayout.getChildCount() == 2 || i == titleList.size()) {
					mKs.addView(mainLayout, mainlp);
				}
			} else {
				// mKs.addView(mainLayout);
				mainLayout = new LinearLayout(context);
				mainLayout.setOrientation(LinearLayout.HORIZONTAL);
				TextView tx = new TextView(context);
				tx.setTag(titleList.get(i - 1).org_id);
				tx.setBackgroundResource(R.drawable.org_nor);
				tx.setGravity(Gravity.CENTER);
				tx.setTextColor(Color.rgb(0, 0, 0));
				tx.setText(titleList.get(i - 1).org_value);
				tx.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						initQuView(arg0.getTag().toString());
					}
				});
				tx.setPadding(10, 0, 10, 0);
				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp1.leftMargin = 20;
				lp1.topMargin = 10;
				mainLayout.addView(tx, lp1);
				if (i == titleList.size())// 说明最后一个了
				{
					mKs.addView(mainLayout, mainlp);
				}
			}

		}
		// if (mKs.getChildCount() == 0) // 说明不满一行
		// {
		// mKs.addView(mainLayout);
		// }

	}

	/**
	 * 根据科室 获取下面的区域
	 */
	private void initQuView(String pid) {

		mQy.removeAllViews();// 清除底下的view
		LinearLayout mainLayout = new LinearLayout(context);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		List<OrgIdSelect> tempList = new ArrayList<OrgIdSelect>();
		for (OrgIdSelect obj : ksList) { // 检索出pid下的子区域
			if (StringUtil.noNull(obj.getOrg_pid()).equals(pid)) {
				tempList.add(obj);
			}
		}

		if (tempList.size() > 0) {

			for (int i = 1; i <= tempList.size(); i++) {
				if (mainLayout.getChildCount() < 2) // 一行显示2个
				{
					TextView tx = new TextView(context);
					tx.setText(tempList.get(i - 1).org_value);
					tx.setTag(tempList.get(i - 1).org_id);
					tx.setBackgroundResource(R.drawable.org_nor);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(0, 0, 0));
					tx.setPadding(10, 0, 10, 0);
					tx.setOnClickListener(new OnClickListener() {

						public void onClick(View arg0) {

						}
					});

					LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp1.leftMargin = 20;
					lp1.topMargin = 10;
					mainLayout.addView(tx, lp1);
					if (mainLayout.getChildCount() == 2 || i == tempList.size()) {
						mQy.addView(mainLayout, mainlp);
					}
				} else {

					mainLayout = new LinearLayout(context);
					mainLayout.setOrientation(LinearLayout.HORIZONTAL);
					TextView tx = new TextView(context);
					tx.setText(tempList.get(i - 1).org_value);
					tx.setTag(tempList.get(i - 1).org_id);
					tx.setBackgroundResource(R.drawable.org_nor);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(0, 0, 0));
					tx.setPadding(10, 0, 10, 0);
					LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp1.leftMargin = 20;
					lp1.topMargin = 10;
					mainLayout.addView(tx, lp1);
					if (i == tempList.size())// 说明最后一个了
					{
						mQy.addView(mainLayout, mainlp);
					}

				}

			}

		}

	}

}
