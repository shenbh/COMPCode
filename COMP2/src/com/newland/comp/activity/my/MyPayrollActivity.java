package com.newland.comp.activity.my;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.comp.adapter.BaseFragmentPagerAdapter;
import com.newland.comp.common.AppContext;
import com.newland.comp.fragment.BaseFragment;
import com.newland.comp.fragment.CheckInFragment;
import com.newland.comp.fragment.IndicatorOperationFragment;
import com.newland.comp.fragment.MarkingWorkloadFragment;
import com.newland.comp.fragment.MyPayStubFragment;
import com.newland.comp.fragment.PayrollNightFragment;
import com.newland.comp.fragment.PayrollOthersFragment;
import com.newland.comp.fragment.QosFragment;
import com.newland.comp.fragment.WorkLoadFragment;
import com.newland.comp.test.TestActivity;
import com.newland.comp.utils.ActivityUtil;
import com.newland.comp.utils.StringUtil;
import com.newland.comp.view.DatePickDialog;
import com.newland.comp2.R;

/**
 * 我的薪酬
 * 
 * @author H81
 * 
 */
public class MyPayrollActivity extends FragmentActivity {

	private LinearLayout mGzd;
	private ImageView mGzd_iv;
	private LinearLayout mGzl;
	private ImageView mGzl_iv;
	private LinearLayout mFwzz;
	private ImageView mFwzz_iv;
	private LinearLayout mKqxx;
	private ImageView mKqxx_iv;
	private LinearLayout mYxgzl;
	private ImageView mYxzl_iv;
	private LinearLayout mYbsc;
	private ImageView mYbsc_iv;
	private LinearLayout mQtxx;
	private ImageView mQtxx_iv;
	private TextView timeView; // 选择日期
	private TextView title; // 标题

	private TextView mGzd_tv;
	private TextView mGzl_tv;
	private TextView mFwzz_tv;
	private TextView mKqxx_tv;
	private TextView mYxzl_tv;
	private TextView mYbsc_tv;
	private TextView mQtxx_tv;

	HorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout hor_lin;

	private MyPayStubFragment myPayStubFragment;// 工资单
	private WorkLoadFragment workLoadFragment; // 工作量
	private QosFragment qosFragment; // 服务质量
	private CheckInFragment checkinFra; // 考勤信息
	private MarkingWorkloadFragment maringWorkloadFra;// 营销工作量
	private PayrollNightFragment payrollNightFragment;// 夜班时长
	private PayrollOthersFragment payrollOthersFragment;// 其他
	private BaseFragment currentFragment;// 当前选中的fragment
	private ViewPager mViewPager;
	private List<BaseFragment> listFragments;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.my_payroll2);
		initView();
		initFragment();
	}

	private void initView() {
		hor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		mGzd = (LinearLayout) findViewById(R.id.gzd);
		mGzd_iv = (ImageView) findViewById(R.id.gzd_iv);
		mGzl = (LinearLayout) findViewById(R.id.gzl);
		mGzl_iv = (ImageView) findViewById(R.id.gzl_iv);
		mFwzz = (LinearLayout) findViewById(R.id.fwzz);
		mFwzz_iv = (ImageView) findViewById(R.id.fwzz_iv);
		mKqxx = (LinearLayout) findViewById(R.id.kqxx);
		mKqxx_iv = (ImageView) findViewById(R.id.kqxx_iv);
		mYxgzl = (LinearLayout) findViewById(R.id.yxgzl);
		mYxzl_iv = (ImageView) findViewById(R.id.yxzl_iv);
		mYbsc = (LinearLayout) findViewById(R.id.ybsc);
		mYbsc_iv = (ImageView) findViewById(R.id.ybsc_iv);
		mQtxx = (LinearLayout) findViewById(R.id.qtxx);
		mQtxx_iv = (ImageView) findViewById(R.id.qtxx_iv);
		mColumnHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mGzd_tv = (TextView) findViewById(R.id.gzd_tv);
		mGzl_tv = (TextView) findViewById(R.id.gzl_tv);
		mFwzz_tv = (TextView) findViewById(R.id.fwzz_tv);
		mKqxx_tv = (TextView) findViewById(R.id.kqxx_tv);
		mYxzl_tv = (TextView) findViewById(R.id.yxzl_tv);
		mYbsc_tv = (TextView) findViewById(R.id.ybsc_tv);
		mQtxx_tv = (TextView) findViewById(R.id.qtxx_tv);

		findViewById(R.id.head_right_btn).setVisibility(View.GONE);
		findViewById(R.id.head_left_btn).setVisibility(View.VISIBLE);
		timeView = (TextView) findViewById(R.id.head_right_tv);
		title = (TextView) findViewById(R.id.head_center_title);
		timeView.setVisibility(View.VISIBLE);
		title.setText("我的薪酬");
		timeView.setText(StringUtil.getNowTime(StringUtil.MONTH_TIME));
		timeView.setOnClickListener(new OnClickListener() {

			
			public void onClick(View arg0) {
				new DatePickDialog(MyPayrollActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(timeView, currentFragment);

			}
		});
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
	}

	/**
	 * 初始化Fragment
	 * */
	private void initFragment() {
		listFragments = new ArrayList<BaseFragment>();
		myPayStubFragment = MyPayStubFragment.newInstance(MyPayrollActivity.this);
		currentFragment = myPayStubFragment;
		workLoadFragment = WorkLoadFragment.newInstance(MyPayrollActivity.this);
		qosFragment = QosFragment.newInstance(MyPayrollActivity.this);
		checkinFra = CheckInFragment.newInstance(MyPayrollActivity.this);
		maringWorkloadFra = MarkingWorkloadFragment.newInstance(MyPayrollActivity.this);
		payrollNightFragment = PayrollNightFragment.newInstance(MyPayrollActivity.this);
		payrollOthersFragment = PayrollOthersFragment.newInstance(MyPayrollActivity.this);

		listFragments.add(myPayStubFragment);
		listFragments.add(workLoadFragment);
		listFragments.add(qosFragment);
		listFragments.add(checkinFra);
		listFragments.add(maringWorkloadFra);
		listFragments.add(payrollNightFragment);
		listFragments.add(payrollOthersFragment);

		BaseFragmentPagerAdapter mAdapetr = new BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

		mViewPager.setOffscreenPageLimit(0);
		currentFragment = myPayStubFragment;
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
				mGzd_iv.setVisibility(View.VISIBLE);
				mGzd_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 1:
				mGzl_iv.setVisibility(View.VISIBLE);
				mGzl_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 2:
				mFwzz_iv.setVisibility(View.VISIBLE);
				mFwzz_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 3:
				mKqxx_iv.setVisibility(View.VISIBLE);
				mKqxx_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 4:
				mYxzl_iv.setVisibility(View.VISIBLE);
				mYxzl_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 5:
				mYbsc_iv.setVisibility(View.VISIBLE);
				mYbsc_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;
			case 6:
				mQtxx_iv.setVisibility(View.VISIBLE);
				mQtxx_tv.setTextColor(MyPayrollActivity.this.getResources().getColor(R.color.app_green));
				break;

			default:
				break;
			}

			selectTab(position);
			currentFragment.refresh();// 保证每个页面都能实时刷新
		}
	};

	private void selectTab(int tab_postion) {
		for (int i = 0; i < hor_lin.getChildCount(); i++) {
			View checkView = hor_lin.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
	}

	private void clearPress() {

		mGzd_iv.setVisibility(View.INVISIBLE);
		mGzl_iv.setVisibility(View.INVISIBLE);
		mFwzz_iv.setVisibility(View.INVISIBLE);
		mKqxx_iv.setVisibility(View.INVISIBLE);
		mYxzl_iv.setVisibility(View.INVISIBLE);
		mYbsc_iv.setVisibility(View.INVISIBLE);
		mQtxx_iv.setVisibility(View.INVISIBLE);

		mGzd_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mGzl_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mFwzz_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mKqxx_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mYxzl_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mYbsc_tv.setTextColor(this.getResources().getColor(R.color.gray));
		mQtxx_tv.setTextColor(this.getResources().getColor(R.color.gray));

	}

	public void onClick(View view) {
		clearPress();
		if (view.getId() == R.id.gzd) // 工资单
		{

			mGzd_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(0);
		} else if (view.getId() == R.id.gzl) // 工作量
		{
			mGzl_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(1);
		} else if (view.getId() == R.id.fwzz) // 服务质量
		{
			mFwzz_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(2);
		}

		else if (view.getId() == R.id.kqxx) // 考勤信息
		{
			mKqxx_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(3);
		} else if (view.getId() == R.id.yxgzl) // 营销工作量
		{
			mYxzl_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(4);
		}

		else if (view.getId() == R.id.ybsc) // 夜班时长
		{
			mYbsc_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(5);
		} else if (view.getId() == R.id.qtxx) // 其他信息
		{
			mQtxx_iv.setVisibility(View.VISIBLE);
			mViewPager.setCurrentItem(6);
		} else if (view.getId() == R.id.head_left_btn) // 退出
		{
			finish();
		} else if (view.getId() == R.id.head_right_tv) // 时间选择
		{
			new DatePickDialog(MyPayrollActivity.this, DatePickDialog.YEAR_MONTH).datePicKDialog(timeView, currentFragment);
		}

	}
	

}
