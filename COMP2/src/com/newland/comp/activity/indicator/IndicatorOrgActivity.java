package com.newland.comp.activity.indicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.indicator.OrgIdSelect;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.JsonInfoV3;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 机构查询
 * 
 * @author H81
 *
 */
public class IndicatorOrgActivity extends BaseActivity {

	private RelativeLayout mTop_layout;
	private TextView mPop_title;
	private Button mClose;
	private LinearLayout mKs;
	private LinearLayout mQy;
	private List<OrgIdSelect> list;
	private String currentOrgId = "";// 当前选中的org_id
	private String currentOrgType = "";// 当前选中的org_type
	private TextView currentView; // 当前选中的科室view 机构所在类别 2-中心 3-科室 4-区域 5-小组
	private TextView currentQyView; // 当前选中的区域view

	List<OrgIdSelect> titleList;// 中心\科室list
	List<OrgIdSelect> qyList;
	private View load_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);// 需要添加的语句
		setContentView(R.layout.indicator_acc_orgid);
		bindViews();
		addLinster();
		refresh();
	}

	private void bindViews() {
		load_msg = findViewById(R.id.load_msg);
		mTop_layout = (RelativeLayout) findViewById(R.id.top_layout);
		mPop_title = (TextView) findViewById(R.id.pop_title);
		mClose = (Button) findViewById(R.id.close);
		mKs = (LinearLayout) findViewById(R.id.ks);
		mQy = (LinearLayout) findViewById(R.id.qy);
	}

	private void addLinster() {
		mClose.setOnClickListener(new OnClickListener() //点击关闭，不返回值，则不进行网络请求
		{

			
			public void onClick(View arg0) {
				Intent data = new Intent();
				data.putExtra("org_id", currentOrgId);
				// 请求代码可以自己设置，这里设置成20
				// 循环寻找对应org_id的org_type
				data.putExtra("org_type", currentOrgType);
				IndicatorOrgActivity.this.finish();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void refresh() {

		String quarter_type = StringUtil.noNull(getIntent().getStringExtra("quarter_type"));
		String zb_id = StringUtil.noNull(getIntent().getStringExtra("zb_id"));

		String userid = SharedPreferencesUtils.getConfigStr(this, BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "org_id_select");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", quarter_type);
		params.put("zb_id", zb_id);
		System.out.println(HttpUtils.URL+"?" + params.toString());
		FinalHttp fh = new FinalHttp();fh.configTimeout(HttpUtils.CONNECTION_TIMEOUT);;
		fh.post(HttpUtils.URL, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {if(StringUtil.isNotEmpty(strMsg))strMsg=replaceErroStr(strMsg);
				if (IndicatorOrgActivity.this==null) {
					return;
				}
				strMsg="连接超时";Toast.makeText(getApplicationContext(), strMsg, 1000).show();
				load_msg.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(Object t) {
				if (IndicatorOrgActivity.this==null) {
					return;
				}
				System.out.println("org_id_select--"+t.toString());
				load_msg.setVisibility(View.GONE);

				JsonInfoV3 jsonInfo = null;
					try {
						jsonInfo = JSON.parseObject(t.toString(),JsonInfoV3.class);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
					}
					

				if (jsonInfo != null) {
					if (JsonInfoV2.SUCCESS_CODE.equals(jsonInfo.getResultCode())) {// 正常返回数据
						list=jsonInfo.getResultDataKeyToClass("data",OrgIdSelect.class );
						if (list.size() > 0) {
							initDataView();
						} else {
							Toast.makeText(getApplicationContext(), "服务器返回无数据", 1000).show();// 显示失败提示
						}
					} else {
						Toast.makeText(getApplicationContext(), jsonInfo.getResultDesc(), 1000).show();// 显示失败提示
					}
				}
			}
		});

	}

	/**
	 * 把返回的数据按固定格式显示
	 */
	private void initDataView() {
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleList = new ArrayList<OrgIdSelect>();// 中心\科室list
		qyList = new ArrayList<OrgIdSelect>(); // 区域list
		final Set<TextView> titleSet = new HashSet<TextView>(); // 用于调整选中状态
		for (OrgIdSelect bean : list) {
//			if (StringUtil.isEmpty(bean.org_pid)) // 如果没有父id则是顶级id
//			{
//				titleList.add(bean);
//			} else {
//				qyList.add(bean);
//			}
			if (StringUtil.noNull(bean.org_type).equals("3")) // 
			{
				titleList.add(bean);
			} else if (StringUtil.noNull(bean.org_type).equals("4")){
				qyList.add(bean);
			}
		}

		for (int i = 1; i <= titleList.size(); i++) {

			if (mainLayout.getChildCount() < 3) {
				final TextView tx = new TextView(this);
				tx.setText(titleList.get(i - 1).org_value);
				tx.setTag(titleList.get(i - 1).org_id);
				tx.setBackgroundResource(R.drawable.org_nor);
				tx.setGravity(Gravity.CENTER);
				tx.setTextColor(Color.rgb(0, 0, 0));
				tx.setPadding(10, 0, 10, 0);
				tx.setOnClickListener(new OnClickListener() {
					
					public void onClick(View arg0) {
						initQuView(arg0.getTag().toString()); // 展开以下区域项
						currentView = tx;
						currentOrgId = tx.getTag().toString();
						updateKsView(titleSet);
					}
				});

				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp1.leftMargin = 20;
				lp1.topMargin = 10;
				mainLayout.addView(tx, lp1);
				titleSet.add(tx);
				if (mainLayout.getChildCount() == 3 || i == titleList.size()) {
					mKs.addView(mainLayout, mainlp);
				}
			} else {
				mainLayout = new LinearLayout(this);
				mainLayout.setOrientation(LinearLayout.HORIZONTAL);
				final TextView tx = new TextView(this);
				tx.setTag(titleList.get(i - 1).org_id);
				tx.setBackgroundResource(R.drawable.org_nor);
				tx.setGravity(Gravity.CENTER);
				tx.setTextColor(Color.rgb(0, 0, 0));
				tx.setText(titleList.get(i - 1).org_value);
				tx.setOnClickListener(new OnClickListener() {
					
					public void onClick(View arg0) {
						initQuView(arg0.getTag().toString());
						currentView = tx;
						currentOrgId = tx.getTag().toString();
						updateKsView(titleSet);
					}
				});
				tx.setPadding(10, 0, 10, 0);
				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp1.leftMargin = 20;
				lp1.topMargin = 10;
				mainLayout.addView(tx, lp1);
				titleSet.add(tx);
				if (i == titleList.size())// 说明最后一个了
				{
					mKs.addView(mainLayout, mainlp);
				}
			}
		}

	}

	/**
	 * 更新科室的选择状态
	 * 
	 * @param titleSet
	 */
	private void updateKsView(Set<TextView> titleSet) {
		// 更新机构的选择view
		for (TextView textView : titleSet) {
			textView.setBackgroundResource(R.drawable.org_nor); // 置为未选中状态
		}
		if (currentView != null) {
			currentView.setBackgroundResource(R.drawable.org_pre); // 选中
		}
	}

	/**
	 * 根据科室 获取下面的区域
	 */
	private void initQuView(String pid) {

		mQy.removeAllViews();// 清除底下的view
		final Set<TextView> qyeSet = new HashSet<TextView>(); // 用于调整选中状态
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams mainlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final List<OrgIdSelect> tempList = new ArrayList<OrgIdSelect>();
		for (OrgIdSelect obj : qyList) { // 检索出pid下的子区域
			if (StringUtil.noNull(obj.getOrg_pid()).equals(pid)) {
				tempList.add(obj);
			}
		}

		if (tempList.size() > 0) {

			for (int i = 1; i <= tempList.size(); i++) {
				if (mainLayout.getChildCount() < 3) // 一行显示3个
				{
					final TextView tx = new TextView(this);
					tx.setText(tempList.get(i - 1).org_value);
					tx.setTag(tempList.get(i - 1).org_id);
					tx.setBackgroundResource(R.drawable.org_nor);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(0, 0, 0));
					tx.setPadding(10, 0, 10, 0);
					tx.setOnClickListener(new OnClickListener() {
						
						public void onClick(View arg0) {
							currentOrgId = tx.getTag().toString();
							currentQyView = tx;
							UpdateQyView(qyeSet);
							
						}
					});

					LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp1.leftMargin = 10;
					lp1.topMargin = 10;
					mainLayout.addView(tx, lp1);
					qyeSet.add(tx);
					if (mainLayout.getChildCount() == 2 || i == tempList.size()) {
						mQy.addView(mainLayout, mainlp);
					}
				} else {

					mainLayout = new LinearLayout(this);
					mainLayout.setOrientation(LinearLayout.HORIZONTAL);
					final TextView tx = new TextView(this);
					tx.setText(tempList.get(i - 1).org_value);
					tx.setTag(tempList.get(i - 1).org_id);
					tx.setBackgroundResource(R.drawable.org_nor);
					tx.setGravity(Gravity.CENTER);
					tx.setTextColor(Color.rgb(0, 0, 0));
					tx.setPadding(10, 0, 10, 0);
					tx.setOnClickListener(new OnClickListener() {
						
						public void onClick(View arg0) {
							currentOrgId = tx.getTag().toString();
							currentQyView = tx;
							UpdateQyView(qyeSet);
						}
					});
					LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp1.leftMargin = 10;
					lp1.topMargin = 10;
					mainLayout.addView(tx, lp1);
					qyeSet.add(tx);
					if (i == tempList.size())// 说明最后一个了
					{
						mQy.addView(mainLayout, mainlp);
					}

				}

			}

		}

	}

	/**
	 * 更新区域的选中状态
	 * 
	 * @param qyeSet
	 */
	private void UpdateQyView(Set<TextView> qyeSet) {
		// 更新机构的选择view
		for (TextView textView : qyeSet) {
			textView.setBackgroundResource(R.drawable.org_nor); // 置为未选中状态
		}
		if (currentQyView != null) {
			currentQyView.setBackgroundResource(R.drawable.org_pre); // 选中
		}
	}

	public void search(View view) {
		Intent data = new Intent();
		data.putExtra("org_id", currentOrgId);
		// 请求代码可以自己设置，这里设置成20
//		循环寻找对应org_id的org_type
		if (list!=null) {
			for (int i = 0; i < list.size(); i++) {
				if (currentOrgId.equals(list.get(i).org_id)) {
					currentOrgType=list.get(i).org_type;
					data.putExtra("org_type", currentOrgType);
					break;
				}
			}
		}else
		{
			data.putExtra("org_type", currentOrgType);
		}
		setResult(20, data);
		IndicatorOrgActivity.this.finish();
	}
}
