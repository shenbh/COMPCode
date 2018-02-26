package com.newland.comp.activity.indicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.newland.comp.activity.BaseActivity;
import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.bean.JsonInfoV2;
import com.newland.comp.bean.indicator.Warning;
import com.newland.comp.common.AppContext;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.fragment.IndicatorOperationFragment;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.HttpUtils;
import com.newland.comp.utils.JsonInfo;
import com.newland.comp.utils.MD5Utils;
import com.newland.comp.utils.SharedPreferencesUtils;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

/**
 * 运营指标
 * 
 * @author H81
 * 
 */
public class IndicatorActivity extends FragmentActivity {

	private String[] time_period = { "按天查询", "按周查询", "按月查询", "按季查询" };
	private String[] time_period2 = { "按天查询", "按周查询", "按月查询" }; // 营销指标 服务指标无季度
	private String[] week_period = { "第一周", "第二周", "第三周", "第四周", "第五周", "第六周" };
	private String[] quarter_period = { "第一季度", "第二季度", "第三季度", "第四季度" };
	public static String DAY = "day";
	public static String WEEK = "week";
	public static String MONTH = "month";
	public static String QUARTER = "quarte";
	public static String currentDateType = DAY; // 当前的查询周期

	// 预警
	private PopupWindow popupWindow;
	private TextView right_tv;
	private TextView currentView;

	private List<BaseFragment> listFragments;
	private IndicatorOperationFragment indicatorOperationFragment;// 运营指标
	private BaseFragment currentFragment;// 当前选中的fragment
	private ViewPager mViewPager;

	// 控件
	// private LinearLayout mIndicator_ll_operation;// 运营指标
	private TextView mIndicator_tv_operation;
	private ImageView mIndicator_iv_operation;
	// private LinearLayout mIndicator_ll_marketing;//营销指标--呼入营销指标
	private TextView mIndicator_tv_marketing;
	private ImageView mIndicator_iv_marketing;
	// private LinearLayout mIndicator_ll_callout;//电话经理外呼营销指标
	private TextView mIndicator_tv_callout;
	private ImageView mIndicator_iv_callout;
	// private LinearLayout mIndicator_ll_service;// 服务指标
	private TextView mIndicator_tv_service;
	private ImageView mIndicator_iv_service;
	ImageButton right_btn;
	LinearLayout hor_lin;

	private Spinner mSelect_dur;// 查询条件
	private TextView mZb_time;
	private TextView mZb_time_dropdown;
	private Spinner mWeek;
	private TextView mSpinner_week_pulldown;
	private TextView mWeek_pulldown;

	private Warning warning; // 话务预警
	String time;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;

	private long selectTime;// 选中的时间-换成毫秒值保存

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.fragment_indicator_layout);
		initView();
		setTitle();
		initFragment();
		addLinster();
		reflush();// 加载话务预警

		selectTime = StringUtil.getLongTime(StringUtil.getLastTime(), StringUtil.DAY_TIME);
	}

	private void findViewById() {
		hor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mSelect_dur = (Spinner) findViewById(R.id.select_dur);
		mZb_time = (TextView) findViewById(R.id.zb_time);
		mZb_time_dropdown = (TextView) findViewById(R.id.zb_time_dropdown);
		mWeek = (Spinner) findViewById(R.id.week);
		mSpinner_week_pulldown = (TextView) findViewById(R.id.spinner_week_pulldown);
		mWeek_pulldown = (TextView) findViewById(R.id.spinner_week_pulldown);

		// 运营指标
		// mIndicator_ll_operation = (LinearLayout)
		// findViewById(R.id.indicator_ll_operation);
		mIndicator_tv_operation = (TextView) findViewById(R.id.indicator_tv_operation);
		mIndicator_iv_operation = (ImageView) findViewById(R.id.indicator_iv_operation);
		// 营销指标--呼入
		// mIndicator_ll_marketing = (LinearLayout)
		// findViewById(R.id.indicator_ll_marketing);
		mIndicator_tv_marketing = (TextView) findViewById(R.id.indicator_tv_marketing);
		mIndicator_iv_marketing = (ImageView) findViewById(R.id.indicator_iv_marketing);
		// 外呼
		// mIndicator_ll_callout = (LinearLayout)
		// findViewById(R.id.indicator_ll_callout);
		mIndicator_tv_callout = (TextView) findViewById(R.id.indicator_tv_callout);
		mIndicator_iv_callout = (ImageView) findViewById(R.id.indicator_iv_callout);
		// 服务指标
		// mIndicator_ll_service = (LinearLayout)
		// findViewById(R.id.indicator_ll_service);
		mIndicator_tv_service = (TextView) findViewById(R.id.indicator_tv_service);
		mIndicator_iv_service = (ImageView) findViewById(R.id.indicator_iv_service);

	}

	private void initView() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		findViewById();

		ActivityUtil.showDropDown(this, mSelect_dur, time_period, R.layout.simple_spinner_item);
		ActivityUtil.showDropDown(this, mWeek, week_period, R.layout.simple_spinner_item);
	}

	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		indicatorOperationFragment = IndicatorOperationFragment.newInstance(IndicatorActivity.this);
		Bundle bundle = new Bundle();
		bundle.putString("item_type", "1");
		indicatorOperationFragment.setArguments(bundle);

		IndicatorOperationFragment indicatorOperationFragment3 = IndicatorOperationFragment.newInstance(IndicatorActivity.this);
		Bundle bundle3 = new Bundle();
		bundle3.putString("item_type", "3");
		indicatorOperationFragment3.setArguments(bundle3);

		IndicatorOperationFragment indicatorOperationFragment4 = IndicatorOperationFragment.newInstance(IndicatorActivity.this);
		Bundle bundle4 = new Bundle();
		bundle4.putString("item_type", "4");
		indicatorOperationFragment4.setArguments(bundle4);

		IndicatorOperationFragment indicatorOperationFragment5 = IndicatorOperationFragment.newInstance(IndicatorActivity.this);
		Bundle bundle5 = new Bundle();
		bundle5.putString("item_type", "5");
		indicatorOperationFragment5.setArguments(bundle5);

		listFragments.add(indicatorOperationFragment);
		listFragments.add(indicatorOperationFragment3);
		listFragments.add(indicatorOperationFragment4);
		listFragments.add(indicatorOperationFragment5);

		BaseFragmentPagerAdapter mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(pageListener);
		mViewPager.setOffscreenPageLimit(4);
		currentFragment = indicatorOperationFragment;
	}

	/**
	 * 增加监听
	 */
	private void addLinster() {
		mZb_time.setText(StringUtil.getLastTime());
		mZb_time.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (currentDateType.equals(DAY))// 日
				{
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					new DatePickDialog(IndicatorActivity.this, DatePickDialog.YEAR_MONTH_DAY).datePicKDialog(mZb_time);
				} else if (currentDateType.equals(WEEK))// 周
				{
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					new DatePickDialog(IndicatorActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(mZb_time);
				} else if (currentDateType.equals(MONTH))// 月
				{
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					new DatePickDialog(IndicatorActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(mZb_time);
				} else if (currentDateType.equals(QUARTER))// 季
				{
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					new DatePickDialog(IndicatorActivity.this, DatePickDialog.YEAR).datePicKDialog(mZb_time);
				}
			}
		});
		mSelect_dur.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View view, int poistion, long arg3) {
				Map<String, String> map = new HashMap<String, String>();
				if (poistion == 0) // 按天
				{
					if (StringUtil.isNotEmpty(DatePickDialog.selectTimeStr)) {
						selectTime = StringUtil.getLongTime(DatePickDialog.selectTimeStr, StringUtil.DAY_TIME);
					}
					String tempDay = StringUtil.long2FormatTime(selectTime, StringUtil.DAY_TIME);

					mWeek.setVisibility(View.GONE);
					mSpinner_week_pulldown.setVisibility(View.GONE);
					mWeek_pulldown.setVisibility(View.GONE);
					currentDateType = DAY;
					mZb_time.setText(tempDay);
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);// 显示3列跟两列的spinner时显示得背景图片不一样
					mZb_time_dropdown.setBackgroundResource(R.drawable.spanner_alf2_final);
				} else if (poistion == 1) // 按周
				{
					if (StringUtil.isNotEmpty(DatePickDialog.selectTimeStr)) {
						selectTime = StringUtil.getLongTime(DatePickDialog.selectTimeStr, StringUtil.MONTH_TIME);
					}

					String tempWeek = StringUtil.long2FormatTime(selectTime, StringUtil.MONTH_TIME);

					mWeek.setVisibility(View.VISIBLE);
					mSpinner_week_pulldown.setVisibility(View.VISIBLE);
					mWeek_pulldown.setVisibility(View.VISIBLE);
					currentDateType = WEEK;
					ActivityUtil.showDropDown(IndicatorActivity.this, mWeek, week_period, R.layout.simple_spinner_item);
					mZb_time.setText(tempWeek);
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					mZb_time_dropdown.setBackgroundResource(R.drawable.spanner_alf_final);
				} else if (poistion == 2) // 按月
				{
					if (StringUtil.isNotEmpty(DatePickDialog.selectTimeStr)) {
						selectTime = StringUtil.getLongTime(DatePickDialog.selectTimeStr, StringUtil.MONTH_TIME);
					}
					String tempMonth = StringUtil.long2FormatTime(selectTime, StringUtil.MONTH_TIME);

					mWeek.setVisibility(View.GONE);
					mSpinner_week_pulldown.setVisibility(View.GONE);
					mWeek_pulldown.setVisibility(View.GONE);
					currentDateType = MONTH;
					mZb_time.setText(tempMonth);
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					mZb_time_dropdown.setBackgroundResource(R.drawable.spanner_alf2_final);
				} else if (poistion == 3) // 按季
				{
					if (StringUtil.isNotEmpty(DatePickDialog.selectTimeStr)) {
						selectTime = StringUtil.getLongTime(DatePickDialog.selectTimeStr, StringUtil.YEAR_TIME);
					}
					String tempYear = StringUtil.long2FormatTime(selectTime, StringUtil.YEAR_TIME);

					mWeek.setVisibility(View.VISIBLE);
					mSpinner_week_pulldown.setVisibility(View.VISIBLE);
					mWeek_pulldown.setVisibility(View.VISIBLE);
					currentDateType = QUARTER;
					ActivityUtil.showDropDown(IndicatorActivity.this, mWeek, quarter_period, R.layout.simple_spinner_item);
					mZb_time.setText(tempYear);
					mZb_time.setBackgroundResource(R.drawable.spanner_alf2);
					mZb_time_dropdown.setBackgroundResource(R.drawable.spanner_alf_final);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int position) {
			clearPress();
			mViewPager.setCurrentItem(position);
			currentFragment = listFragments.get(position);
			switch (position) {
			case 0:
				mIndicator_iv_operation.setVisibility(View.VISIBLE);
				mIndicator_tv_operation.setTextColor(IndicatorActivity.this.getResources().getColor(R.color.app_green));
				// ActivityUtil.showDropDown(IndicatorActivity.this,
				// mSelect_dur, time_period, R.layout.simple_spinner_item);
				currentFragment.refresh();
//				setCenterTitle("运营指标");
				break;
			case 1:
				mIndicator_iv_service.setVisibility(View.VISIBLE);
				mIndicator_tv_service.setTextColor(IndicatorActivity.this.getResources().getColor(R.color.app_green));
				// ActivityUtil.showDropDown(IndicatorActivity.this,
				// mSelect_dur, time_period, R.layout.simple_spinner_item);
				currentFragment.refresh();
//				setCenterTitle("服务指标");
				break;
			case 2:
				mIndicator_iv_marketing.setVisibility(View.VISIBLE);
				mIndicator_tv_marketing.setTextColor(IndicatorActivity.this.getResources().getColor(R.color.app_green));
				// ActivityUtil.showDropDown(IndicatorActivity.this,
				// mSelect_dur, time_period2, R.layout.simple_spinner_item);
				currentFragment.refresh();
//				setCenterTitle("呼入营销");
				break;
			case 3:
				mIndicator_iv_callout.setVisibility(View.VISIBLE);
				mIndicator_tv_callout.setTextColor(IndicatorActivity.this.getResources().getColor(R.color.app_green));
				// ActivityUtil.showDropDown(IndicatorActivity.this,
				// mSelect_dur, time_period, R.layout.simple_spinner_item);
				currentFragment.refresh();
//				setCenterTitle("外呼营销");
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 把pageview的颜色重置为灰色且无下划线
	 */
	private void clearPress() {

		mIndicator_iv_operation.setVisibility(View.INVISIBLE);
		mIndicator_iv_marketing.setVisibility(View.INVISIBLE);
		mIndicator_iv_callout.setVisibility(View.INVISIBLE);
		mIndicator_iv_service.setVisibility(View.INVISIBLE);

		mIndicator_tv_operation.setTextColor(this.getResources().getColor(R.color.gray));
		mIndicator_tv_marketing.setTextColor(this.getResources().getColor(R.color.gray));
		mIndicator_tv_callout.setTextColor(this.getResources().getColor(R.color.gray));
		mIndicator_tv_service.setTextColor(this.getResources().getColor(R.color.gray));

	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);

		if (right_btn != null) {
			right_btn.setVisibility(View.VISIBLE);
//			right_btn.setBackgroundResource(R.drawable.yj);
			right_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					getPopupWindow();

					popupWindow.showAsDropDown(arg0);
				}
			});
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);

		}
		setCenterTitle("指标监控");
	}
	private void setCenterTitle(String s){
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText(s);
	}

	private void getPopupWindow() {

		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	private TextView mFwzl;
	private TextView mUskh;
	private TextView mJssm;

	protected void initPopuptWindow() {
		View popupWindow_view = this.getLayoutInflater().inflate(R.layout.fragment_indicator_operation_earlywarning, null, false);

		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setAnimationStyle(R.style.pop_anim);
		popupWindow.showAsDropDown(right_btn, 0, 0);

		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});

		mFwzl = (TextView) popupWindow_view.findViewById(R.id.fwzl);
		mUskh = (TextView) popupWindow_view.findViewById(R.id.uskh);
		mJssm = (TextView) popupWindow_view.findViewById(R.id.jssm);
		if (warning != null) {
			mFwzl.setText("话务预警：" + StringUtil.noNull(warning.hw_warn, "0"));
			mUskh.setText("故障情况：" + StringUtil.noNull(warning.fault_warn, "0"));
			mJssm.setText("投诉情况：" + StringUtil.noNull(warning.complaint, "0"));
			mFwzl.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(IndicatorActivity.this, IndicatorWarningActivity.class);
					intent.putExtra("quarter_type", currentDateType);
					intent.putExtra("time", time);
					intent.putExtra("type", "hw_warn");
					startActivity(intent);
				}
			});

			mUskh.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(IndicatorActivity.this, IndicatorWarningActivity.class);
					intent.putExtra("quarter_type", currentDateType);
					intent.putExtra("time", time);
					intent.putExtra("type", "fault_warn");
					startActivity(intent);
				}
			});

			mJssm.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Intent intent = new Intent(IndicatorActivity.this, IndicatorWarningActivity.class);
					intent.putExtra("quarter_type", currentDateType);
					intent.putExtra("time", time);
					intent.putExtra("type", "complaint");
					startActivity(intent);
				}
			});
		}
	}

	public void onClick(View view) {
		clearPress();
		int id = view.getId();
		if (id == R.id.indicator_ll_operation) {// 运营指标
			mIndicator_iv_operation.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		}
		if (id == R.id.indicator_ll_service) {// 服务指标
			mIndicator_iv_service.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		}
		if (id == R.id.indicator_ll_marketing) {// 营销指标----呼入
			mIndicator_iv_marketing.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
		}
		if (id == R.id.indicator_ll_callout) {// 外呼
			mIndicator_iv_callout.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(3);
		}
	}

	/**
	 * 搜索
	 * 
	 * @param view
	 */
	public void btn_search(View view) {
		currentFragment.page = 1;
		currentFragment.refresh();
		reflush();// 加载话务预警
	}

	/**
	 * 选择日期
	 * 
	 * @param view
	 */
	public void btn_edit_time(View view) {

	}

	/**
	 * 折线图隐藏
	 * 
	 * @param view
	 */
	public void chart_hide_btn(View view) {
		// 设置不显示图表
		boolean isShow = SharedPreferencesUtils.getConfigBoolean(getApplicationContext(), SharedPreferencesUtils.Name, "showChart");
		if (isShow) {
			SharedPreferencesUtils.setConfigBoolean(getApplicationContext(), SharedPreferencesUtils.Name, "showChart", false);
		} else {
			SharedPreferencesUtils.setConfigBoolean(getApplicationContext(), SharedPreferencesUtils.Name, "showChart", true);
		}
		currentFragment.refresh();
	}

	/**
	 * 刷新话务预警
	 */
	@SuppressWarnings("unchecked")
	public void reflush() {

		String userid = SharedPreferencesUtils.getConfigStr(getApplicationContext(), BaseActivity.CASH_NAME, "userid");
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("method", "warning");
		params.put("signid", MD5Utils.getInstance().getUserIdSignid(userid));
		params.put("quarter_type", currentDateType);
		if (IndicatorActivity.currentDateType.equals(IndicatorActivity.DAY)) // 按天选择
		{
			time = mZb_time.getText().toString();
			params.put("time", time);
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.WEEK)) {// 按周选择
																						// 2015-03,1
																						// spinner索引默认为0
			time = mZb_time.getText().toString() + "," + (mWeek.getSelectedItemPosition() + 1);
			params.put("time", time);
		} else if (IndicatorActivity.currentDateType.equals(IndicatorActivity.MONTH)) {// 按月
			time = mZb_time.getText().toString();
			params.put("time", time);
		} else { // 按季度
			time = mZb_time.getText().toString() + "," + (mWeek.getSelectedItemPosition() + 1);
			params.put("time", time);
		}
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
				if (IndicatorActivity.this == null) {
					return;
				}
				strMsg = "连接超时";
				Toast.makeText(getApplicationContext(), strMsg, 1000).show();
			}

			@Override
			public void onSuccess(Object t) {
				if (IndicatorActivity.this == null) {
					return;
				}
				System.out.println("warning--" + t.toString());
				JsonInfoV2 jsonInfov2 = null;
				try {
					jsonInfov2 = JSON.parseObject(t.toString(), JsonInfoV2.class);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "更新接口数据返回异常，请检查接口格式", 1000).show();
				}

				if (jsonInfov2 != null) {
					if (JsonInfo.SUCCESS_CODE.equals(jsonInfov2.getResultCode())) {// 正常返回数据
						JsonInfo jsonInfo = jsonInfov2.getResultDataToClass(JsonInfo.class);
						warning = jsonInfo.getData_exp(Warning.class);
					} else {
						Toast.makeText(getApplicationContext(), jsonInfov2.getResultDesc(), 1000).show();// 显示失败提示
					}

				}
			}
		});

	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "在按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void clickWeek(View v) {
		mWeek.performClick();
	}

	public void clickdur(View v) {
		mSelect_dur.performClick();
	}

	public void clicktime(View v) {
		mZb_time.performClick();
	}

}
