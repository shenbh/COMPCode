package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.fragment.AttendanceFilingFragment;
import com.newland.comp.fragment.AttendanceLeaveFragment;
import com.newland.comp.fragment.AttendanceOverTimeFragment;
import com.newland.comp.fragment.AttendanceStatisticsFragment;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的考勤
 * 
 * @author H81
 * 
 */
public class MyAttendanceActivity extends FragmentActivity {
	// private HorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout hor_lin;
	// 加班统计
	private LinearLayout mLl_overtime_statistics;
	private TextView mTv_overtime_statistics;
	private ImageView mIv_overtime_statistics;
	// 请假统计
	private LinearLayout mLl_leave_statistics;
	private TextView mTv_leave_statistics;
	private ImageView mIv_leave_statistics;
	// 考勤统计
	private LinearLayout mLl_attendance_statistics;
	private TextView mTv_attendance_statistics;
	private ImageView mIv_attendance_statistics;
	// 考勤报备
	private LinearLayout mLl_attendance_filing;
	private TextView mTv_attendance_filing;
	private ImageView mIv_attendance_filing;
	// webview
	private WebView mWebview;

	private TextView timeView; // 选择日期
//	private TextView title; // 标题

	private AttendanceOverTimeFragment attendanceOverTimeFragment;// 加班统计
	private AttendanceLeaveFragment attendanceLeaveFragment;// 请假统计
	private AttendanceStatisticsFragment attendanceStatisticsFragment;// 考勤统计
	private AttendanceFilingFragment attendanceFilingFragment;// 考勤报备
	private BaseFragment currentFragment;// 当前选中的fragment
	private ViewPager mViewPager;
	private List<BaseFragment> listFragments;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;

	private void bindViews() {
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		hor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		mLl_overtime_statistics = (LinearLayout) findViewById(R.id.ll_overtime_statistics);
		mTv_overtime_statistics = (TextView) findViewById(R.id.tv_overtime_statistics);
		mIv_overtime_statistics = (ImageView) findViewById(R.id.iv_overtime_statistics);
		mLl_leave_statistics = (LinearLayout) findViewById(R.id.ll_leave_statistics);
		mTv_leave_statistics = (TextView) findViewById(R.id.tv_leave_statistics);
		mIv_leave_statistics = (ImageView) findViewById(R.id.iv_leave_statistics);
		mLl_attendance_statistics = (LinearLayout) findViewById(R.id.ll_attendance_statistics);
		mTv_attendance_statistics = (TextView) findViewById(R.id.tv_attendance_statistics);
		mIv_attendance_statistics = (ImageView) findViewById(R.id.iv_attendance_statistics);
		mLl_attendance_filing = (LinearLayout) findViewById(R.id.ll_attendance_filing);
		mTv_attendance_filing = (TextView) findViewById(R.id.tv_attendance_filing);
		mIv_attendance_filing = (ImageView) findViewById(R.id.iv_attendance_filing);
		mWebview = (WebView) findViewById(R.id.webview);

		initData();
	}

	/**
	 * 初始化viewpager颜色
	 */
	private void initData() {
		mIv_leave_statistics.setVisibility(View.INVISIBLE);
		mIv_attendance_statistics.setVisibility(View.INVISIBLE);
		mIv_attendance_filing.setVisibility(View.INVISIBLE);

		mTv_leave_statistics.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_attendance_statistics.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_attendance_filing.setTextColor(this.getResources().getColor(R.color.gray));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_attendance);
		getDm();
		setTitle();
		bindViews();
		initFragment();
	}


	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		timeView = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("我的考勤");
		if (left_btn != null)
			left_btn.setVisibility(View.VISIBLE);
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (timeView != null) {
			timeView.setVisibility(View.VISIBLE);
			timeView.setText(StringUtil.getNowTime(StringUtil.MONTH_TIME));
			timeView.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View arg0) {
					new DatePickDialog(MyAttendanceActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(timeView, currentFragment);
				}
			});
		}
	}
	/**
	 * 获取屏幕宽度
	 */
	private void getDm() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		attendanceOverTimeFragment = AttendanceOverTimeFragment.newInstance(MyAttendanceActivity.this);
		attendanceLeaveFragment = AttendanceLeaveFragment.newInstance(MyAttendanceActivity.this);
		attendanceStatisticsFragment = AttendanceStatisticsFragment.newInstance(MyAttendanceActivity.this);
		attendanceFilingFragment = AttendanceFilingFragment.newInstance(MyAttendanceActivity.this);

		listFragments.add(attendanceOverTimeFragment);
		listFragments.add(attendanceLeaveFragment);
		listFragments.add(attendanceStatisticsFragment);
		listFragments.add(attendanceFilingFragment);

		BaseFragmentPagerAdapter mAdapetr = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

		mViewPager.setOffscreenPageLimit(0);
		
		currentFragment = attendanceOverTimeFragment;
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
				mIv_overtime_statistics.setVisibility(View.VISIBLE);
				mTv_overtime_statistics.setTextColor(MyAttendanceActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 1:
				mIv_leave_statistics.setVisibility(View.VISIBLE);
				mTv_leave_statistics.setTextColor(MyAttendanceActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 2:
				mIv_attendance_statistics.setVisibility(View.VISIBLE);
				mTv_attendance_statistics.setTextColor(MyAttendanceActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 3:
				mIv_attendance_filing.setVisibility(View.VISIBLE);
				mTv_attendance_filing.setTextColor(MyAttendanceActivity.this.getResources().getColor(R.color.app_green));
				break;
			default:
				break;
			}
			selectTab(position);
			currentFragment.refresh();//保证每个页面都能实时刷新
		}
	};

	private void clearPress() {

		mIv_overtime_statistics.setVisibility(View.INVISIBLE);
		mIv_leave_statistics.setVisibility(View.INVISIBLE);
		mIv_attendance_statistics.setVisibility(View.INVISIBLE);
		mIv_attendance_filing.setVisibility(View.INVISIBLE);

		mTv_overtime_statistics.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_leave_statistics.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_attendance_statistics.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_attendance_filing.setTextColor(this.getResources().getColor(R.color.gray));
	}

	private void selectTab(int tab_postion) {
		for (int i = 0; i < hor_lin.getChildCount(); i++) {
			View checkView = hor_lin.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
		}
	}

	public void onClick(View view) {
		clearPress();
		if (view.getId() == R.id.ll_overtime_statistics) // 加班统计
		{

			mIv_overtime_statistics.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		} else if (view.getId() == R.id.ll_leave_statistics) // 请假统计
		{
			mIv_leave_statistics.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		} else if (view.getId() == R.id.ll_attendance_statistics) // 考勤统计
		{
			mIv_attendance_statistics.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
		}

		else if (view.getId() == R.id.ll_attendance_filing) // 考勤报备
		{
			mIv_attendance_filing.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(3);
		} 
		else if (view.getId() == R.id.head_left_btn) // 退出
		{
			finish();
		} 

	}
}
