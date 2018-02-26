package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.fragment.AttendanceFilingFragment;
import com.newland.comp.fragment.AttendanceLeaveFragment;
import com.newland.comp.fragment.AttendanceOverTimeFragment;
import com.newland.comp.fragment.AttendanceStatisticsAbsenteeismFragment;
import com.newland.comp.fragment.AttendanceStatisticsBeLateFragment;
import com.newland.comp.fragment.AttendanceStatisticsFragment;
import com.newland.comp.fragment.AttendanceStatisticsLeaveEarlyFragment;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的考勤-考勤明细--包含迟到、早退、旷工，三个Fragment
 * 
 * @author H81
 * 
 */
public class MyAttendanceStatisticsActivity extends FragmentActivity {

	private LinearLayout mHor_lin;
	// 迟到
	private LinearLayout mLl_belate;
	private TextView mTv_belate;
	private ImageView mIv_belate;
	// 早退
	private LinearLayout mLl_leaveearly;
	private TextView mTv_leaveearly;
	private ImageView mIv_leaveearly;
	// 旷工
	private LinearLayout mLl_absenteeism;
	private TextView mTv_absenteeism;
	private ImageView mIv_absenteeism;
	private android.support.v4.view.ViewPager mMViewPager;

	private AttendanceStatisticsBeLateFragment attendanceStatisticsBeLateFragment;// 迟到
	private AttendanceStatisticsLeaveEarlyFragment attendanceStatisticsLeaveEarlyFragment;// 早退
	private AttendanceStatisticsAbsenteeismFragment attendanceStatisticsAbsenteeismFragment;// 旷工
	private BaseFragment currentFragment;// 当前选中的Fragment
	private ViewPager mViewPager;
	private List<BaseFragment> listFragments;
	private int mScreenWidth = 0;// 屏幕宽度

	private String month;// 月份

	
	private TextView right_tv;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		month = getIntent().getStringExtra("month");
		setContentView(R.layout.my_attendance_statistics_detail);
		setTitle();
		bindViews();
		initData();
		initFragment();
	}

	/**
	 * 设置标题栏
	 */
	private void setTitle() {
		ImageButton left_btn = (ImageButton) findViewById(R.id.head_left_btn);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("考勤明细");
		if (left_btn != null) {// 返回
			left_btn.setVisibility(View.VISIBLE);
		}
		left_btn.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				MyAttendanceStatisticsActivity.this.finish();
			}
		});
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {// 日期
			right_tv.setVisibility(View.GONE);
//			if (right_tv.getText().toString().equals(null)) {// 判断textview里面是否有时间内容
				right_tv.setText(month);
//			}
			right_tv.setOnClickListener(new OnClickListener() {

				
				public void onClick(View arg0) {
					new DatePickDialog(MyAttendanceStatisticsActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(right_tv, currentFragment);
				}
			});
		}
	}

	/**
	 * 初始化viewpager颜色
	 */
	private void initData() {
		mIv_leaveearly.setVisibility(View.INVISIBLE);
		mIv_absenteeism.setVisibility(View.INVISIBLE);

		mTv_leaveearly.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_absenteeism.setTextColor(this.getResources().getColor(R.color.gray));
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		attendanceStatisticsBeLateFragment = AttendanceStatisticsBeLateFragment.newInstance(MyAttendanceStatisticsActivity.this);
		attendanceStatisticsLeaveEarlyFragment = AttendanceStatisticsLeaveEarlyFragment.newInstance(MyAttendanceStatisticsActivity.this);
		attendanceStatisticsAbsenteeismFragment = AttendanceStatisticsAbsenteeismFragment.newInstance(MyAttendanceStatisticsActivity.this);


		listFragments.add(attendanceStatisticsBeLateFragment);
		listFragments.add(attendanceStatisticsLeaveEarlyFragment);
		listFragments.add(attendanceStatisticsAbsenteeismFragment);

		BaseFragmentPagerAdapter mAdapetr = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
		mViewPager.setOffscreenPageLimit(3);
		currentFragment = attendanceStatisticsBeLateFragment;
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
				mIv_belate.setVisibility(View.VISIBLE);
				mTv_belate.setTextColor(MyAttendanceStatisticsActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 1:
				mIv_leaveearly.setVisibility(View.VISIBLE);
				mTv_leaveearly.setTextColor(MyAttendanceStatisticsActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 2:
				mIv_absenteeism.setVisibility(View.VISIBLE);
				mTv_absenteeism.setTextColor(MyAttendanceStatisticsActivity.this.getResources().getColor(R.color.app_green));
				break;
			default:
				break;
			}
			selectTab(position);
		}
	};

	private void clearPress() {

		mIv_belate.setVisibility(View.INVISIBLE);
		mIv_leaveearly.setVisibility(View.INVISIBLE);
		mIv_absenteeism.setVisibility(View.INVISIBLE);

		mTv_belate.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_leaveearly.setTextColor(this.getResources().getColor(R.color.gray));
		mTv_absenteeism.setTextColor(this.getResources().getColor(R.color.gray));
	}

	private void selectTab(int tab_postion) {
		for (int i = 0; i < mHor_lin.getChildCount(); i++) {
			View checkView = mHor_lin.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
		}
	}

	private void bindViews() {
		mHor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		// 迟到
		mLl_belate = (LinearLayout) findViewById(R.id.ll_belate);
		mTv_belate = (TextView) findViewById(R.id.tv_belate);
		mIv_belate = (ImageView) findViewById(R.id.iv_belate);
		// 早退
		mLl_leaveearly = (LinearLayout) findViewById(R.id.ll_leaveearly);
		mTv_leaveearly = (TextView) findViewById(R.id.tv_leaveearly);
		mIv_leaveearly = (ImageView) findViewById(R.id.iv_leaveearly);
		// 旷工
		mLl_absenteeism = (LinearLayout) findViewById(R.id.ll_absenteeism);
		mTv_absenteeism = (TextView) findViewById(R.id.tv_absenteeism);
		mIv_absenteeism = (ImageView) findViewById(R.id.iv_absenteeism);
		mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.mViewPager);
	}

	public void onClick(View view) {
		clearPress();
		if (view.getId() == R.id.ll_belate) // 迟到
		{
			mIv_belate.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		} else if (view.getId() == R.id.ll_leaveearly) // 早退
		{
			mIv_leaveearly.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		} else if (view.getId() == R.id.ll_absenteeism) // 旷工
		{
			mIv_absenteeism.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
		}

		else if (view.getId() == R.id.head_left_btn) // 退出
		{
			finish();
		}
	}
}
